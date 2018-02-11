package com.windforce.common.ramcache.persist;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.orm.Accessor;
import com.windforce.common.ramcache.persist.Element;
import com.windforce.common.ramcache.persist.EventType;
import com.windforce.common.ramcache.persist.Listener;
import com.windforce.common.ramcache.persist.TimingPersister;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SuppressWarnings("rawtypes")
public class EntityQueueTest {

	@Autowired
	private Accessor accessor;
	
	private TimingPersister queue;

	@Test
	public void test_listener() throws InterruptedException {
		queue = new TimingPersister();
		class TestListener implements Listener {
			EventType type;
			Serializable id;
			IEntity entity;
			boolean success;
			@Override
			public void notify(EventType type, boolean isSuccess, Serializable id, IEntity entity, RuntimeException ex) {
				this.success = isSuccess;
				this.type = type;
				this.id = id;
				this.entity = entity;
			}
		}
		TestListener listener = new TestListener();
		queue.addListener(Person.class, listener);
		queue.initialize("test_listener", accessor, "* * * * * *");
		
		// 测试保存
		Person entity = new Person(1, "frank");
		queue.put(Element.saveOf(entity));
		Thread.sleep(1000);
		assertThat(listener.success, is(true));
		assertThat(listener.type, is(EventType.INSERT));
		assertThat((Person) listener.entity, sameInstance(entity));
		assertThat((Integer) listener.id, is(1));
		
		queue.put(Element.saveOf(entity));
		Thread.sleep(1000);
		assertThat(listener.success, is(false));
		assertThat(listener.type, is(EventType.INSERT));
		
		// 测试更新
		entity.setName("edit");
		queue.put(Element.updateOf(entity));
		Thread.sleep(1000);
		assertThat(listener.success, is(true));
		assertThat(listener.type, is(EventType.UPDATE));
		
		// 测试删除
		queue.put(Element.removeOf(entity.getId(), Person.class));
		Thread.sleep(1000);
		assertThat(listener.success, is(true));
		assertThat(listener.type, is(EventType.DELETE));
	}
	
	@Test
	public void test_identity() throws InterruptedException {
		queue = new TimingPersister();
		queue.initialize("test_identity", accessor, "* * * * * *");
		assertThat(queue.size(), is(0));
//		queue.pause();
		
		Person entity = new Person(1, "frank");
		queue.put(Element.updateOf(entity));
		assertThat(queue.size(), is(1));
		queue.put(Element.updateOf(entity));
		assertThat(queue.size(), is(1));
		
//		queue.unpause();
		Thread.sleep(1000);
		assertThat(queue.size(), is(0));
	}
}
