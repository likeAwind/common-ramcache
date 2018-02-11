package com.windforce.common.ramcache.enhance;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.exception.EnhanceException;

/**
 * 实体类实例增强器接口
 * @author frank
 */
public interface Enhancer {

	/**
	 * 将指定实体的类实例转换为增强类实例
	 * @param entity
	 * @return
	 * @throws EnhanceException
	 */
	@SuppressWarnings("rawtypes")
	<T extends IEntity> T transform(T entity) throws EnhanceException;

}
