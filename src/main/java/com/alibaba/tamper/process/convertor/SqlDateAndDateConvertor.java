package com.alibaba.tamper.process.convertor;

import java.util.Date;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * Date <-> SqlDate 之间的转化
 * 
 * @author jianghang 2011-11-16 上午09:47:01
 */
public class SqlDateAndDateConvertor {

    public static class SqlDateToDateConvertor extends AbastactConvertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (Date.class != destClass) {
                throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
            }
            if (src instanceof java.sql.Date) {
                return new Date(((java.sql.Date) src).getTime());
            }
            if (src instanceof java.sql.Timestamp) {
                return new Date(((java.sql.Timestamp) src).getTime());
            }
            if (src instanceof java.sql.Time) {
                return new Date(((java.sql.Time) src).getTime());
            }
            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    public static class DateToSqlDateConvertor extends AbastactConvertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (Date.class.isInstance(src)) { // 必须是字符串
                Date date = (Date) src;
                long value = date.getTime();
                // java.sql.Date
                if (destClass.equals(java.sql.Date.class)) {
                    return new java.sql.Date(value);
                }

                // java.sql.Time
                if (destClass.equals(java.sql.Time.class)) {
                    return new java.sql.Time(value);
                }

                // java.sql.Timestamp
                if (destClass.equals(java.sql.Timestamp.class)) {
                    return new java.sql.Timestamp(value);
                }
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
}
