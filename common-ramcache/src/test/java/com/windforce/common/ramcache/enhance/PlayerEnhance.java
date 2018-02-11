package com.windforce.common.ramcache.enhance;

import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.CachedEntityConfig;
import com.windforce.common.ramcache.enhance.EnhancedEntity;
import com.windforce.common.ramcache.exception.UniqueFieldException;
import com.windforce.common.ramcache.service.EntityEnhanceService;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PlayerEnhance extends Player implements EnhancedEntity {
	
	private final EntityEnhanceService service;
	private final Player entity;
	
	public PlayerEnhance(EntityEnhanceService service, Player entity) {
		this.service = service;
		this.entity = entity;
	}
	
	@Override
	public IEntity getEntity() {
		return entity;
	}

	// Enhanced Methods

	public boolean increaseGold(int value) {
		boolean result = entity.increaseGold(value);
		if (String.valueOf(result).equals("true")) {
			service.writeBack(entity.getId(), entity);
		}
		return result;
	}

	public void charge(int value) throws IllegalStateException {
		try {
			entity.charge(value);
			service.writeBack(entity.getId(), entity);
		} catch (IllegalStateException e) {
			service.writeBack(entity.getId(), entity);
			throw e;
		}
	}

	public void setName(String name) {
		CachedEntityConfig config = service.getEntityConfig();
		WriteLock lock = config.getUniqueWriteLock("name");
		lock.lock();
		try {
			if (service.hasUniqueValue("name", name)) {
				throw new UniqueFieldException("唯一属性[" + "name" + "]值[" + name + "]已经存在");
			}
			entity.setName(name);
			service.replaceUniqueValue(entity.getId(), "name", name);
			service.writeBack(entity.getId(), entity);
		} finally {
			lock.unlock();
		}
	}

	// Delegate's ...
	
	public Integer getId() {
		return entity.getId();
	}

	public String getName() {
		return entity.getName();
	}

	public int getGold() {
		return entity.getGold();
	}

	public int hashCode() {
		return entity.hashCode();
	}

	public boolean equals(Object obj) {
		return entity.equals(obj);
	}

	public String toString() {
		return entity.toString();
	}

}
