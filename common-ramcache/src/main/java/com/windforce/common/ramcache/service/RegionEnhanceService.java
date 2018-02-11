package com.windforce.common.ramcache.service;

import java.io.Serializable;

import com.windforce.common.ramcache.IEntity;

/**
 * 区域增强服务接口
 * @author frank
 *
 * @param <PK>
 * @param <T>
 */
public interface RegionEnhanceService<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> extends EnhanceService<PK, T> {

	/**
	 * 修改索引值的方法
	 * @param name 索引属性名
	 * @param entity 修改的实体 
	 * @param prev 之前的值
	 */
	void changeIndexValue(String name, T entity, Object prev);

}
