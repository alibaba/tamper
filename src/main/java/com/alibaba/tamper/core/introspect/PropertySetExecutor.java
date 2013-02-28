package com.alibaba.tamper.core.introspect;

import java.lang.reflect.Method;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * pojo bean属性的set操作
 * 
 * @author jianghang 2011-5-25 下午01:04:50
 */
public class PropertySetExecutor extends AbstractExecutor implements SetExecutor {

    private Method method;

    public PropertySetExecutor(Introspector is, Class<?> clazz, String identifier, Class arg){
        super(clazz, identifier);
        method = discover(is, clazz, identifier, arg);
    }

    @Override
    public Object invoke(Object key, Object value) throws BeanMappingException {
        Object[] pargs = { value };
        try {
            method.invoke(key, pargs);
            return value;
        } catch (Exception e) {
            throw new BeanMappingException(e);
        }
    }

    @Override
    public boolean isAlive() {
        return method != null;
    }

    public static Method discover(Introspector is, Class<?> clazz, String property, Class arg) {
        Method method = discoverSet(is, clazz, property, arg);
        if (method == null) {
            // 特殊处理 boolean isSuccessed生成的set/get方法为isSucessed(),setSuccessed()，需要过滤属性is前缀
            if (property.startsWith("is")) {
                property = property.substring("is".length());// 截取掉is前缀
                method = discoverSet(is, clazz, property, arg);
            }
        }
        return method;
    }

    public static Method discoverSet(Introspector is, Class<?> clazz, String property, Class arg) {
        String prefix = "set";
        final int start = prefix.length(); // "get" or "is" 情况处理

        StringBuilder sb = new StringBuilder(prefix);
        sb.append(property);
        char c = sb.charAt(start);
        sb.setCharAt(start, Character.toUpperCase(c));// 转化为 setXxx()
        if (arg == null) { // 参数为空，根据method name进行查找
            Method method = is.getJavaMethod(clazz, sb.toString()); // 注意，不加参数进行查找
            // lowercase nth char
            if (method == null) {
                sb.setCharAt(start, Character.toLowerCase(c));
                method = is.getJavaMethod(clazz, sb.toString()); // 注意，不加参数进行查找
            }

            return method;
        } else {// 明确指定参数，根据method name + param进行查找
            Method method = is.getJavaMethod(clazz, sb.toString(), arg);
            // lowercase nth char
            if (method == null) {
                sb.setCharAt(start, Character.toLowerCase(c));
                method = is.getJavaMethod(clazz, sb.toString(), arg);
            }

            return method;
        }
    }

    public Method getMethod() {
        return this.method;
    }

}
