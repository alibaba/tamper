package com.agapple.mapping.process.convetor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义自己的convertor仓库
 * 
 * @author jianghang 2011-5-25 下午10:16:10
 */
public class ConvertorRepository {

    private static final String    SEPERATOR   = ":";
    private static String          int_name    = Integer.class.getName();
    private static String          short_name  = Short.class.getName();
    private static String          long_name   = Long.class.getName();
    private static String          char_name   = Character.class.getName();
    private static String          void_name   = Void.class.getName();
    private static String          double_name = Double.class.getName();
    private static String          float_name  = Float.class.getName();
    private static String          byte_name   = Byte.class.getName();
    private static String          bool_name   = Boolean.class.getName();

    private Map<String, Convertor> convertors  = new ConcurrentHashMap<String, Convertor>(10);

    public Convertor getConvertor(Class src, Class dest) {
        // 按照src->dest来取映射
        return convertors.get(mapperConvertorName(src, dest));
    }

    public Convertor getConvertor(String alias) {
        return convertors.get(alias);
    }

    public void registerConvertor(Class src, Class dest, Convertor convertor) {
        String key = mapperConvertorName(src, dest);
        // 对于已经注册的convert，进行覆盖处理
        if (convertor != null) {
            convertors.put(key, convertor);
        }
    }

    public void registerConvertor(String alias, Convertor convertor) {
        // 对于已经注册的convert，进行覆盖处理
        if (convertor != null) {
            convertors.put(alias, convertor);
        }
    }

    // ========================= helper method ===================

    private String mapperConvertorName(Class src, Class dest) {
        String name1 = getName(src);
        String name2 = getName(dest);

        return name1 + SEPERATOR + name2;
    }

    private String getName(Class type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return int_name;
            } else if (type == short.class) {
                return short_name;
            } else if (type == long.class) {
                return long_name;
            } else if (type == char.class) {
                return char_name;
            } else if (type == void.class) {
                return void_name;
            } else if (type == double.class) {
                return double_name;
            } else if (type == float.class) {
                return float_name;
            } else if (type == byte.class) {
                return byte_name;
            } else if (type == boolean.class) {
                return bool_name;
            }
        }

        return type.getName();
    }

}
