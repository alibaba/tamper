package com.agapple.mapping.core.introspect;

import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于map的属性get动作处理
 * 
 * @author jianghang 2011-5-25 上午11:32:33
 */
public class MapGetExecutor extends AbstractExecutor implements GetExecutor {

    private boolean flag = false;

    public MapGetExecutor(Introspector is, Class<?> clazz, String key){
        super(clazz, key);
        flag = discover(clazz);
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        final Map<Object, ?> map = (Map<Object, ?>) obj;
        return map.get(property);
    }

    @Override
    public boolean isAlive() {
        return flag;
    }

    public static boolean discover(Class<?> clazz) {
        return (Map.class.isAssignableFrom(clazz)) ? true : false;
    }

}
