package com.windforce.common.ramcache.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.orm.Accessor;
import com.windforce.common.utility.DateUtils;

/**
 * 消费者
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月11日
 *
 */
public class TimingConsumer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(TimingConsumer.class);

	/** 更新队列名 */
	private String name;
	/** 定期入库CRON */
	private String cron;
	/** 持久层的存储器 */
	private Accessor accessor;
	/** 实体持久化缓存 */
	private TimingPersister owner;
	/** 当前消费者线程自身 */
	private Thread me;
	/** 状态 */
	private TimingConsumerState state;
	private volatile boolean stoped;
	/** 下次执行的时间 */
	private Date nextTime;
	/** 错误计数器 */
	private AtomicInteger error = new AtomicInteger();

	public TimingConsumer(String name, String cron, Accessor accessor, TimingPersister owner) {
		this.name = name;
		this.cron = cron;
		this.accessor = accessor;
		this.owner = owner;
		this.me = new Thread(this, "持久化[" + name + ":定时]");
		me.setDaemon(true);
		me.start();
	}

	@Override
	public void run() {
		while (!stoped) {
			try {

				Collection<Element> elements = null;
				synchronized (me) {
					state = TimingConsumerState.WAITING;
					if (!interrupted) {
						nextTime = DateUtils.getNextTime(cron, new Date());
						if (logger.isDebugEnabled()) {
							logger.debug("定时入库[{}]的下个执行时间点为[{}]", name,
									DateUtils.date2String(nextTime, DateUtils.PATTERN_DATE_TIME));
						}
						try {
							long ms = nextTime.getTime() - System.currentTimeMillis();
							if (ms > 0) {
								me.wait(ms);
							}
						} catch (InterruptedException e) {
							if (logger.isDebugEnabled()) {
								logger.debug("定时入库[{}]被迫使立即执行[{}]", name,
										DateUtils.date2String(new Date(), DateUtils.PATTERN_DATE_TIME));
							}
							return;
						}
					}
					elements = owner.clearElements();
					interrupted = false;
					state = TimingConsumerState.RUNNING;
				}
				Date start = new Date();
				if (logger.isDebugEnabled()) {
					logger.debug("定时入库[{}]开始[{}]执行", name, DateUtils.date2String(start, DateUtils.PATTERN_DATE_TIME));
				}
				persist(elements);
				if (logger.isDebugEnabled()) {
					logger.debug("定时入库[{}]入库[{}]条数据耗时[{}]",
							new Object[] { name, elements.size(), System.currentTimeMillis() - start.getTime() });
				}
			} catch (Throwable e) {
				logger.error("Timing存储线程非法中断!", e);
			}
		}
		synchronized (me) {
			Collection<Element> elements = owner.clearElements();
			persist(elements);
			state = TimingConsumerState.STOPPED;
			interrupted = false;
		}
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private void persist(Collection<Element> elements) {
	// for (Element element : elements) {
	// try {
	// Class clz = element.getEntityClass();
	// switch (element.getType()) {
	// case SAVE:
	// // 如果序列化成功，才存储
	// if (element.getEntity().serialize()) {
	// accessor.save(clz, element.getEntity());
	// }
	// break;
	// case REMOVE:
	// accessor.remove(clz, element.getId());
	// break;
	// case UPDATE:
	// // 如果序列化成功，才存储
	// if (element.getEntity().serialize()) {
	// accessor.update(clz, element.getEntity());
	// }
	// break;
	// }
	//
	// Listener listener = owner.getListener(clz);
	// if (listener != null) {
	// listener.notify(element.getType(), true, element.getId(),
	// element.getEntity(), null);
	// }
	// } catch (RuntimeException e) {
	// error.getAndIncrement();
	// if (logger.isWarnEnabled()) {
	// logger.warn("实体更新队列[{}]处理元素[{}]时出现异常:{}", new Object[] { name, element,
	// e.getMessage() });
	// }
	// Listener listener = owner.getListener(element.getEntityClass());
	// if (listener != null) {
	// listener.notify(element.getType(), false, element.getId(),
	// element.getEntity(), e);
	// }
	// } catch (Exception e) {
	// error.getAndIncrement();
	// if (element == null) {
	// logger.error("获取更新队列元素时线程被非法打断", e);
	// } else {
	// logger.error("更新队列处理出现未知异常", e);
	// }
	// }
	// }
	// }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void persist(Collection<Element> elements) {
		List<IEntity> saveList = new ArrayList<IEntity>(500);
		List<IEntity> updateList = new ArrayList<IEntity>(500);
		List<IEntity> deleteList = new LinkedList<IEntity>();

		for (Element element : elements) {
			try {
				Class clz = element.getEntityClass();
				switch (element.getType()) {
				case INSERT:
					// 如果序列化成功，才存储
					if (element.getEntity().serialize()) {
						// accessor.save(clz, element.getEntity());
						saveList.add(element.getEntity());
					}
					break;
				case DELETE:
					accessor.remove(clz, element.getId());
					// deleteList.add(element.getId());
					break;
				case UPDATE:
					// 如果序列化成功，才存储
					if (element.getEntity().serialize()) {
						// accessor.update(clz, element.getEntity());
						updateList.add(element.getEntity());
					}
					break;
				}

				Listener listener = owner.getListener(clz);
				if (listener != null) {
					listener.notify(element.getType(), true, element.getId(), element.getEntity(), null);
				}
			} catch (RuntimeException e) {
				error.getAndIncrement();
				if (logger.isWarnEnabled()) {
					logger.warn("实体更新队列[{}]处理元素[{}]时出现异常:{}", new Object[] { name, element, e.getMessage() });
				}
				Listener listener = owner.getListener(element.getEntityClass());
				if (listener != null) {
					listener.notify(element.getType(), false, element.getId(), element.getEntity(), e);
				}
			} catch (Exception e) {
				error.getAndIncrement();
				if (element == null) {
					logger.error("获取更新队列元素时线程被非法打断", e);
				} else {
					logger.error("更新队列处理出现未知异常", e);
				}
			}
		}

		try {
			accessor.batchSave(saveList);
		} catch (Exception e) {
			logger.error("批量存储处理出现未知异常", e);
			// 如果失败了，就一个一个存储
			for (IEntity temp : saveList) {
				try {
					accessor.save(null, temp);
				} catch (Exception e1) {
					logger.error("存储处理出现未知异常", e1);
				}
			}
		}

		try {
			accessor.batchUpdate(updateList);
		} catch (Exception e) {
			logger.error("批量更新处理出现未知异常", e);
			// 如果失败了，就一个一个更新
			for (IEntity temp : updateList) {
				try {
					accessor.update(null, temp);
				} catch (Exception e1) {
					logger.error("更新处理出现未知异常", e1);
				}
			}
		}

		try {
			accessor.batchDelete(deleteList);
		} catch (Exception e) {
			logger.error("批量更新处理出现未知异常", e);
			// 如果失败了，就一个一个更新
			for (IEntity temp : deleteList) {
				try {
					accessor.remove(temp.getClass(), temp.getId());
				} catch (Exception e1) {
					logger.error("更新处理出现未知异常", e1);
				}
			}
		}

	}

	public TimingConsumerState getState() {
		synchronized (me) {
			return state;
		}
	}

	public void stop() {
		if (logger.isDebugEnabled()) {
			logger.debug("定时入库[{}]收到停止通知", name);
		}
		synchronized (me) {
			stoped = true;
			interrupted = true;
			me.notify();
		}
		while (interrupted) {
			Thread.yield();
		}
	}

	private volatile boolean interrupted;

	public void interrupt() {
		synchronized (me) {
			interrupted = true;
			me.notify();
		}
		while (interrupted) {
			Thread.yield();
		}
	}

	public Date getNextTime() {
		return nextTime;
	}

	public int getError() {
		return error.get();
	}
}
