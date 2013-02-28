package com.alibaba.mapping;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.mapping.core.builder.BeanMappingBuilder;
import com.alibaba.mapping.core.config.BeanMappingConfigHelper;
import com.alibaba.mapping.core.config.BeanMappingConfigRespository;

/**
 * 基于API构造mapping进行测试，更全面的测试
 * 
 * @author jianghang 2011-6-22 下午01:51:19
 */
public class BeanMappingNestAndNameTest extends TestCase {

    private static final String ONE         = "one";
    private static final String TWO         = "two";
    private static final String THREE       = "three";
    private static final String ONE_OTHER   = "oneOther";
    private static final String TWO_OTHER   = "twoOther";
    private static final String THREE_OTHER = "threeOther";

    @Before
    public void setUp() {
        try {
            // 清空下repository下的数据
            TestUtils.setField(BeanMappingConfigHelper.getInstance(), "repository", new BeanMappingConfigRespository());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testName() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping("testName", HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, String.class));
                fields(srcField(TWO, String.class), targetField(TWO_OTHER, String.class));
                fields(srcField(THREE, String.class), targetField(THREE_OTHER, String.class));
            }

        };

        BeanMappingConfigHelper.getInstance().register(builder);// 注册
        BeanMapping mapping = BeanMapping.create("testName");// 获取
        Map src = new HashMap();
        src.put(ONE, null);// 测试null value
        src.put(TWO, "  "); // 测试emptyString
        src.put(THREE, " ljh "); // 测试trimStrings
        Map dest = new HashMap();
        dest.put(ONE_OTHER, "ljh"); // 先设一个值，验证下是否有mapping
        dest.put(TWO_OTHER, "ljh"); // 先设一个值，验证下是否有mapping

        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), "ljh");
        assertEquals(dest.get(TWO_OTHER), "ljh");
        assertEquals(dest.get(THREE_OTHER), "ljh");
    }

    @Test
    public void testNestedNameMapping() {// 测试嵌套mapping处理
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, BigInteger.class));
                fields(srcField(TWO, HashMap.class), targetField(TWO_OTHER, HashMap.class)).recursiveMapping(true,
                                                                                                             "nestedMapping");
            }

        };

        // 第三层mapping
        final BeanMappingBuilder nextNestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping("nextNestedMapping", HashMap.class, HashMap.class);
                fields(srcField(THREE), targetField(THREE_OTHER));
            }

        };

        // 第二层mapping
        BeanMappingBuilder nestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping("nestedMapping", HashMap.class, HashMap.class);
                fields(srcField(TWO), targetField(TWO_OTHER));
                fields(srcField(THREE), targetField(THREE_OTHER)).recursiveMapping(true).nestObject(nextNestedMapping);
            }

        };

        BeanMappingConfigHelper.getInstance().register(nestedMapping);
        BeanMappingConfigHelper.getInstance().register(nextNestedMapping);
        BeanMappingConfigHelper.getInstance().register(builder);

        BeanMapping mapping = new BeanMapping(builder);
        Map nextNestedMap = new HashMap();
        nextNestedMap.put(THREE, "nextNestedMap three");

        Map nestedMap = new HashMap();
        nestedMap.put(TWO, "nestedMap two");
        nestedMap.put(THREE, nextNestedMap);

        Map src = new HashMap();
        src.put(ONE, "10");
        src.put(TWO, nestedMap);

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), BigInteger.valueOf(10));

        Map nestedOtherMap = (Map) dest.get(TWO_OTHER);
        assertEquals(nestedOtherMap.get(TWO_OTHER), "nestedMap two");

        Map nextNestedOtherMap = (Map) nestedOtherMap.get(THREE_OTHER);
        assertEquals(nextNestedOtherMap.get(THREE_OTHER), "nextNestedMap three");
    }

    @Test
    public void testCollectionAndNestedNameMapping() {// 测试嵌套collection mapping处理
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, BigInteger.class));
                fields(srcField(TWO, List.class).componentClasses(HashMap.class),
                       targetField(TWO_OTHER, List.class).componentClasses(HashMap.class)).nestName("nestedMapping");
            }

        };

        // 第三层mapping
        final BeanMappingBuilder nextNestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping("nextNestedMapping", HashMap.class, HashMap.class);
                fields(srcField(THREE), targetField(THREE_OTHER));
            }

        };

        // 第二层mapping
        BeanMappingBuilder nestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping("nestedMapping", HashMap.class, HashMap.class);
                fields(srcField(TWO), targetField(TWO_OTHER));
                fields(srcField(THREE), targetField(THREE_OTHER)).recursiveMapping(true).nestObject(nextNestedMapping);
            }

        };

        BeanMappingConfigHelper.getInstance().register(nextNestedMapping);
        BeanMappingConfigHelper.getInstance().register(nestedMapping);
        BeanMappingConfigHelper.getInstance().register(builder);

        BeanMapping mapping = new BeanMapping(builder);
        Map nextNestedMap1 = new HashMap();
        nextNestedMap1.put(THREE, "nextNestedMap three1");

        Map nestedMap1 = new HashMap();
        nestedMap1.put(TWO, "nestedMap two1");
        nestedMap1.put(THREE, nextNestedMap1);

        Map nextNestedMap2 = new HashMap();
        nextNestedMap2.put(THREE, "nextNestedMap three2");

        Map nestedMap2 = new HashMap();
        nestedMap2.put(TWO, "nestedMap two2");
        nestedMap2.put(THREE, nextNestedMap2);

        Map src = new HashMap();
        src.put(ONE, "10");
        src.put(TWO, Arrays.asList(nestedMap1, nestedMap2)); // 映射集合

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), BigInteger.valueOf(10));

        List nestedOtherMap = (List) dest.get(TWO_OTHER);
        assertEquals(nestedOtherMap.size(), 2);

        Map nestedOtherMap1 = (Map) nestedOtherMap.get(0);
        assertEquals(nestedOtherMap1.get(TWO_OTHER), "nestedMap two1");
        Map nextNestedOtherMap1 = (Map) nestedOtherMap1.get(THREE_OTHER);
        assertEquals(nextNestedOtherMap1.get(THREE_OTHER), "nextNestedMap three1");

        Map nestedOtherMap2 = (Map) nestedOtherMap.get(1);
        assertEquals(nestedOtherMap2.get(TWO_OTHER), "nestedMap two2");
        Map nextNestedOtherMap2 = (Map) nestedOtherMap2.get(THREE_OTHER);
        assertEquals(nextNestedOtherMap2.get(THREE_OTHER), "nextNestedMap three2");
    }
}
