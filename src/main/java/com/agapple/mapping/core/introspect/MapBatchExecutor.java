/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.core.introspect;

import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;

/**
 * @author jianghang 2011-6-2 下午04:47:52
 */
public class MapBatchExecutor extends AbstractBatchExecutor {

    private static Object MAP      = new Object();
    private Object        executor = null;
    private Object[]      properties;

    public MapBatchExecutor(Introspector is, Class<?> clazz, Object[] fields){
        super(clazz);
        this.properties = fields;
        executor = discover(clazz);
    }

    public Object[] gets(Object obj) throws BeanMappingException {
        final Map<Object, Object> map = (Map<Object, Object>) obj;
        Object[] values = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            values[i] = map.get(properties[i]);

        }
        return values;
    }

    public void sets(Object obj, Object[] values) throws BeanMappingException {
        final Map<Object, Object> map = (Map<Object, Object>) obj;
        for (int i = 0; i < properties.length; i++) {
            map.put(properties[i], values[i]);

        }
    }

    @Override
    public boolean isAlive() {
        return executor != null;
    }

    public static Object discover(Class<?> clazz) {
        return (Map.class.isAssignableFrom(clazz)) ? MAP : null;
    }

}
