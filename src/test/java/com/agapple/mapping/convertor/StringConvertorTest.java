package com.agapple.mapping.convertor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * convertor相关的单元测试
 * 
 * @author jianghang 2011-5-26 上午11:17:36
 */
public class StringConvertorTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testObjectToString() {
        Convertor ct = helper.getConvertor(Object.class, String.class);
        String VALUE = "1";

        Object integer = ct.convert(Integer.valueOf(VALUE), String.class); // 数字
        assertEquals(integer, VALUE);
        Object bigDecimal = ct.convert(new BigDecimal(VALUE), String.class); // BigDecimal
        assertEquals(bigDecimal, VALUE);
        Object bigInteger = ct.convert(new BigInteger(VALUE), String.class); // BigInteger
        assertEquals(bigInteger, VALUE);

        ConvertorModel model = new ConvertorModel();
        model.setI(1);
        model.setInteger(1);
        model.setBigDecimal(new BigDecimal(VALUE));
        Object modelStr = ct.convert(model, String.class); // ConvertorModel
        assertNotNull(modelStr);
    }

    @Test
    public void testStringToCommon() {
        String strValue = "10";
        int value = 10;
        // 基本变量
        Object intValue = helper.getConvertor(String.class, int.class).convert(strValue, int.class);
        assertEquals(intValue, value);

        Object integerValue = helper.getConvertor(String.class, Integer.class).convert(strValue, Integer.class);
        assertEquals(integerValue, value);

        Object byteValue = helper.getConvertor(String.class, byte.class).convert(strValue, byte.class);
        assertEquals(byteValue, Byte.valueOf((byte) value));

        Object shortValue = helper.getConvertor(String.class, short.class).convert(strValue, short.class);
        assertEquals(shortValue, Short.valueOf((short) value));

        Object longValue = helper.getConvertor(String.class, long.class).convert(strValue, long.class);
        assertEquals(longValue, Long.valueOf((long) value));

        Object floatValue = helper.getConvertor(String.class, float.class).convert(strValue, float.class);
        assertEquals(floatValue, Float.valueOf((float) value));

        Object doubleValue = helper.getConvertor(String.class, double.class).convert(strValue, double.class);
        assertEquals(doubleValue, Double.valueOf((double) value));

        Object bigIntegerValue = helper.getConvertor(String.class, BigInteger.class).convert(strValue, BigInteger.class);
        assertEquals(bigIntegerValue, BigInteger.valueOf(value));

        Object bigDecimalValue = helper.getConvertor(String.class, BigDecimal.class).convert(strValue, BigDecimal.class);
        assertEquals(bigDecimalValue, BigDecimal.valueOf(value));

        Object boolValue = helper.getConvertor(String.class, boolean.class).convert(strValue, boolean.class);
        assertEquals(boolValue, Boolean.valueOf(value > 0 ? true : false));

        Object charValue = helper.getConvertor(String.class, char.class).convert(strValue, char.class);
        assertEquals(charValue, Character.valueOf((char) value));
    }

    @Test
    public void testStringAndDateDefault() {
        Convertor stringDate = helper.getConvertor(String.class, Date.class);
        Convertor dateString = helper.getConvertor(Date.class, String.class);

        Convertor stringCalendar = helper.getConvertor(String.class, Calendar.class);
        Convertor calendarString = helper.getConvertor(Calendar.class, String.class);

        String time = "2010-10-01 23:59:59";
        Calendar c1 = Calendar.getInstance();
        c1.set(2010, 10 - 1, 01, 23, 59, 59);
        c1.set(Calendar.MILLISECOND, 0);
        Date timeDate = c1.getTime();

        // 验证默认的转化器
        Object stringDateValue = stringDate.convert(time, Date.class);
        assertTrue(timeDate.equals(stringDateValue));
        Object dateStringValue = dateString.convert(timeDate, String.class);
        assertTrue(time.equals(dateStringValue));

        Object stringCalendarValue = stringCalendar.convert(time, Calendar.class);
        assertTrue(c1.equals(stringCalendarValue));
        Object calendarStringValue = calendarString.convert(c1, String.class);
        assertTrue(time.equals(calendarStringValue));
    }

    @Test
    public void testStringAndDateAlias() {
        Convertor stringDateDay = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_DATE_DAY);
        Convertor stringDateTime = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_DATE_TIME);
        Convertor dateDayString = helper.getConvertor(ConvertorHelper.ALIAS_DATE_DAY_TO_STRING);
        Convertor dateTimeString = helper.getConvertor(ConvertorHelper.ALIAS_DATE_TIME_TO_STRING);

        Convertor stringCalendarDay = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_CALENDAR_DAY);
        Convertor stringCalendarTime = helper.getConvertor(ConvertorHelper.ALIAS_STRING_TO_CALENDAR_TIME);
        Convertor calendarDayString = helper.getConvertor(ConvertorHelper.ALIAS_CALENDAR_DAY_TO_STRING);
        Convertor calendarTimeString = helper.getConvertor(ConvertorHelper.ALIAS_CALENDAR_TIME_TO_STRING);

        String day = "2010-10-01";
        String time = "2010-10-01 23:59:59";
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(2010, 10 - 1, 01, 23, 59, 59);
        timeCalendar.set(Calendar.MILLISECOND, 0);
        Date timeDate = timeCalendar.getTime();
        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(2010, 10 - 1, 1, 0, 0, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        Date dayDate = dayCalendar.getTime();

        // date转化验证
        Object stringDateDayValue = stringDateDay.convert(day, Date.class);
        assertTrue(dayDate.equals(stringDateDayValue));
        Object stringDateTimeValue = stringDateTime.convert(time, Date.class);
        assertTrue(timeDate.equals(stringDateTimeValue));
        Object dateDayStringValue = dateDayString.convert(dayDate, String.class);
        assertTrue(day.equals(dateDayStringValue));
        Object dateTimeStringValue = dateTimeString.convert(timeDate, String.class);
        assertTrue(time.equals(dateTimeStringValue));
        // calendar转化验证
        Object stringCalendarDayValue = stringCalendarDay.convert(day, Calendar.class);
        assertTrue(dayCalendar.getTime().equals(((Calendar) stringCalendarDayValue).getTime()));
        Object stringCalendarTimeValue = stringCalendarTime.convert(time, Calendar.class);
        assertTrue(timeCalendar.getTime().equals(((Calendar) stringCalendarTimeValue).getTime()));
        Object calendarDayStringValue = calendarDayString.convert(dayCalendar, String.class);
        assertTrue(day.equals(calendarDayStringValue));
        Object calendarTimeStringValue = calendarTimeString.convert(timeCalendar, String.class);
        assertTrue(time.equals(calendarTimeStringValue));

    }

}
