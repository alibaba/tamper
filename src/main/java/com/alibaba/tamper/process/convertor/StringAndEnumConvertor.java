package com.alibaba.tamper.process.convertor;

import com.alibaba.tamper.core.BeanMappingException;

/**
 * string <-> Enum 之间的转化
 * 
 * @author jianghang 2011-7-12 下午12:49:30
 */
public class StringAndEnumConvertor {

    /**
     * string -> Enum 对象的转化
     */
    public static class StringToEnum extends AbastactConvertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (src instanceof String && destClass.isEnum()) {
                return Enum.valueOf(destClass, (String) src);
            }

            throw new BeanMappingException("Unsupported convert: [" + src.getClass() + "," + destClass.getName() + "]");

        }
    }

    /**
     * Enum -> String 对象的转化
     */
    public static class EnumToString extends AbastactConvertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (src.getClass().isEnum() && destClass == String.class) {
                return ((Enum) src).name(); // 返回定义的enum name
            }

            throw new BeanMappingException("Unsupported convert: [" + src.getClass() + "," + destClass.getName() + "]");
        }
    }
}
