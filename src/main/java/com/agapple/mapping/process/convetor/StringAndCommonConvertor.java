package com.agapple.mapping.process.convetor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.agapple.mapping.core.BeanMappingException;

/**
 * string <-> common对象 之间的转化
 * 
 * <pre>
 * common对象范围：8种Primitive和对应的Java类型，BigDecimal, BigInteger
 * 
 * </pre>
 * 
 * @author jianghang 2011-5-25 下午11:11:25
 */
public class StringAndCommonConvertor {

    /**
     * string -> common对象的转化
     */
    public static class StringToCommon extends AbastactConvertor {

        protected static final Set TRUE_STRINGS  = new HashSet(Arrays.asList(new String[] { "true", "on", "yes", "y" }));
        protected static final Set FALSE_STRINGS = new HashSet(Arrays.asList(new String[] { "false", "null", "nul",
                                                         "nil", "off", "no", "n" }));

        protected Boolean booleanConvert(Object value) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }

            if (value instanceof Number) {
                return (Math.abs(((Number) value).doubleValue()) < Float.MIN_VALUE) ? Boolean.FALSE : Boolean.TRUE;
            }

            if (value instanceof String) {
                String strValue = ((String) value).trim();
                try {
                    return (Integer.parseInt(strValue) == 0) ? Boolean.FALSE : Boolean.TRUE;
                } catch (NumberFormatException e) {
                    strValue = strValue.toLowerCase();

                    if (TRUE_STRINGS.contains(strValue)) {
                        return Boolean.TRUE;
                    }

                    if (FALSE_STRINGS.contains(strValue)) {
                        return Boolean.FALSE;
                    }

                }
            }

            throw new BeanMappingException("Unsupported convert: [" + String.class + "," + Boolean.class.getName()
                                           + "]");
        }

        protected Character charConvert(Object value) {
            if (value instanceof Character) {
                return (Character) value;
            }

            if (value instanceof Number) {
                return new Character((char) ((Number) value).intValue());
            }

            if (value instanceof String) {
                String strValue = ((String) value).trim();

                try {
                    return new Character((char) Integer.parseInt(strValue));
                } catch (NumberFormatException e) {
                }
            }

            throw new BeanMappingException("Unsupported convert: [" + String.class + "," + Character.class.getName()
                                           + "]");
        }

        @Override
        public Object convert(Object src, Class destClass) {
            if (String.class.isInstance(src)) { // src必须是String
                String str = (String) src;
                if (destClass == Double.class || destClass == double.class) {
                    return Double.valueOf(str);
                }

                if (destClass == Float.class || destClass == float.class) {
                    return Float.valueOf(str);
                }

                if (destClass == Boolean.class || destClass == boolean.class) {
                    return booleanConvert(str);
                }

                if (destClass == Integer.class || destClass == int.class) {
                    return Integer.valueOf(str);
                }

                if (destClass == Short.class || destClass == short.class) {
                    return Short.valueOf(str);
                }

                if (destClass == Long.class || destClass == long.class) {
                    return Long.valueOf(str);
                }

                if (destClass == Byte.class || destClass == byte.class) {
                    return Byte.valueOf(str);
                }

                if (destClass == Character.class || destClass == char.class) {
                    return charConvert(str); // 只取第一个字符
                }

                if (destClass == BigDecimal.class) {
                    return new BigDecimal(str);
                }

                if (destClass == BigInteger.class) {
                    return new BigInteger(str);
                }
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
}
