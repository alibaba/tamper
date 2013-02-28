package com.alibaba.tamper.convertor;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.tamper.process.convertor.Convertor;
import com.alibaba.tamper.process.convertor.ConvertorHelper;

public class DateAndSqlDateTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testDateAndSqlDate() {
        Calendar c1 = Calendar.getInstance();
        c1.set(2010, 10 - 1, 01, 23, 59, 59);
        c1.set(Calendar.MILLISECOND, 0);
        Date timeDate = c1.getTime();

        Convertor dateToSql = helper.getConvertor(Date.class, java.sql.Date.class);
        java.sql.Date sqlDate = (java.sql.Date) dateToSql.convert(timeDate, java.sql.Date.class);
        assertNotNull(sqlDate);

        java.sql.Time sqlTime = (java.sql.Time) dateToSql.convert(timeDate, java.sql.Time.class);
        assertNotNull(sqlTime);

        java.sql.Timestamp sqlTimestamp = (java.sql.Timestamp) dateToSql.convert(timeDate, java.sql.Timestamp.class);
        assertNotNull(sqlTimestamp);

        Convertor sqlToDate = helper.getConvertor(java.sql.Date.class, Date.class);
        Date date = (Date) sqlToDate.convert(sqlDate, Date.class);
        assertEquals(timeDate, date);

        date = (Date) sqlToDate.convert(sqlTime, Date.class);
        assertEquals(timeDate, date);

        date = (Date) sqlToDate.convert(sqlTimestamp, Date.class);
        assertEquals(timeDate, date);
    }

}
