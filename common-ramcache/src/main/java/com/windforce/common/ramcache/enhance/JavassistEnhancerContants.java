package com.windforce.common.ramcache.enhance;

/**
 * 类增强常量定义
 * @author frank
 */
public interface JavassistEnhancerContants {

	/** 增强类后缀 */
	String CLASS_SUFFIX = "$ENHANCED";

	/** 增强类域名:服务域 */
	String FIELD_SERVICE = "service";

	/** 增强类域名:实体域 */
	String FIELD_ENTITY = "entity";

	/** 增强类接口:获取实体域 */
	String METHOD_GET_ENTITY = "getEntity";

	/** 类型:缓存实体配置类 */
	String TYPE_CACHED_ENTITY_CONFIG = "com.my9yu.common.ramcache.anno.CachedEntityConfig";

	/** 类型:锁 */
	String TYPE_LOCK = "java.util.concurrent.locks.Lock";

	/** 类型:唯一属性值异常 */
	String TYPE_UNIQUE_EXCEPTION = "com.my9yu.common.ramcache.exception.UniqueFieldException";
}
