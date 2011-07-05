package com.agapple.mapping.script;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.agapple.mapping.process.script.ScriptContext;
import com.agapple.mapping.process.script.ScriptExecutor;
import com.agapple.mapping.process.script.jexl.JexlScriptContext;
import com.agapple.mapping.process.script.jexl.JexlScriptExecutor;

/**
 * @author jianghang 2011-5-24 下午04:43:01
 */
public class ScriptExecutorTest extends TestCase {

    public void testJexlScript() {
        ScriptExecutor executor = new JexlScriptExecutor();
        Map param = new HashMap();
        // bean数据
        ModelA a = new ModelA();
        ModelB b = new ModelB();
        a.setData(b);
        a.setName("modelA");
        b.setName("modelB");
        param.put("modelA", a);

        // data数据
        Map<String, Object> ds = new HashMap<String, Object>();
        ds.put("table$name", "ljh");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("oracle$datasource", ds);

        // convert数据
        Map<String, Object> converts = new HashMap<String, Object>();
        converts.put("convert", new Convert());

        param.putAll(data);
        param.putAll(converts);
        String expr = "modelA.data.name = convert.convert(oracle$datasource.table$name)";

        ScriptContext context = new JexlScriptContext(param);
        Object obj = executor.evaluate(context, expr);
        assertEquals(obj, "convert");
        assertEquals(a.getData().getName(), "convert");
    }

    public static class Convert {

        public Object convert(Object source) {
            return "convert";
        }
    }

    public static class ModelA {

        private ModelB data;

        private String name;

        public ModelB getData() {
            return data;
        }

        public void setData(ModelB data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class ModelB {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
