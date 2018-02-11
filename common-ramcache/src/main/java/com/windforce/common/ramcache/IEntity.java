package com.windforce.common.ramcache;

import java.io.Serializable;

/**
 * 实体标识接口，用于告知锁创建器具体类实例是实体对象
 * 
 * @author frank
 */
public interface IEntity<PK extends Serializable & Comparable<PK>> {

	/**
	 * 获取实体标识
	 * 
	 * @return
	 */
	public PK getId();

	/**
	 * 序列化方法，这个方法会延迟到这个实体被存储的时候才会被调用，当返回为false的时候，就不存储 这个可以用来对付蝗虫玩家
	 * 
	 * @return
	 */
	public boolean serialize();
}
