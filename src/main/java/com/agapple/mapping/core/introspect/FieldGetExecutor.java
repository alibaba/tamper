package com.agapple.mapping.core.introspect;

import java.lang.reflect.Field;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于field属性的直接操作
 * 
 * @author jianghang 2011-6-27 下午08:05:06
 */
public class FieldGetExecutor extends AbstractExecutor implements GetExecutor {

    private Field field;

    protected FieldGetExecutor(Introspector is, Class<?> clazz, String identifier){
        super(clazz, identifier);
        field = discover(is, clazz, identifier);
    }

    @Override
    public boolean isAlive() {
        return field != null;
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        try {
            return field.get(obj);
        } catch (Exception e) {
            throw new BeanMappingException("field invoke error!", e);
        }
    }

    public static Field discover(Introspector is, Class<?> clazz, String property) {
        Field field = is.getField(clazz, property);// 直接通过class获取属性
        if (field != null) {
            field.setAccessible(true); // 设置为true
            return field;
        } else {
            return null;
        }
    }

    public Field getField() {
        return field;
    }
}
