package com.agapple.mapping.process.script;

/**
 * script具体的执行器
 * 
 * @author jianghang 2011-5-20 下午03:42:10
 */
public interface ScriptExecutor {

    public void addFunction(String name, Object obj);

    public Object evaluate(ScriptContext context, String script);

}
