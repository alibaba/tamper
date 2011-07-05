package com.agapple.mapping.core.introspect;

import net.sf.cglib.reflect.FastMethod;

import com.agapple.mapping.core.BeanMappingException;

/**
 * bean 属性的获取
 * 
 * @author jianghang 2011-5-25 下午12:37:50
 */
public class PropertyGetExecutor extends AbstractExecutor implements GetExecutor {

    private FastMethod method;

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

    public static FastMethod discover(Introspector is, Class<?> clazz, String property) {
        FastMethod method = discoverGet(is, "get", clazz, property);
        if (method == null) {
            // 尝试一下"is"方法
            method = discoverGet(is, "is", clazz, property);
        }
        return method;
    }

    public static FastMethod discoverGet(Introspector is, String which, Class<?> clazz, String property) {
        FastMethod method = null;
        final int start = which.length(); // "get" or "is" 情况处理
        StringBuilder sb = new StringBuilder(which);
        sb.append(property);
        char c = sb.charAt(start);
        sb.setCharAt(start, Character.toUpperCase(c));// 转化为 getXxx()
        method = is.getFastMethod(clazz, sb.toString());
        // lowercase nth char
        if (method == null) {
            sb.setCharAt(start, Character.toLowerCase(c));
            method = is.getFastMethod(clazz, sb.toString());
        }
        return method;
    }

    public FastMethod getMethod() {
        return this.method;
    }

}
