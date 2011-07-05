package com.agapple.mapping.script;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义
 * 
 * @author jianghang 2011-6-28 下午04:40:57
 */
public class CustomFunctionClass {

    public int sum(int a, Integer b) {
        return a + b;
    }

    public Map newHashMap() {
        return new HashMap();
    }
}
