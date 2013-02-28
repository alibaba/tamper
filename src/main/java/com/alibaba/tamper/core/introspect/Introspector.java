package com.alibaba.tamper.core.introspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * 一些反射调用的工具类
 * 
 * @author jianghang 2011-5-25 下午12:40:17
 */
public class Introspector {

    private Map<String, FastClass>  fastClassCache  = new ConcurrentHashMap<String, FastClass>();
    private Map<String, FastMethod> fastMethodCache = new ConcurrentHashMap<String, FastMethod>();
    private Map<String, Class>      classCache      = new ConcurrentHashMap<String, Class>();
    private Map<String, Method>     methodCache     = new ConcurrentHashMap<String, Method>();
    private Map<String, Method[]>   allMethodCache  = new ConcurrentHashMap<String, Method[]>();
    private Map<String, Field>      fieldCache      = new ConcurrentHashMap<String, Field>();

    public FastMethod getFastMethod(Class<?> clazz, String methodName) {
        return getFastMethod(clazz, methodName, new Class[] {});
    }

    /**
     * 根据methodName + paramter参数进行获取处理
     * 
     * @param clazz
     * @param methodName
     * @param parameter
     * @return
     */
    public FastMethod getFastMethod(Class<?> clazz, String methodName, Object... parameter) {
        if (parameter == null) {
            return getFastMethod(clazz, methodName);
        }

        Class[] types = new Class[parameter.length];
        for (int i = 0; i < parameter.length; i++) {
            types[i] = parameter.getClass();
        }

        return getFastMethod(clazz, methodName, types);
    }

    /**
     * 根据信息查询FastMethod，已经有cache实现。
     * 
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public FastMethod getFastMethod(Class<?> clazz, String methodName, Class... parameterTypes) {
        String clazzName = clazz.getName();
        String methodKey = buildMethodKey(clazzName, methodName, parameterTypes);

        FastMethod method = fastMethodCache.get(methodKey);
        if (null == method) {
            getFastClass(clazz);// 分析一次clazz,这时会跟新fastMethodCache
            return fastMethodCache.get(methodKey);
        } else {
            return method;
        }
    }

    /**
     * 根据信息查询Method，已经有cache实现。
     * 
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public Method getJavaMethod(Class<?> clazz, String methodName, Class... parameterTypes) {
        String clazzName = clazz.getName();
        String methodKey = buildMethodKey(clazzName, methodName, parameterTypes);

        Method method = methodCache.get(methodKey);
        if (null == method) {
            getFastClass(clazz);// 分析一次clazz,这时会跟新methodCache
            return methodCache.get(methodKey);
        } else {
            return method;
        }
    }

    /**
     * 根据methodName进行获取处理，<strong>这里不区分具体的参数<strong>，主要用户进行converter之前，需要先获取目标的method参数对象
     * 
     * @param clazz
     * @param methodName
     * @param parameter
     * @return
     */
    public Method getJavaMethod(Class<?> clazz, String methodName) {
        String className = clazz.getName();
        Method[] methods = allMethodCache.get(className);
        if (methods == null) {
            initJavaClassCache(clazz);// 分析一次clazz,这时会跟新allMethodCache
            methods = allMethodCache.get(className);
        }
        if (methods == null) {
            return null;
        }

        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    public Method[] getJavaMethods(Class<?> clazz) {
        Method[] methods = allMethodCache.get(clazz.getName());
        if (methods == null) {
            initJavaClassCache(clazz);// 分析一次clazz,这时会跟新allMethodCache
            return allMethodCache.get(clazz.getName());
        } else {
            return methods;
        }
    }

    public Field getField(Class<?> clazz, String fieldName) {
        Field field = fieldCache.get(fieldName);
        if (field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // ignore
            } catch (Exception e) {
                throw new BeanMappingException(e);
            }
            if (field != null) {
                fieldCache.put(fieldName, field);
            }
        }

        return field;
    }

    /**
     * 获取对应的fastClass
     * 
     * @param clazz
     * @return
     */
    public FastClass getFastClass(Class<?> clazz) {
        String clazzName = clazz.getName();
        FastClass fc = fastClassCache.get(clazzName);
        if (null == fc) {
            synchronized (clazz) { // 进行锁控制
                fc = fastClassCache.get(clazzName); // double check
                if (fc != null) {
                    return fc;
                }
                // 更新methodCache和fastMethodCache
                fc = FastClass.create(clazz);
                String className = clazz.getName();
                Method[] methods = clazz.getMethods();
                for (Method m : methods) {
                    String key = buildMethodKey(className, m.getName(), m.getParameterTypes());
                    methodCache.put(key, m);
                    fastMethodCache.put(key, fc.getMethod(m));
                }
                allMethodCache.put(clazzName, methods);
                fastClassCache.put(clazzName, fc);
            }
            // 再取一次cache
            return fastClassCache.get(clazzName);
        } else {
            return fc;
        }
    }

    // ============================ helper mthod =========================

    private void initJavaClassCache(Class clazz) {
        String clazzName = clazz.getName();
        Class cl = classCache.get(clazzName);
        if (null == cl) {
            synchronized (clazz) { // 进行锁控制
                cl = classCache.get(clazzName); // double check
                if (cl != null) {
                    return;
                }
                Method[] methods = clazz.getMethods();
                for (Method m : methods) {
                    String key = buildMethodKey(clazzName, m.getName(), m.getParameterTypes());
                    methodCache.put(key, m);
                }
                allMethodCache.put(clazzName, methods);
            }
        }
    }

    /**
     * @param methodName
     * @param clazzName
     * @return
     */
    private String buildMethodKey(String clazzName, String methodName, Class[] parameters) {
        String paramName = "";
        for (Class p : parameters) {
            paramName += p.getName() + "#";
        }
        return clazzName + "#" + methodName + "#" + paramName;
    }

}
