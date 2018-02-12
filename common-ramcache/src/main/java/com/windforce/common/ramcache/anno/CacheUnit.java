package com.windforce.common.ramcache.anno;

/**
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月12日
 *
 */
public enum CacheUnit {

	/** 实体 */
	ENTITY,
	/**
	 * 区域 PS:不建议使用,该缓存方式存在还未解决的BUG
	 */
	@Deprecated
	REGION;
}
