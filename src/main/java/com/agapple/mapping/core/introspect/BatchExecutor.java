package com.agapple.mapping.core.introspect;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 针对一个obj对象，批量处理的get/set操作
 * 
 * @author jianghang 2011-5-31 下午08:41:14
 */
public interface BatchExecutor {

    Object[] gets(Object obj) throws BeanMappingException;

    void sets(Object obj, Object[] values) throws BeanMappingException;

}
