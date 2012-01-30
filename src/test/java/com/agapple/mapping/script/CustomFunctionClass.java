package com.agapple.mapping.script;

import java.util.HashMap;
import java.util.Map;

import com.agapple.mapping.process.script.lifecyle.DisposableScript;
import com.agapple.mapping.process.script.lifecyle.InitializingScript;

/**
 * 自定义
 * 
 * @author jianghang 2011-6-28 下午04:40:57
 */
public class CustomFunctionClass implements InitializingScript, DisposableScript {

    private int initial = 0;

    public int sum(int a, Integer b) {
        return a + b;
    }

    public Map newHashMap() {
        return new HashMap();
    }

    public void initial() {
        initial += 1;
    }

    public void dispose() {
        initial += 1;
    }

    public int getInitial() {
        return initial;
    }

}
