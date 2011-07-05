package com.agapple.mapping;

import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;

/**
 * Bean mapping处理的一些常用方法
 * 
 * @author jianghang 2011-5-27 下午12:27:12
 */
public class BeanMappingUtil {

    /**
     * 根据定义的bean-mapping配置进行对象属性的mapping拷贝
     * 
     * @param src
     * @param target
     */
    public static void mapping(Object src, Object target) throws BeanMappingException {
        BeanMapping mapping = BeanMapping.create(src.getClass(), target.getClass());
        mapping.mapping(src, target);
    }

    /**
     * 对象属性的拷贝，与BeanUtils , BeanCopier功能类似
     * 
     * @param src
     * @param target
     */
    public static void copy(Object src, Object target) throws BeanMappingException {
        BeanCopy copy = BeanCopy.create(src.getClass(), target.getClass());
        copy.copy(src, target);
    }

    /**
     *将bean的属性转化为Map对象
     * 
     * @param src
     * @return
     * @throws BeanMappingException
     */
    public static Map describe(Object src) throws BeanMappingException {
        BeanMap map = BeanMap.create(src.getClass());
        return map.describe(src);
    }

    /**
     * 将map的属性映射到bean对象
     * 
     * @param target
     * @param properties
     * @throws BeanMappingException
     */
    public static void populate(Object target, Map properties) throws BeanMappingException {
        BeanMap map = BeanMap.create(target.getClass());
        map.populate(target, properties);
    }
}
