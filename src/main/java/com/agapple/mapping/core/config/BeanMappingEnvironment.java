package com.agapple.mapping.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.core.introspect.Uberspect;
import com.agapple.mapping.core.introspect.UberspectImpl;
import com.agapple.mapping.core.process.ValueProcess;

/**
 * mapping的一些环境变量配置
 * 
 * @author jianghang 2012-1-30 下午03:59:21
 */
public class BeanMappingEnvironment {

    private static final String       config                     = "mapping.properties";
    private static final String       BEANMAP_VPS                = "beanMap.valueProcess.list";
    private static final String       BEANMAPPING_VPS            = "beanMapping.valueProcess.list";
    private static final String       BEANCOPY_VPS               = "beanCopy.valueProcess.list";

    private static final String       VALUEPROCESS_PREFIX        = "valueProcess.";
    private static final String       UBERSPECTOR_IMPL           = "uberspect.impl";

    private static Properties         properties                 = new Properties(System.getProperties());
    private static List<ValueProcess> beanMappingVps;
    private static List<ValueProcess> beanMapVps;
    private static List<ValueProcess> beanCopyVps;
    private static Class              uberspectClazz;
    private static boolean            isBeanMappingSupportScript = false;

    static {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
        try {
            properties.load(stream);
        } catch (IOException e) {
            throw new BeanMappingException("can't found " + config);
        }
        try {
            // 解析value process
            String vps = properties.getProperty(BEANMAPPING_VPS, StringUtils.EMPTY);
            beanMappingVps = parseVps(vps);
            isBeanMappingSupportScript = StringUtils.containsIgnoreCase(vps, "script");

            beanMapVps = parseVps(properties.getProperty(BEANMAP_VPS, StringUtils.EMPTY));
            beanCopyVps = parseVps(properties.getProperty(BEANCOPY_VPS, StringUtils.EMPTY));
            // 解析uberspect
            String uberspectClazzName = properties.getProperty(UBERSPECTOR_IMPL, UberspectImpl.class.getName());
            uberspectClazz = Class.forName(uberspectClazzName);
        } catch (Exception e) {
            throw new BeanMappingException(e);
        }
    }

    public static boolean isBeanMappingSupportScript() {
        return isBeanMappingSupportScript;
    }

    public static List<ValueProcess> getBeanMappingVps() {
        return beanMappingVps;
    }

    public static List<ValueProcess> getBeanMapVps() {
        return beanMapVps;
    }

    public static List<ValueProcess> getBeanCopyVps() {
        return beanCopyVps;
    }

    public static Uberspect getUberspect() {
        return (Uberspect) ReflectionHelper.newInstance(uberspectClazz);
    }

    public static void setBeanMappingVps(String vps) {
        beanMappingVps = parseVps(vps);
        isBeanMappingSupportScript = StringUtils.containsIgnoreCase(vps, "script");
    }

    public static void setBeanMapVps(String vps) {
        beanMapVps = parseVps(vps);
    }

    public static void setBeanCopyVps(String vps) {
        beanCopyVps = parseVps(vps);
    }

    public static void setValueProcess(String vp, Class clazz) {
        properties.put(vp, clazz.getName());
    }

    public static void setUberspect(Class uberspectClazz) {
        BeanMappingEnvironment.uberspectClazz = uberspectClazz;
    }

    // =================== help method =======================

    private static List<ValueProcess> parseVps(String vps) {
        String[] strs = StringUtils.split(vps, ",");
        List<ValueProcess> result = new ArrayList<ValueProcess>();
        for (String str : strs) {
            result.add(initValueProcess(str));
        }

        return result;
    }

    private static ValueProcess initValueProcess(String name) {
        String className = (String) properties.get(VALUEPROCESS_PREFIX + name);
        try {
            Class clazz = Class.forName(className);
            if (ValueProcess.class.isAssignableFrom(clazz) == false) {
                throw new BeanMappingException(className + " is not assign From ValueProcess!");
            }

            return (ValueProcess) ReflectionHelper.newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new BeanMappingException(e);
        }
    }

}
