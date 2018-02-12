package com.windforce.common.ramcache.anno;

/**
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月12日
 *
 */
public enum CacheType {

	/** 按LRU策略管理缓存内容 */
	LRU,

	/** 手动管理(实体不删除时不会从缓存中移除) */
	MANUAL;

}
