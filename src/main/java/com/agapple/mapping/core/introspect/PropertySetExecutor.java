package com.agapple.mapping.core.introspect;

import java.lang.reflect.Method;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.agapple.mapping.core.BeanMappingException;

/**
 * pojo bean属性的set操作
 * 
 * @author jianghang 2011-5-25 下午01:04:50
 */
public class PropertySetExecutor extends AbstractExecutor implements SetExecutor {

    private FastMethod method;

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

    public static FastMethod discover(Introspector is, Class<?> clazz, String property, Class arg) {
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

            if (method != null) {
                FastClass fc = is.getFastClass(clazz);
                return fc.getMethod(method);
            } else {
                return null;
            }
        } else {// 明确指定参数，根据method name + param进行查找
            FastMethod fastMethod = is.getFastMethod(clazz, sb.toString(), arg);
            // lowercase nth char
            if (fastMethod == null) {
                sb.setCharAt(start, Character.toLowerCase(c));
                fastMethod = is.getFastMethod(clazz, sb.toString(), arg);
            }

            return fastMethod;
        }

    }

    public FastMethod getMethod() {
        return this.method;
    }

}
