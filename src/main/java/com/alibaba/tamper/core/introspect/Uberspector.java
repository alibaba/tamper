package com.alibaba.tamper.core.introspect;

import com.alibaba.tamper.core.config.BeanMappingEnvironment;

/**
 * 暴露给外部的内审接口操作，外部可通过Uberspector.getInstance()进行操作
 * 
 * @author jianghang 2011-5-25 下午01:18:18
 */
public class Uberspector {

    private static volatile Uberspect singleton;

    public static Uberspect getInstance() {
        if (singleton == null) {
            synchronized (Uberspector.class) {
                if (singleton == null) { // double check
                    singleton = BeanMappingEnvironment.getUberspect();
                }
            }
        }
        return singleton;
    }
}
