package com.windforce.common.ramcache.service;

import java.io.Serializable;
import java.util.Collection;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.CachedEntityConfig;
import com.windforce.common.ramcache.persist.Persister;

/**
 * 区域缓存服务接口
 * @author frank
 *
 * @param <PK>
 * @param <T>
 */
public interface RegionCacheService<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {

	/**
	 * 加载指定区域的实体集合
	 * @param idx 索引值
	 * @return 不可修改的列表
	 */
	Collection<T> load(IndexValue idx);

	/**
	 * 获取某一组件的实体
	 * @param idx
	 * @param id
	 * @return
	 */
	T get(IndexValue idx, PK id);
	
	/**
	 * 创建新的实体
	 * @param entity
	 */
	T create(T entity);
	
	/**
	 * 移除指定实体(异步)
	 * @param entity
	 */
	void remove(T entity);
	
	/**
	 * 清除指定的区域缓存
	 * @param idx
	 */
	void clear(IndexValue idx);
	
	/**
	 * 将缓存中的指定实体回写到存储层(异步)
	 * @param id 主键
	 * @param T 回写实体实例
	 */
	void writeBack(PK id, T entity);

	/**
	 * 获取实体缓存配置信息
	 * @return
	 */
	CachedEntityConfig getEntityConfig();

	/**
	 * 获取对应的持久化处理器
	 * @return
	 */
	Persister getPersister();

}
