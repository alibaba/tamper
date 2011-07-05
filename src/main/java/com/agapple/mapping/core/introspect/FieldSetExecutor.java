package com.agapple.mapping.core.introspect;

import java.lang.reflect.Field;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于field属性的直接操作
 * 
 * @author jianghang 2011-6-27 下午08:05:41
 */
public class FieldSetExecutor extends AbstractExecutor implements SetExecutor {

    private Field field;

    protected FieldSetExecutor(Introspector is, Class<?> clazz, String identifier, Class arg){
        super(clazz, identifier);
        field = discover(is, clazz, identifier, arg);
    }

    @Override
    public boolean isAlive() {
        return field != null;
    }

    @Override
    public Object invoke(Object obj, Object value) throws BeanMappingException {
        try {
            field.set(obj, value);
            return value;
        } catch (Exception e) {
            throw new BeanMappingException("field invoke error!", e);
        }
    }

    public static Field discover(Introspector is, Class<?> clazz, String property, Class arg) {
        Field field = is.getField(clazz, property);// 直接通过class获取属性
        if (field != null) {
            if (field.getType() != arg) { // 判断类型是否匹配
                return null;
            }

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
