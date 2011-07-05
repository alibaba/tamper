package com.agapple.mapping.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;

import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.process.convetor.StringAndCommonConvertor.StringToCommon;

/**
 * @author jianghang 2011-6-22 上午10:57:00
 */
public class BeanMappingBuilderTest extends TestCase {

    public void testSimpleBuilder() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior(true, false, false, true);// 设置行为
                mapping(HashMap.class, HashMap.class).debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(
                                                                                                                                 true);
                fields(srcField("one", String.class), targetField("oneOther", String.class)).debug(true).mappingEmptyStrings(
                                                                                                                             false).mappingNullValue(
                                                                                                                                                     false).trimStrings(
                                                                                                                                                                        true);
                fields(srcField("two").locatorClass(ArrayList.class).componentClasses(String.class),
                       targetField("twoOther"));
            }

        };

        BeanMappingObject object = builder.get();
        assertNotNull(object);
        assertEquals(object.getBeanFields().size(), 2);

    }

    public void testBehavior() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping(HashMap.class, HashMap.class).debug(false).mappingEmptyStrings(true);
                fields(srcField("one"), targetField("oneOther")).mappingEmptyStrings(false).trimStrings(false);
            }

        };

        BeanMappingObject object = builder.get();
        assertNotNull(object);
        // 验证bean behavior
        BeanMappingBehavior objectBehavior = object.getBehavior();
        assertEquals(objectBehavior.isDebug(), false); // 覆盖
        assertEquals(objectBehavior.isMappingEmptyStrings(), true);// 覆盖
        assertEquals(objectBehavior.isMappingNullValue(), false); // 继承
        assertEquals(objectBehavior.isTrimStrings(), true); // 继承
        // 验证field behavior
        BeanMappingBehavior fieldBehavior = object.getBeanFields().get(0).getBehavior();
        assertEquals(fieldBehavior.isDebug(), false); // 继承
        assertEquals(fieldBehavior.isMappingEmptyStrings(), false);// 覆盖
        assertEquals(fieldBehavior.isMappingNullValue(), false); // 继承
        assertEquals(fieldBehavior.isTrimStrings(), false); // 覆盖
    }

    public void testConfig() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                mapping(HashMap.class, HashMap.class).batch(false).reversable(true).keys("src", "target");
                fields(srcField("one"), targetField("oneOther")).convertor("convertor").defaultValue("ljh");
                fields(srcField("two").clazz(String.class), targetField("twoOther")).script("1+2").convertor(
                                                                                                             StringToCommon.class);
                fields(srcField("three").clazz(ArrayList.class), targetField("threeOther").clazz(HashSet.class)).recursiveMapping(
                                                                                                                                  true);
            }

        };

        BeanMappingObject object = builder.get();
        assertNotNull(object);
        assertEquals(object.getBeanFields().size(), 3);
        assertEquals(object.isBatch(), false);
        assertEquals(object.isReversable(), true);
        assertEquals(object.getSrcKey(), "src");
        assertEquals(object.getTargetKey(), "target");
        assertEquals(object.getSrcClass(), HashMap.class);
        assertEquals(object.getTargetClass(), HashMap.class);

        BeanMappingField one = object.getBeanFields().get(0);
        assertEquals(one.getConvertor(), "convertor");
        assertEquals(one.getDefaultValue(), "ljh");
        assertEquals(one.getSrcField().getName(), "one");
        assertEquals(one.getTargetField().getName(), "oneOther");

        BeanMappingField two = object.getBeanFields().get(1);
        assertEquals(two.getConvertorClass(), StringToCommon.class);
        assertEquals(two.getScript(), "1+2");
        assertEquals(two.getSrcField().getName(), "two");
        assertEquals(two.getSrcField().getClazz(), String.class);
        assertEquals(two.getTargetField().getName(), "twoOther");

        BeanMappingField three = object.getBeanFields().get(2);
        assertEquals(three.getSrcField().getName(), "three");
        assertEquals(three.getSrcField().getClazz(), ArrayList.class);
        assertEquals(three.getTargetField().getName(), "threeOther");
        assertEquals(three.getTargetField().getClazz(), HashSet.class);
        assertEquals(three.isMapping(), true);

    }

    public void testRegister() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                mapping(HashMap.class, HashMap.class);
                fields(srcField("one"), targetField("oneOther"));
            }

        };

        BeanMappingConfigHelper.getInstance().register(builder); // 进行注册

        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(HashMap.class,
                                                                                              HashMap.class);

        assertNotNull(object);

    }
}
