package com.windforce.common.ramcache.schema;

import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.FactoryBean;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.ServiceManager;
import com.windforce.common.ramcache.orm.Accessor;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.persist.PersisterConfig;

/**
 * 缓存服务管理器工厂
 * @author frank
 */
@SuppressWarnings("rawtypes")
public class ServiceManagerFactory implements FactoryBean<ServiceManager> {
	
	public static final String ENTITY_CLASSES_NAME = "entityClasses";
	public static final String PERSISTER_CONFIG_NAME = "persisterConfig";

	private Accessor accessor;
	private Querier querier;
	private Set<Class<IEntity>> entityClasses;
	private Map<String, PersisterConfig> persisterConfig;
	private Map<String, Integer> constants;
	
	private ServiceManager cacheServiceManager;
	
	@Override
	public ServiceManager getObject() throws Exception {
		cacheServiceManager = new ServiceManager(entityClasses, accessor, querier, constants, persisterConfig);
		return cacheServiceManager;
	}

	@PreDestroy
	public void shutdown() {
		if (cacheServiceManager == null) {
			return;
		}
		cacheServiceManager.shutdown();
	}
	
	// Setter Methods ...
	
	public void setAccessor(Accessor accessor) {
		this.accessor = accessor;
	}


	public void setQuerier(Querier querier) {
		this.querier = querier;
	}
	
	public void setEntityClasses(Set<Class<IEntity>> entityClasses) {
		this.entityClasses = entityClasses;
	}
	
	public void setPersisterConfig(Map<String, PersisterConfig> persisterConfig) {
		this.persisterConfig = persisterConfig;
	}
	
	public void setConstants(Map<String, Integer> constants) {
		this.constants = constants;
	}
	
	// Other Methods

	@Override
	public Class<?> getObjectType() {
		return ServiceManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
