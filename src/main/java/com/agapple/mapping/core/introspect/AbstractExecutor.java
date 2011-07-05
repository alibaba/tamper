package com.agapple.mapping.core.introspect;

/**
 * @author jianghang 2011-5-25 上午11:21:47
 */
public abstract class AbstractExecutor {

    protected final Class<?> objectClass; // 操作object class
    protected final String   property;   // 操作object class

    protected AbstractExecutor(Class<?> theClass, String property){
        this.objectClass = theClass;
        this.property = property;

    }

    public abstract boolean isAlive();

    public Class<?> getTargetClass() {
        return objectClass;
    }

    public String getTargetProperty() {
        return property;
    }

}
