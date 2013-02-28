package com.alibaba.tamper.process.script;

import java.util.Map;

/**
 * script具体的执行器
 * 
 * @author jianghang 2011-5-20 下午03:42:10
 */
public interface ScriptExecutor {

    /**
     * Generate a proper execute context for the executor.
     */
    public ScriptContext genScriptContext(Map<String, Object> context);

    /**
     * 接受Map context上下文，执行script
     */
    public Object evaluate(Map<String, Object> context, String script);

    /**
     * 接受ScriptContext上下文，执行script
     */
    public Object evaluate(ScriptContext context, String script);

    public void disposeFunctions();

    public void addFunction(String name, Object func);

}
