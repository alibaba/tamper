package com.alibaba.tamper.script;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;

import com.alibaba.tamper.TestUtils;
import com.alibaba.tamper.process.script.ScriptExecutor;
import com.alibaba.tamper.process.script.ScriptHelper;
import com.alibaba.tamper.process.script.jexl.JexlScriptExecutor;

/**
 * @author jianghang 2011-5-24 下午04:43:01
 */
public class ScriptExecutorTest extends TestCase {

    @Before
    public void setUp() {
        try {
            // 清空下repository下的数据
            TestUtils.setField(ScriptHelper.getInstance(), "executor", null);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void testJexlScript() {
        ScriptExecutor executor = new JexlScriptExecutor();
        doTest(executor);
    }

    public void test_system() {
        System.setProperty("BeanMapping.Script.Executor", "com.alibaba.tamper.process.script.jexl.JexlScriptExecutor");
        doTest(ScriptHelper.getInstance().getScriptExecutor());
        System.setProperty("BeanMapping.Script.Executor", "");
    }

    public void test_service() {
        System.setProperty("BeanMapping.Script.Executor", "");
        doTest(ScriptHelper.getInstance().getScriptExecutor());
    }

    private void doTest(ScriptExecutor executor) {
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

        Object obj = executor.evaluate(param, expr);
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
