package com.windforce.common.ramcache.anno;

/**
 * 缓存管理方式
 * @author frank
 */
public enum CacheType {
	
	/** 按LRU策略管理缓存内容 */
	LRU,
	
	/** 手动管理(实体不删除时不会从缓存中移除) */
	MANUAL;
	
}
