/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.process.convetor;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.agapple.mapping.core.BeanMappingException;

/**
 * common <-> common对象之间的转化
 * 
 * <pre>
 * common对象范围：8种Primitive和对应的Java类型，BigDecimal, BigInteger
 * 
 * </pre>
 * 
 * @author jianghang 2011-6-14 下午10:09:09
 */
public class CommonAndCommonConvertor {

    /**
     * common <-> common对象的转化
     */
    public static class CommonToCommon extends AbastactConvertor {

        private static final Integer ZERO = new Integer(0);
        private static final Integer ONE  = new Integer(1);

        // Number数据处理
        private Object toCommon(Class srcClass, Class targetClass, Number value) {
            // 相同类型,直接返回
            if (targetClass.equals(value.getClass())) {
                return value;
            }

            // Integer
            if (targetClass == Integer.class || targetClass == int.class) {
                long longValue = value.longValue();
                if (longValue > Integer.MAX_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too large for "
                                                   + targetClass.getName());
                }
                if (longValue < Integer.MIN_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too small "
                                                   + targetClass.getName());
                }
                return Integer.valueOf(value.intValue());
            }

            // Long
            if (targetClass == Long.class || targetClass == long.class) {
                return Long.valueOf(value.longValue());
            }

            // Boolean
            if (targetClass == Boolean.class || targetClass == boolean.class) {
                long longValue = value.longValue();
                return Boolean.valueOf(longValue > 0 ? true : false);
            }

            // Byte
            if (targetClass == Byte.class || targetClass == byte.class) {
                long longValue = value.longValue();
                if (longValue > Byte.MAX_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too large for "
                                                   + targetClass.getName());
                }
                if (longValue < Byte.MIN_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too small "
                                                   + targetClass.getName());
                }
                return Byte.valueOf(value.byteValue());
            }

            // Double
            if (targetClass == Double.class || targetClass == double.class) {
                return Double.valueOf(value.doubleValue());
            }

            // BigDecimal
            if (targetClass == BigDecimal.class) {
                if (value instanceof Float || value instanceof Double) {
                    return new BigDecimal(value.toString());
                } else if (value instanceof BigInteger) {
                    return new BigDecimal((BigInteger) value);
                } else {
                    return BigDecimal.valueOf(value.longValue());
                }
            }

            // BigInteger
            if (targetClass == BigInteger.class) {
                if (value instanceof BigDecimal) {
                    return ((BigDecimal) value).toBigInteger();
                } else {
                    return BigInteger.valueOf(value.longValue());
                }
            }

            // Short
            if (targetClass == Short.class || targetClass == short.class) {
                long longValue = value.longValue();
                if (longValue > Short.MAX_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too large for "
                                                   + targetClass.getName());
                }
                if (longValue < Short.MIN_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too small "
                                                   + targetClass.getName());
                }
                return Short.valueOf(value.shortValue());
            }

            // Float
            if (targetClass == Float.class || targetClass == float.class) {
                double doubleValue = value.doubleValue();
                if (doubleValue > Float.MAX_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too large for "
                                                   + targetClass.getName());
                }

                if (doubleValue < Float.MIN_VALUE) {
                    throw new BeanMappingException(srcClass.getName() + " value '" + value + "' is too small for "
                                                   + targetClass.getName());
                }
                return Float.valueOf(value.floatValue());
            }

            // Character
            if (targetClass == Character.class || targetClass == char.class) {
                long longValue = value.longValue();
                // Character没有很明显的值上下边界，直接依赖于jvm的转型
                return Character.valueOf((char) longValue);
            }

            throw new BeanMappingException("Unsupported convert: [" + srcClass.getName() + "," + targetClass.getName()
                                           + "]");
        }

        // BigDecimal数据处理
        private Object toCommon(Class srcClass, Class targetClass, BigDecimal value) {
            if (targetClass == srcClass) {
                return value;
            }

            if (targetClass == BigInteger.class) {
                return value.toBigInteger();
            }

            // 其他类型的处理，先转化为String，再转到对应的目标对象
            StringAndCommonConvertor.StringToCommon strConvetor = new StringAndCommonConvertor.StringToCommon();
            return strConvetor.convert(value.toPlainString(), targetClass);
        }

        // BigInteger数据处理
        private Object toCommon(Class srcClass, Class targetClass, BigInteger value) {
            if (targetClass == srcClass) {
                return value;
            }

            if (targetClass == BigDecimal.class) {
                return new BigDecimal((BigInteger) value);
            }

            // 其他类型的处理，先转化为String，再转到对应的目标对象
            StringAndCommonConvertor.StringToCommon strConvetor = new StringAndCommonConvertor.StringToCommon();
            return strConvetor.convert(value.toString(), targetClass);
        }

        @Override
        public Object convert(Object src, Class targetClass) {
            Class srcClass = src.getClass();
            if (srcClass == Integer.class || srcClass == int.class) {
                return toCommon(srcClass, targetClass, (Integer) src);
            }

            if (srcClass == Long.class || srcClass == long.class) {
                return toCommon(srcClass, targetClass, (Long) src);
            }

            if (srcClass == Boolean.class || srcClass == boolean.class) {
                Boolean boolValue = (Boolean) src;
                return toCommon(srcClass, targetClass, boolValue.booleanValue() ? ONE : ZERO);
            }

            if (srcClass == Byte.class || srcClass == byte.class) {
                return toCommon(Double.class, targetClass, (Byte) src);
            }

            if (srcClass == Double.class || srcClass == double.class) {
                return toCommon(srcClass, targetClass, (Double) src);
            }

            if (srcClass == BigDecimal.class) {
                return toCommon(srcClass, targetClass, (BigDecimal) src);
            }

            if (srcClass == BigInteger.class) {
                return toCommon(srcClass, targetClass, (BigInteger) src);
            }

            if (srcClass == Float.class || srcClass == float.class) {
                return toCommon(srcClass, targetClass, (Float) src);
            }

            if (srcClass == Short.class || srcClass == short.class) {
                return toCommon(srcClass, targetClass, (Short) src);
            }

            if (srcClass == Character.class || srcClass == char.class) {
                Character charvalue = (Character) src;
                return toCommon(srcClass, targetClass, Integer.valueOf((int) charvalue));
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + targetClass.getName() + "]");
        }
    }
}
