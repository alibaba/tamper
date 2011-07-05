package com.agapple.mapping.core.helper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.core.ReflectUtils;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 一些反射调用的helper类
 * 
 * @author jianghang 2011-5-26 下午08:35:43
 */
public class ReflectionHelper {

    private static String                   alias_int_name    = "int";
    private static String                   alias_short_name  = "short";
    private static String                   alias_long_name   = "long";
    private static String                   alias_char_name   = "char";
    private static String                   alias_double_name = "double";
    private static String                   alias_float_name  = "float";
    private static String                   alias_byte_name   = "byte";
    private static String                   alias_bool_name   = "boolean";
    private static String                   alias_string_name = "string";
    private static String                   alias_map_name    = "map";
    private static String                   alias_list_name   = "list";
    private static String                   alias_set_name    = "set";
    private final static Map<Class, Object> primitiveValueMap = new ConcurrentHashMap<Class, Object>(16);
    private final static Map<String, Class> aliasClassMap     = new ConcurrentHashMap<String, Class>(16); // class别名,比如primitive类型,string,list,map

    static {
        primitiveValueMap.put(Boolean.class, Boolean.FALSE);
        primitiveValueMap.put(Byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(Character.class, Character.valueOf((char) 0));
        primitiveValueMap.put(Short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(Double.class, Double.valueOf(0));
        primitiveValueMap.put(Float.class, Float.valueOf(0));
        primitiveValueMap.put(Integer.class, Integer.valueOf(0));
        primitiveValueMap.put(Long.class, Long.valueOf(0));
        primitiveValueMap.put(boolean.class, Boolean.FALSE);
        primitiveValueMap.put(byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(char.class, Character.valueOf((char) 0));
        primitiveValueMap.put(short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(double.class, Double.valueOf(0));
        primitiveValueMap.put(float.class, Float.valueOf(0));
        primitiveValueMap.put(int.class, Integer.valueOf(0));
        primitiveValueMap.put(long.class, Long.valueOf(0));

        aliasClassMap.put(alias_int_name, int.class);
        aliasClassMap.put(alias_short_name, short.class);
        aliasClassMap.put(alias_long_name, long.class);
        aliasClassMap.put(alias_double_name, double.class);
        aliasClassMap.put(alias_char_name, char.class);
        aliasClassMap.put(alias_float_name, float.class);
        aliasClassMap.put(alias_byte_name, byte.class);
        aliasClassMap.put(alias_bool_name, boolean.class);
        aliasClassMap.put(alias_string_name, String.class);
        aliasClassMap.put(alias_list_name, ArrayList.class);
        aliasClassMap.put(alias_map_name, HashMap.class);
        aliasClassMap.put(alias_set_name, HashSet.class);
    }

    public static void registerClassAlias(String alias, Class clazz) {
        aliasClassMap.put(alias, clazz);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class clazz) {
        BeanInfo beanInfo = null;
        PropertyDescriptor[] descriptors = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            return (new PropertyDescriptor[0]);
        }

        descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }

        return descriptors;
    }

    /**
     * 特殊处理，允许通过带参数的constructor创建对象
     * 
     * @param type
     * @return
     */
    public static Object newInstance(Class type) {
        Constructor _constructor = null;
        Object[] _constructorArgs = new Object[0];
        try {
            _constructor = type.getConstructor(new Class[] {});// 先尝试默认的空构造函数
        } catch (NoSuchMethodException e) {
            // ignore
        }

        if (_constructor == null) {// 没有默认的构造函数，尝试别的带参数的函数
            Constructor[] constructors = type.getConstructors();
            if (constructors.length == 0) {
                throw new UnsupportedOperationException("Class[" + type.getName() + "] has no public constructors");
            }
            _constructor = constructors[0];// 默认取第一个参数
            Class[] params = _constructor.getParameterTypes();
            _constructorArgs = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                _constructorArgs[i] = getDefaultValue(params[i]);
            }
        }

        return ReflectUtils.newInstance(_constructor, _constructorArgs);
    }

    /**
     * 根据class类型返回默认值值
     * 
     * @param cl
     * @return
     */
    public static Object getDefaultValue(Class cl) {
        if (cl.isArray()) {// 处理数组
            return Array.newInstance(cl.getComponentType(), 0);
        } else if (cl.isPrimitive() || primitiveValueMap.containsKey(cl)) { // 处理原型
            return primitiveValueMap.get(cl);
        } else {
            return newInstance(cl);
            // return null;
        }
    }

    /**
     * 支持class alias别名
     * 
     * @param className
     * @return
     */
    public static Class forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            if (aliasClassMap.containsKey(className)) {
                return aliasClassMap.get(className);
            }

            throw new BeanMappingException("forName class[" + className + "] is error!", e);
        }
    }

    /**
     * 从指定的ClassLoader中装载
     */
    public static Class forName(String className, ClassLoader loader) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            if (aliasClassMap.containsKey(className)) {
                return aliasClassMap.get(className);
            }

            throw new BeanMappingException("forName class[" + className + "] is error!", e);
        }
    }
}
