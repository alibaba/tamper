package com.agapple.mapping.core.introspect;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 处理下特殊符号的get操作，比如针对"this"返回当前对象的引用
 * 
 * @author jianghang 2011-6-27 下午07:58:25
 */
public class ThisSymbolGetExecutor extends AbstractExecutor implements GetExecutor {

    private boolean flag = false;

    public ThisSymbolGetExecutor(Introspector is, Class<?> clazz, String key){
        super(clazz, key);
        flag = discover(property);
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        if (flag) {
            return obj;
        }
        throw new BeanMappingException("error flag");
    }

    public static boolean discover(String key) {
        return "this".equalsIgnoreCase(key) ? true : false;
    }

    @Override
    public boolean isAlive() {
        return flag;
    }

}
