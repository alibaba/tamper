package com.alibaba.tamper.core.introspect;

/**
 * 暴露给外部的内审接口操作
 * 
 * @author jianghang 2011-5-25 下午01:18:18
 */
public interface Uberspect {

    /**
     * 根据对应的信息返回executor
     */
    public BatchExecutor getBatchExecutor(Class locatorClass, String[] identifier, Class[] args);

    /**
     * 根据对应的信息返回executor
     */
    public GetExecutor getGetExecutor(Class locatorClass, Object identifier);

    /**
     * 根据对应的信息返回executor
     */
    public SetExecutor getSetExecutor(Class locatorClass, Object identifier, Class arg);

    /**
     * 根据executor返回对应的参数类型
     */
    public Class getGetClass(GetExecutor getExecutor, Class srcRefClass, Class getResultClass);

    /**
     * 根据executor返回对应的参数类型
     */
    public Class getSetClass(SetExecutor setExecutor, Class srcRefClass, Class getResultClass);
}
