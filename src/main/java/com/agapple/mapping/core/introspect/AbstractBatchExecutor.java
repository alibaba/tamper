package com.agapple.mapping.core.introspect;

/**
 * 批量操作的抽象父类
 * 
 * @author jianghang 2011-6-2 下午04:57:47
 */
public abstract class AbstractBatchExecutor implements BatchExecutor {

    protected final Class<?> objectClass; // 操作object class

    public AbstractBatchExecutor(Class<?> theClass){
        this.objectClass = theClass;
    }

    public abstract boolean isAlive();

    public Class<?> getObjectClass() {
        return objectClass;
    }

}
