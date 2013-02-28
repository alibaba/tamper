package com.alibaba.tamper.core.introspect;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * 处理下特殊符号的get操作，比如针对"null"返回null值
 * 
 * @author jianghang 2012-4-6 下午02:48:25
 */
public class NullSymbolGetExecutor extends AbstractExecutor implements GetExecutor {

    private boolean flag = false;

    public NullSymbolGetExecutor(Introspector is, Class<?> clazz, String key){
        super(clazz, key);
        flag = discover(property);
    }

    @Override
    public Object invoke(Object obj) throws BeanMappingException {
        if (flag) {
            return null;// 始终返回null值
        }
        throw new BeanMappingException("error flag");
    }

    public static boolean discover(String key) {
        return "null".equalsIgnoreCase(key) ? true : false;
    }

    @Override
    public boolean isAlive() {
        return flag;
    }

}
