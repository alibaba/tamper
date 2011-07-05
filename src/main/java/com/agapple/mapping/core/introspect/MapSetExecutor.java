package com.agapple.mapping.core.introspect;

import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于map的set操作
 * 
 * @author jianghang 2011-5-25 下午01:02:03
 */
public class MapSetExecutor extends AbstractExecutor implements SetExecutor {

    private boolean flag = false;

    public MapSetExecutor(Introspector is, Class<?> clazz, String key, Class arg){
        super(clazz, key);
        flag = discover(clazz);
    }

    @Override
    public Object invoke(Object obj, Object value) throws BeanMappingException {
        final Map map = ((Map) obj);
        map.put(property, value);
        return value;
    }

    @Override
    public boolean isAlive() {
        return flag;
    }

    public static boolean discover(Class<?> clazz) {
        return (Map.class.isAssignableFrom(clazz)) ? true : false;
    }

}
