package com.alibaba.tamper.convertor;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.tamper.process.convertor.Convertor;
import com.alibaba.tamper.process.convertor.ConvertorHelper;

/**
 * @author jianghang 2011-7-12 下午01:04:33
 */
public class StringAndEnumTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testStringAndEnum() {
        Convertor enumToString = helper.getConvertor(TestEnum.class, String.class);
        Convertor stringtoEnum = helper.getConvertor(String.class, TestEnum.class);
        String VALUE = "TWO";

        Object str = enumToString.convert(TestEnum.TWO, String.class); // 数字
        assertEquals(str, VALUE);
        Object enumobj = stringtoEnum.convert(VALUE, TestEnum.class); // BigDecimal
        assertEquals(enumobj, TestEnum.TWO);
    }

    public static enum TestEnum {
        ONE, TWO, THREE;
    }
}
