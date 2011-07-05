package com.agapple.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingConfigRespository;
import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.NestedTargetMappingObject;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.object.TargetMappingObject;
import com.agapple.mapping.process.convetor.StringAndDateConvertor.StringToDateDay;

/**
 * 基于API构造mapping进行测试，更全面的测试
 * 
 * @author jianghang 2011-6-22 下午01:51:19
 */
public class BeanMappingDymaicTest extends TestCase {

    private static final String ONE         = "one";
    private static final String TWO         = "two";
    private static final String THREE       = "three";
    private static final String FOUR        = "four";
    private static final String ONE_OTHER   = "oneOther";
    private static final String TWO_OTHER   = "twoOther";
    private static final String THREE_OTHER = "threeOther";
    private static final String FOUR_OTHER  = "fourOther";

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
    public void testBehavior() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, String.class));
                fields(srcField(TWO, String.class), targetField(TWO_OTHER, String.class));
                fields(srcField(THREE, String.class), targetField(THREE_OTHER, String.class));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
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
    public void testNestedMapping() {// 测试嵌套mapping处理
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, BigInteger.class));
                fields(srcField(TWO, BigInteger.class), targetField(TWO_OTHER, Integer.class));
                fields(srcField(THREE, SrcMappingObject.class), targetField(THREE_OTHER, TargetMappingObject.class)).recursiveMapping(
                                                                                                                                      true);
            }

        };
        // 第二层mapping
        BeanMappingBuilder nestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(SrcMappingObject.class, TargetMappingObject.class);
                fields(srcField("intValue"), targetField("intValue"));
                fields(srcField("integerValue"), targetField("integerValue"));
                fields(srcField("start"), targetField("start"));
                fields(srcField("name"), targetField("targetName"));
                fields(srcField("mapping"), targetField("mapping")).recursiveMapping(true);
            }

        };
        // 第三层mapping
        BeanMappingBuilder nextNestedMapping = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(NestedSrcMappingObject.class, NestedTargetMappingObject.class);
                fields(srcField("name"), targetField("name"));
                fields(srcField("bigDecimalValue"), targetField("value"));
            }

        };

        BeanMappingConfigHelper.getInstance().register(nestedMapping);
        BeanMappingConfigHelper.getInstance().register(nextNestedMapping);

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap();
        src.put(ONE, "10");
        src.put(TWO, BigInteger.TEN);
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        nestedSrcRef.setName("ljh");
        srcRef.setMapping(nestedSrcRef);
        src.put(THREE, srcRef);

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), BigInteger.valueOf(10));
        assertEquals(dest.get(TWO_OTHER), Integer.valueOf(10));

        TargetMappingObject targetRef = (TargetMappingObject) dest.get(THREE_OTHER);
        assertNotNull(targetRef.getMapping());
        assertEquals(targetRef.getIntValue(), srcRef.getIntValue());
        assertEquals(targetRef.getIntegerValue(), srcRef.getIntegerValue());
        assertEquals(targetRef.getTargetName(), srcRef.getName());
        assertEquals(targetRef.getMapping().getName(), srcRef.getMapping().getName());
        assertEquals(targetRef.getMapping().getValue(), srcRef.getMapping().getBigDecimalValue().toString());
    }

    @Test
    public void testAutoConvertor() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, BigInteger.class));
                fields(srcField(TWO, BigInteger.class), targetField(TWO_OTHER, Integer.class));
                fields(srcField(THREE, List.class), targetField(THREE_OTHER, Set.class));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap();
        src.put(ONE, "10");
        src.put(TWO, BigInteger.TEN);

        Map nested = new HashMap();
        nested.putAll(src);
        src.put(THREE, Arrays.asList(nested));

        Map dest = new HashMap();

        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), BigInteger.TEN);
        assertEquals(dest.get(TWO_OTHER), Integer.valueOf(10));
        Set<Map> destNested = (Set) dest.get(THREE_OTHER);
        Map destNestedMap = destNested.iterator().next();
        // 验证下嵌套的属性，不会进行递归的mapping处理，所以不会出现ONE_OTHER属性
        assertEquals(destNestedMap.get(ONE), "10");
        assertEquals(destNestedMap.get(TWO), BigInteger.TEN);
    }

    @Test
    public void testAliasConvertor() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, Date.class)).convertor("StringToDateDay");
                fields(srcField(TWO, String.class), targetField(TWO_OTHER, Calendar.class)).convertor(
                                                                                                      "StringToCalendarDay");
                fields(srcField(THREE, Date.class), targetField(THREE_OTHER, String.class)).convertor("DateDayToString");
                fields(srcField(FOUR, Calendar.class), targetField(FOUR_OTHER, String.class)).convertor(
                                                                                                        "CalendarDayToString");
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap();
        src.put(ONE, "2011-10-01");
        src.put(TWO, "2011-10-01");

        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(2011, 10 - 1, 01, 0, 0, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        Date dayDate = dayCalendar.getTime();

        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(2011, 10 - 1, 01, 23, 59, 59);
        timeCalendar.set(Calendar.MILLISECOND, 0);
        src.put(THREE, timeCalendar.getTime());

        src.put(FOUR, timeCalendar);

        Map dest = new HashMap();

        mapping.mapping(src, dest);
        assertEquals(((Date) dest.get(ONE_OTHER)).getTime(), dayDate.getTime());
        assertEquals(((Calendar) dest.get(TWO_OTHER)).getTime().getTime(), dayCalendar.getTime().getTime());
        assertEquals(dest.get(THREE_OTHER), "2011-10-01");
        assertEquals(dest.get(FOUR_OTHER), "2011-10-01");
    }

    @Test
    public void testConvertorClass() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, Date.class)).convertor(StringToDateDay.class);
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap();
        src.put(ONE, "2011-10-01");

        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(2011, 10 - 1, 01, 0, 0, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        Date dayDate = dayCalendar.getTime();

        Map dest = new HashMap();

        mapping.mapping(src, dest);
        assertEquals(((Date) dest.get(ONE_OTHER)).getTime(), dayDate.getTime());
    }

    @Test
    public void testDefaultValue() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField(ONE, String.class), targetField(ONE_OTHER, Date.class)).defaultValue("2011-10-01").convertor(
                                                                                                                             "StringToDateDay");
                fields(srcField(TWO, BigInteger.class), targetField(TWO_OTHER, Integer.class)).defaultValue("10");
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap(); // 不设置任何记录
        Map dest = new HashMap();

        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(2011, 10 - 1, 01, 0, 0, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        Date dayDate = dayCalendar.getTime();

        mapping.mapping(src, dest);
        assertEquals(((Date) dest.get(ONE_OTHER)).getTime(), dayDate.getTime());
        assertEquals(dest.get(TWO_OTHER), Integer.valueOf(10));
    }

    @Test
    public void testFieldNoMethod() { // 测试下属性没有对应的方法
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField("intValue"), targetField(ONE_OTHER, String.class));
                fields(srcField("integerValue"), targetField(TWO_OTHER, String.class));
                fields(srcField("bigDecimalValue"), targetField(THREE_OTHER, String.class));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map dest = new HashMap();
        mapping.mapping(new NoMethodBean(), dest);
        assertEquals(dest.get(ONE_OTHER), "10");
        assertEquals(dest.get(TWO_OTHER), "10");
        assertEquals(dest.get(THREE_OTHER), "10");
    }

    @Test
    public void testThisSymbol() { // 测试下this的特殊属性
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField("intValue"), targetField(ONE_OTHER, String.class));
                fields(srcField("integerValue"), targetField(TWO_OTHER, String.class));
                fields(srcField("bigDecimalValue"), targetField(THREE_OTHER, String.class));
                fields(srcField("this"), targetField(FOUR_OTHER));
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map dest = new HashMap();
        mapping.mapping(new NoMethodBean(), dest);
        assertEquals(dest.get(ONE_OTHER), "10");
        assertEquals(dest.get(TWO_OTHER), "10");
        assertEquals(dest.get(THREE_OTHER), "10");
        assertEquals(dest.get(FOUR_OTHER).getClass(), NoMethodBean.class);
    }

}

class NoMethodBean {

    private int       intValue;
    protected Integer integerValue;
    public BigDecimal bigDecimalValue;

    public NoMethodBean(){
        intValue = 10;
        integerValue = Integer.valueOf(10);
        bigDecimalValue = BigDecimal.TEN;
    }

    public int getIntValue() {
        return intValue;
    }

}
