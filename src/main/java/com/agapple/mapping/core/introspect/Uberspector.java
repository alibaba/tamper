package com.agapple.mapping.core.introspect;

import java.lang.ref.SoftReference;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 暴露给外部的内审接口操作，外部可通过Uberspector.getInstance()进行操作
 * 
 * @author jianghang 2011-5-25 下午01:18:18
 */
public class Uberspector {

    private static volatile Uberspector singleton;
    private SoftReference<Introspector> introspector;

    public Uberspector(){
        introspector = new SoftReference(new Introspector());
    }

    public Uberspector(Introspector introspector){
        // 允许自定义传入introspector
        this.introspector = new SoftReference(introspector);
    }

    public static Uberspector getInstance() {
        if (singleton == null) {
            synchronized (Uberspector.class) {
                if (singleton == null) { // double check
                    singleton = new Uberspector();
                }
            }
        }
        return singleton;
    }

    public BatchExecutor getBatchExecutor(Class locatorClass, String[] identifier, Class[] args) {
        final Class<?> clazz = locatorClass;
        // 尝试一下map处理
        MapBatchExecutor mBatchExecutor = new MapBatchExecutor(getIntrospector(), clazz, identifier);
        if (mBatchExecutor.isAlive()) {
            return mBatchExecutor;
        }

        // 尝试一下bean处理
        if (identifier != null) {
            PropertyBatchExecutor pBatchExecutor = new PropertyBatchExecutor(getIntrospector(), clazz, identifier, args);
            if (pBatchExecutor.isAlive()) {
                return pBatchExecutor;
            }
        }

        // 如果没有匹配到executor，则返回null，不做batch优化
        return null;
    }

    public GetExecutor getGetExecutor(Class locatorClass, Object identifier) {
        final Class<?> clazz = locatorClass;
        final String property = (identifier == null ? null : identifier.toString());

        // 尝试一下bean处理
        if (property != null) {
            PropertyGetExecutor pExecutor = new PropertyGetExecutor(getIntrospector(), clazz, property);
            if (pExecutor.isAlive()) {
                return pExecutor;
            }
        }

        // 尝试一下map处理
        MapGetExecutor mExecutor = new MapGetExecutor(getIntrospector(), clazz, property);
        if (mExecutor.isAlive()) {
            return mExecutor;
        }

        // 尝试一下直接field操作
        FieldGetExecutor fExecutor = new FieldGetExecutor(getIntrospector(), clazz, property);
        if (fExecutor.isAlive()) {
            return fExecutor;
        }

        // 尝试一下特殊符号，比如"this"
        ThisSymbolGetExecutor sExecutor = new ThisSymbolGetExecutor(getIntrospector(), clazz, property);
        if (sExecutor.isAlive()) {
            return sExecutor;
        }

        throw new BeanMappingException("can not found GetExecutor for Class[" + clazz.getName() + "] , identifier["
                                       + identifier + "]");
    }

    public SetExecutor getSetExecutor(Class locatorClass, Object identifier, Class arg) {
        final Class<?> clazz = locatorClass;
        final String property = (identifier == null ? null : identifier.toString());

        // 尝试一下bean处理
        if (property != null) {
            PropertySetExecutor pExecutor = new PropertySetExecutor(getIntrospector(), clazz, property, arg);
            if (pExecutor.isAlive()) {
                return pExecutor;
            }
        }

        // 尝试一下map处理
        MapSetExecutor mExecutor = new MapSetExecutor(getIntrospector(), clazz, property, arg);
        if (mExecutor.isAlive()) {
            return mExecutor;
        }

        // 尝试一下直接field操作
        FieldSetExecutor fExecutor = new FieldSetExecutor(getIntrospector(), clazz, property, arg);
        if (fExecutor.isAlive()) {
            return fExecutor;
        }

        throw new BeanMappingException("can not found SetExecutor for Class[" + clazz.getName() + "] , identifier["
                                       + identifier + "]");
    }

    /**
     * 根据executor返回对应的参数类型
     */
    public Class getGetClass(GetExecutor getExecutor, Class srcRefClass, Class getResultClass) {
        // 设置为自动提取的targetClasss
        if (getExecutor instanceof MapGetExecutor) {
            if (getResultClass != null) {
                return getResultClass;// 优先设置为getResult的class对象
            }
        } else if (getExecutor instanceof PropertyGetExecutor) {
            return ((PropertyGetExecutor) getExecutor).getMethod().getReturnType(); // 获取getExecutor方法的返回结果类型
        } else if (getExecutor instanceof FieldGetExecutor) {
            return ((FieldGetExecutor) getExecutor).getField().getType(); // 获取属性的类型
        } else if (getExecutor instanceof ThisSymbolGetExecutor) {
            return srcRefClass; // 返回对应src的类型
        }

        return null;
        // throw new BeanMappingException("unknow GetExecutor");
    }

    /**
     * 根据executor返回对应的参数类型
     */
    public Class getSetClass(SetExecutor setExecutor, Class srcRefClass, Class getResultClass) {
        if (setExecutor instanceof MapSetExecutor) {
            if (getResultClass != null) {
                return getResultClass;// 优先设置为getResult的class对象
            }
        } else if (setExecutor instanceof PropertySetExecutor) {
            return getTargetClass((PropertySetExecutor) setExecutor);
        } else if (setExecutor instanceof FieldSetExecutor) {
            return ((FieldSetExecutor) setExecutor).getField().getType();
        }

        return null;
        // throw new BeanMappingException("unknow SetExecutor");
    }

    /**
     * 根据{@linkplain SetExecutor}获取对应的目标targetClass
     */
    private Class getTargetClass(PropertySetExecutor setExecutor) {
        Class[] params = setExecutor.getMethod().getParameterTypes();
        if (params == null || params.length != 1) {
            throw new BeanMappingException("illegal set method[" + setExecutor.getMethod().getName()
                                           + "] for ParameterType");
        }
        return params[0];
    }

    // ================= setter / getter =================

    public Introspector getIntrospector() {
        Introspector in = introspector.get();
        if (in == null) {
            introspector = new SoftReference<Introspector>(new Introspector());// 重新创建一个
            return getIntrospector();
        } else {
            return in;
        }
    }

    public void setIntrospector(Introspector introspector) {
        this.introspector = new SoftReference<Introspector>(introspector);
    }

}
