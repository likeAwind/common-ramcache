package com.windforce.common.ramcache.enhance;

import com.windforce.common.ramcache.IEntity;

/**
 * 被增强过的实体必须实现该接口
 * @author frank
 */
public interface EnhancedEntity {

	/**
	 * 获取被增强前的实体对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	IEntity getEntity();
}
