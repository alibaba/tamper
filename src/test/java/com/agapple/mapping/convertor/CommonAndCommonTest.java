package com.agapple.mapping.convertor;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * @author jianghang 2011-6-21 下午09:46:42
 */
public class CommonAndCommonTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testInteger() {
        int value = 10;

        Object integerValue = helper.getConvertor(int.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Integer.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Integer.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Integer.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Integer.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Integer.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Integer.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Integer.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Integer.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Integer.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Integer.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));

    }

    @Test
    public void testLong() {
        long value = 10;
        Object integerValue = helper.getConvertor(Long.class, Integer.class).convert(value, Integer.class);

        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Long.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Long.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Long.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Long.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Long.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Long.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Long.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Long.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Long.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Long.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testByte() {
        byte value = 10;
        Object integerValue = helper.getConvertor(Byte.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Byte.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Byte.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Byte.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Byte.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Byte.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Byte.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Byte.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Byte.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Byte.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Byte.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testDouble() {
        double value = 10;
        Object integerValue = helper.getConvertor(Double.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Double.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Double.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Double.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Double.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Double.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Double.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Double.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf((long) value));

        Object bigDecimalValue = helper.getConvertor(Double.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Double.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Double.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testFloat() {
        float value = 10;
        Object integerValue = helper.getConvertor(Float.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Float.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Float.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Float.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Float.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Float.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Float.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Float.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf((long) value));

        Object bigDecimalValue = helper.getConvertor(Float.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Float.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Float.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testBoolean() {
        boolean boolTest = true;
        int value = boolTest ? 1 : 0;

        Object integerValue = helper.getConvertor(Boolean.class, Integer.class).convert(boolTest, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Boolean.class, int.class).convert(boolTest, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Boolean.class, byte.class).convert(boolTest, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Boolean.class, short.class).convert(boolTest, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Boolean.class, long.class).convert(boolTest, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Boolean.class, float.class).convert(boolTest, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Boolean.class, double.class).convert(boolTest, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Boolean.class, BigInteger.class).convert(boolTest,
                                                                                              BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Boolean.class, BigDecimal.class).convert(boolTest,
                                                                                              BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Boolean.class, boolean.class).convert(boolTest, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(boolTest));

        Object charValue = helper.getConvertor(Boolean.class, char.class).convert(boolTest, char.class);
        assertEquals(charValue, Character.valueOf((char) (value)));
    }

    @Test
    public void testShort() {
        short value = 10;

        Object integerValue = helper.getConvertor(Short.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Short.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Short.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(Short.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(Short.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Short.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Short.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Short.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Short.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Short.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Short.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testChar() {
        Character value = 10;

        Object integerValue = helper.getConvertor(Character.class, Integer.class).convert(value, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(Character.class, int.class).convert(value, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(Character.class, byte.class).convert(value, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) (char) value));

        Object shortValue = helper.getConvertor(Character.class, short.class).convert(value, short.class);
        assertEquals(shortValue, Short.valueOf((short) (char) value));

        Object longValue = helper.getConvertor(Character.class, long.class).convert(value, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(Character.class, float.class).convert(value, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(Character.class, double.class).convert(value, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(Character.class, BigInteger.class).convert(value, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(Character.class, BigDecimal.class).convert(value, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(Character.class, boolean.class).convert(value, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(Character.class, char.class).convert(value, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testBigInteger() {
        BigInteger oneValue = BigInteger.TEN;
        int value = oneValue.intValue();

        Object integerValue = helper.getConvertor(BigInteger.class, Integer.class).convert(oneValue, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(BigInteger.class, int.class).convert(oneValue, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(BigInteger.class, byte.class).convert(oneValue, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) (char) value));

        Object shortValue = helper.getConvertor(BigInteger.class, short.class).convert(oneValue, short.class);
        assertEquals(shortValue, Short.valueOf((short) (char) value));

        Object longValue = helper.getConvertor(BigInteger.class, long.class).convert(oneValue, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(BigInteger.class, float.class).convert(oneValue, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(BigInteger.class, double.class).convert(oneValue, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Convertor nullConvertor = helper.getConvertor(BigInteger.class, BigInteger.class);
        assertNull(nullConvertor);// 相同类型，不需要转化

        Object bigDecimalValue = helper.getConvertor(BigInteger.class, BigDecimal.class).convert(oneValue,
                                                                                                 BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(BigInteger.class, boolean.class).convert(oneValue, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(BigInteger.class, char.class).convert(oneValue, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testBigDecimal() {
        BigDecimal oneValue = BigDecimal.TEN;
        int value = oneValue.intValue();

        Object integerValue = helper.getConvertor(BigDecimal.class, Integer.class).convert(oneValue, Integer.class);
        assertEquals(integerValue.getClass(), Integer.class);

        Object intValue = helper.getConvertor(BigDecimal.class, int.class).convert(oneValue, int.class);
        assertEquals(intValue.getClass(), Integer.class); // 也返回原型对应的Object

        Object byteValue = helper.getConvertor(BigDecimal.class, byte.class).convert(oneValue, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) (char) value));

        Object shortValue = helper.getConvertor(BigDecimal.class, short.class).convert(oneValue, short.class);
        assertEquals(shortValue, Short.valueOf((short) (char) value));

        Object longValue = helper.getConvertor(BigDecimal.class, long.class).convert(oneValue, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(BigDecimal.class, float.class).convert(oneValue, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(BigDecimal.class, double.class).convert(oneValue, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(BigDecimal.class, BigInteger.class).convert(oneValue,
                                                                                                 BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Convertor nullConvertor = helper.getConvertor(BigDecimal.class, BigDecimal.class);
        assertNull(nullConvertor);// 相同类型，不需要转化

        Object boolValue = helper.getConvertor(BigDecimal.class, boolean.class).convert(oneValue, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(BigDecimal.class, char.class).convert(oneValue, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testError() {
        long maxLong = Long.MAX_VALUE;
        long minLong = Long.MIN_VALUE;

        double maxDouble = Double.MAX_VALUE;
        double minDouble = Double.MIN_VALUE;

        try {
            helper.getConvertor(Long.class, Integer.class).convert(maxLong, Integer.class);
            Assert.fail();// 不会走到这一步
        } catch (BeanMappingException e) {
        }

        try {
            helper.getConvertor(Long.class, byte.class).convert(minLong, byte.class);
            Assert.fail();// 不会走到这一步
        } catch (BeanMappingException e) {
        }

        try {
            helper.getConvertor(Long.class, short.class).convert(maxLong, short.class);
            Assert.fail();// 不会走到这一步
        } catch (BeanMappingException e) {
        }

        try {
            helper.getConvertor(Long.class, long.class).convert(minLong, long.class);
        } catch (BeanMappingException e) {
            Assert.fail();// 不会走到这一步
        }

        try {
            helper.getConvertor(Double.class, float.class).convert(maxDouble, float.class);
            Assert.fail();// 不会走到这一步
        } catch (BeanMappingException e) {
        }

        try {
            helper.getConvertor(Double.class, double.class).convert(minDouble, double.class);
        } catch (BeanMappingException e) {
            Assert.fail();// 不会走到这一步
        }

        Object bigIntegerValue = helper.getConvertor(Long.class, BigInteger.class).convert(maxLong, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(maxLong));

        Object bigDecimalValue = helper.getConvertor(Double.class, BigDecimal.class).convert(maxDouble,
                                                                                             BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(maxDouble));

        try {
            helper.getConvertor(Long.class, boolean.class).convert(maxLong, float.class);
        } catch (BeanMappingException e) {
            Assert.fail();// 不会走到这一步
        }

        try {
            helper.getConvertor(Long.class, char.class).convert(maxLong, char.class);
        } catch (BeanMappingException e) {
            Assert.fail();// 不会走到这一步
        }

    }
}
