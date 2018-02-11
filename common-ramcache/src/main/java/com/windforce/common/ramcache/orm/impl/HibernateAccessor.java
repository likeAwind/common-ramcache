package com.windforce.common.ramcache.orm.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.orm.Accessor;

/**
 * {@link Accessor} 的 Hibernate 实现
 * 
 * @author frank
 */
@SuppressWarnings("rawtypes")
public class HibernateAccessor extends HibernateDaoSupport implements Accessor {

	@Override
	public <PK extends Serializable, T extends IEntity> T load(Class<T> clz, PK id) {
		return getHibernateTemplate().get(clz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <PK extends Serializable, T extends IEntity> PK save(Class<T> clz, T entity) {
		return (PK) getHibernateTemplate().save(entity);
	}

	@Override
	public <PK extends Serializable, T extends IEntity> void remove(Class<T> clz, PK id) {
		T entity = load(clz, id);
		if (entity == null) {
			return;
		}
		getHibernateTemplate().delete(entity);
	}

	@Override
	public <PK extends Serializable, T extends IEntity> void update(Class<T> clz, T entity) {
		getHibernateTemplate().update(entity);
	}

	@Override
	public <PK extends Serializable, T extends IEntity> void batchSave(final List<T> entitys) {
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int size = entitys.size();
				session.beginTransaction();
				for (int i = 0; i < size; i++) {
					T eneity = entitys.get(i);
					session.save(eneity);
					if ((i + 1) % 50 == 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				session.getTransaction().commit();
				return size;
			}
		});
	}

	@Override
	public <PK extends Serializable, T extends IEntity> void batchUpdate(final List<T> entitys) {
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int size = entitys.size();
				session.beginTransaction();
				for (int i = 0; i < size; i++) {
					T eneity = entitys.get(i);
					session.update(eneity);
					if ((i + 1) % 10 == 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				session.getTransaction().commit();
				return size;
			}
		});
	}

	@Override
	public <PK extends Serializable, T extends IEntity> void batchDelete(final List<T> entitys) {
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int size = entitys.size();
				session.beginTransaction();
				for (int i = 0; i < size; i++) {
					T eneity = entitys.get(i);
					session.delete(eneity);
					if ((i + 1) % 1000 == 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				session.getTransaction().commit();
				return size;
			}
		});
	}

}
