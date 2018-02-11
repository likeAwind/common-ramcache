package com.windforce.common.ramcache.orm.impl;
//package com.my9yu.common.ramcache.orm.impl;
//
//import java.io.Serializable;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.my9yu.common.bdb.AccessorFactory;
//import com.my9yu.common.ramcache.IEntity;
//import com.my9yu.common.ramcache.orm.Accessor;
//
//@SuppressWarnings({"unchecked", "rawtypes"})
//public class BdbAccessor implements Accessor {
//	
//	@Autowired
//	private AccessorFactory accessorFactory;
//
//	@Override
//	public <PK extends Serializable, T extends IEntity> T load(Class<T> clz, PK id) {
//		com.my9yu.common.bdb.Accessor<PK, T> accessor = (com.my9yu.common.bdb.Accessor<PK, T>) accessorFactory.getAccessor(clz);
//		return accessor.get(id);
//	}
//
//	@Override
//	public <PK extends Serializable, T extends IEntity> PK save(Class<T> clz, T entity) {
//		com.my9yu.common.bdb.Accessor<PK, T> accessor = (com.my9yu.common.bdb.Accessor<PK, T>) accessorFactory.getAccessor(clz);
//		return accessor.save(entity);
//	}
//
//	@Override
//	public <PK extends Serializable, T extends IEntity> void remove(Class<T> clz, PK id) {
//		com.my9yu.common.bdb.Accessor<PK, T> accessor = (com.my9yu.common.bdb.Accessor<PK, T>) accessorFactory.getAccessor(clz);
//		accessor.delete(id);
//	}
//
//	@Override
//	public <PK extends Serializable, T extends IEntity> void update(Class<T> clz, T entity) {
//		com.my9yu.common.bdb.Accessor<PK, T> accessor = (com.my9yu.common.bdb.Accessor<PK, T>) accessorFactory.getAccessor(clz);
//		accessor.update(entity);
//	}
//
//}
