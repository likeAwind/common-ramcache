package com.windforce.common.ramcache.lock;

/**
 * 测试用实体(String)
 * 
 * @author frank
 */
public class EntityString extends AbstractEntity<String> {

	public EntityString(String id) {
		super(id);
	}

	@Override
	public boolean serialize() {
		// TODO Auto-generated method stub
		return false;
	}
}