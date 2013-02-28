package com.alibaba.tamper.process.script.jexl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.Interpreter;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.parser.JexlNode;

import com.alibaba.tamper.process.script.ScriptContext;
import com.alibaba.tamper.process.script.ScriptExecutor;
import com.alibaba.tamper.process.script.lifecyle.DisposableScript;
import com.alibaba.tamper.process.script.lifecyle.InitializingScript;

/**
 * Jexl的script实现
 * 
 * @author jianghang 2011-5-25 下午08:08:45
 */
public class JexlScriptExecutor implements ScriptExecutor {

    private static final int    DEFAULT_CACHE_SIZE = 1000;
    private ScriptJexlEngine    engine;
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
        engine = new ScriptJexlEngine();
        engine.setCache(cacheSize);
        engine.setSilent(true);
        engine.setFunctions(functions); // 注册functions
    }

    public ScriptContext genScriptContext(Map<String, Object> context) {
        return new JexlScriptContext(context);
    }

    public Object evaluate(Map<String, Object> context, String script) {
        return evaluate(genScriptContext(context), script);
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

    public void disposeFunctions() {
        engine.disposeUsedFunctions();
    }

}

/**
 * 定义为Jexl context
 * 
 * @author jianghang 2011-5-25 下午07:57:59
 */
class JexlScriptContext extends MapContext implements ScriptContext {

    public JexlScriptContext(Map map){
        super(map);
    }
}

/**
 * 扩展jexl的实现，提供{@linkplain InitializingScript},{@linkplain DisposableScript}的管理
 */
class ScriptJexlEngine extends JexlEngine {

    // 记录script执行过程中使用过的function
    private ThreadLocal<Set<Object>> usedFunctions = null;

    public ScriptJexlEngine(){
        super(null, null, null, null);
        usedFunctions = new ThreadLocal<Set<Object>>() {

            protected Set<Object> initialValue() {
                return new HashSet<Object>(); // 线程安全，不全同步
            }
        };
    }

    protected Interpreter createInterpreter(JexlContext context) {
        if (context == null) {
            context = EMPTY_CONTEXT;
        }
        return new ScriptInterpreter(this, context);
    }

    class ScriptInterpreter extends Interpreter {

        public ScriptInterpreter(JexlEngine jexl, JexlContext aContext){
            super(jexl, aContext);
        }

        protected Object resolveNamespace(String prefix, JexlNode node) {
            Object result = super.resolveNamespace(prefix, node);
            if (result != null) {
                boolean contains = usedFunctions.get().add(result);// 添加到使用记录中
                if (contains && InitializingScript.class.isAssignableFrom(result.getClass())) {// 第一次添加
                    ((InitializingScript) result).initial();// 回调
                }
            }
            return result;
        }
    }

    public void disposeUsedFunctions() {
        try {
            Set<Object> functions = usedFunctions.get();
            for (Object function : functions) {
                if (function != null && DisposableScript.class.isAssignableFrom(function.getClass())) {
                    ((DisposableScript) function).dispose();// 回调
                }
            }
        } finally {
            usedFunctions.set(new HashSet<Object>());// 清空
        }
    }

}
