package com.windforce.common.ramcache.lock;

/**
 * 测试用实体(Integer)
 * @author frank
 */
public class EntityInteger extends AbstractEntity<Integer> {

	public EntityInteger(Integer id) {
		super(id);
	}

	@Override
	public boolean serialize() {
		// TODO Auto-generated method stub
		return false;
	}

}
