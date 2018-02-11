package com.windforce.common.ramcache.service;

import java.io.Serializable;

import com.windforce.common.ramcache.IEntity;

public interface EntityBuilder<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {
	
	T newInstance(PK id);

}
