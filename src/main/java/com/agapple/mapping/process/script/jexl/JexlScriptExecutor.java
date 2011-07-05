package com.agapple.mapping.process.script.jexl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.agapple.mapping.process.script.ScriptContext;
import com.agapple.mapping.process.script.ScriptExecutor;

/**
 * Jexl的script实现
 * 
 * @author jianghang 2011-5-25 下午08:08:45
 */
public class JexlScriptExecutor implements ScriptExecutor {

    private static final int    DEFAULT_CACHE_SIZE = 1000;
    private JexlEngine          engine;
    private Map<String, Object> functions;
    private int                 cacheSize          = DEFAULT_CACHE_SIZE;

    public JexlScriptExecutor(){
        initialize();
    }

    /**
     * 初始化function
     */
    public void initialize() {
        if (cacheSize <= 0) {// 不考虑cacheSize为0的情况，强制使用LRU cache机制
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        functions = new HashMap<String, Object>();
        engine = new JexlEngine();
        engine.setCache(cacheSize);
        engine.setFunctions(functions); // 注册functions
    }

    /**
     * <pre>
     * 1. 接受JexlScriptContext上下文
     * 2. script针对对应name下的script脚本
     * </pre>
     */
    public Object evaluate(ScriptContext context, String script) {
        Expression expr = engine.createExpression(script);
        return expr.evaluate((JexlContext) context);
    }

    // ============================ setter / getter ============================

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public void addFunction(String name, Object obj) {
        this.functions.put(name, obj);
    }
}
