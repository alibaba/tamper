package com.agapple.mapping;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingConfigRespository;
import com.agapple.mapping.object.inherit.FirstObject;
import com.agapple.mapping.object.inherit.TwoObject;
import com.agapple.mapping.process.script.ScriptHelper;
import com.agapple.mapping.script.CustomFunctionClass;

/**
 * 测试一下带继承关系的属性，映射到一平面上
 * 
 * @author jianghang 2011-6-22 下午03:42:51
 */
public class InheritObjectMappingTest extends TestCase {

    @Before
    public void setUp() {
        try {
            // 清空下repository下的数据
            TestUtils.setField(BeanMappingConfigHelper.getInstance(), "repository", new BeanMappingConfigRespository());

            ScriptHelper.getInstance().registerFunctionClass("customFunction", new CustomFunctionClass());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlat() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(TwoObject.class, HashMap.class);
                fields(srcField("name").locatorClass(FirstObject.class), targetField("name"));// name从父类中获取
                fields(srcField("firstValue").locatorClass(FirstObject.class), targetField("firstValue"));// 也从父类中获取
                fields(srcField("twoValue"), targetField("twoValue"));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        TwoObject src = new TwoObject("one", "two");
        src.setFirstValue(10);
        src.setTwoValue(BigInteger.TEN);

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get("name"), "two"); // 类的多台决定了，针对src调用getName()方法会取到子类的方法
        assertEquals(dest.get("firstValue"), Integer.valueOf(10));
        assertEquals(dest.get("twoValue"), BigInteger.TEN);
    }

    @Test
    public void testLevel() {
        BeanMappingBuilder firstBuilder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(FirstObject.class, HashMap.class);
                fields(srcField("firstValue"), targetField("firstValue"));
            }

        };

        BeanMappingBuilder twoBuilder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(TwoObject.class, HashMap.class);
                fields(srcField("twoValue"), targetField("twoValue"));
                fields(srcField("this", FirstObject.class), targetField("firstMap")).recursiveMapping(true).script(
                                                                                                                   "customFunction:newHashMap()");
            }

        };

        BeanMappingConfigHelper.getInstance().register(firstBuilder);
        BeanMapping mapping = new BeanMapping(twoBuilder);
        TwoObject src = new TwoObject("one", "two");
        src.setFirstValue(10);
        src.setTwoValue(BigInteger.TEN);

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get("twoValue"), BigInteger.TEN);
        assertEquals(((Map) dest.get("firstMap")).get("firstValue"), Integer.valueOf(10));
    }

    @Test
    public void testExchange() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(TwoObject.class, TwoObject.class);
                // firstValue的值设置给twoValue
                fields(srcField("firstValue").locatorClass(FirstObject.class),
                       targetField("twoValue").locatorClass(TwoObject.class));
                // twoValue的值设置给firstValue
                fields(srcField("twoValue").locatorClass(TwoObject.class),
                       targetField("firstValue").locatorClass(FirstObject.class));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        TwoObject src = new TwoObject("one", "two");
        src.setFirstValue(11);
        src.setTwoValue(BigInteger.valueOf(12));

        TwoObject dest = new TwoObject("one1", "two2");

        mapping.mapping(src, dest);
        assertEquals(dest.getName(), "two2"); // 类的多台决定了，针对src调用getName()方法会取到子类的方法
        assertEquals(dest.getFirstValue(), 12);
        assertEquals(dest.getTwoValue(), BigInteger.valueOf(11));
    }
}
