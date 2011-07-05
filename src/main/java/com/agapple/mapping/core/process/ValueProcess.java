package com.agapple.mapping.core.process;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 数据处理接口，允许在get Executor执行之前处理下value。
 * 
 * @author jianghang 2011-5-25 下午01:39:25
 */
public interface ValueProcess {

    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException;

}
