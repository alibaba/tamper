package com.alibaba.tamper.core.introspect;

import java.lang.reflect.Method;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * bean 属性的获取
 * 
 * @author jianghang 2011-5-25 下午12:37:50
 */
public class PropertyGetExecutor extends AbstractExecutor implements GetExecutor {

    private Method method;

    public PropertyGetExecutor(Introspector is, Class<?> clazz, String identifier){
        super(clazz, identifier);
        method = discover(is, clazz, identifier);
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        try {
            return (method == null ? null : method.invoke(obj, (Object[]) null));
        } catch (Exception e) {
            throw new BeanMappingException(e);
        }
    }

    @Override
    public boolean isAlive() {
        return method != null;
    }

    public static Method discover(Introspector is, Class<?> clazz, String property) {
        Method method = discoverGet(is, "get", clazz, property);
        if (method == null) {
            // 尝试一下"is"方法
            method = discoverGet(is, "is", clazz, property);
            if (method == null) {
                // 特殊处理 boolean isSuccessed生成的set/get方法为isSucessed(),setSuccessed()，需要过滤属性is前缀
                if (property.startsWith("is")) {
                    property = property.substring("is".length());// 截取掉is前缀
                    method = discoverGet(is, "is", clazz, property);
                }
            }
        }

        return method;
    }

    public static Method discoverGet(Introspector is, String which, Class<?> clazz, String property) {
        Method method = null;
        final int start = which.length(); // "get" or "is" 情况处理
        StringBuilder sb = new StringBuilder(which);
        sb.append(property);
        char c = sb.charAt(start);
        sb.setCharAt(start, Character.toUpperCase(c));// 转化为 getXxx()
        method = is.getJavaMethod(clazz, sb.toString());
        // lowercase nth char
        if (method == null) {
            sb.setCharAt(start, Character.toLowerCase(c));
            method = is.getJavaMethod(clazz, sb.toString());
        }
        return method;
    }

    public Method getMethod() {
        return this.method;
    }

}
