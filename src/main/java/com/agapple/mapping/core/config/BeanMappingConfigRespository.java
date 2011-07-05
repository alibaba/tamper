package com.agapple.mapping.core.config;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.parse.BeanMappingParser;

/**
 * BeanMappingObject对应的仓库，解析一次Object后会进行cache
 * 
 * @author jianghang 2011-5-26 下午07:57:33
 */
public class BeanMappingConfigRespository {

    private static final String            SEPERATOR = ":";
    private Map<String, BeanMappingObject> mappings  = new ConcurrentHashMap<String, BeanMappingObject>(10);

    /**
     * 根据class查找对应的{@linkplain BeanMappingObject}
     */
    public BeanMappingObject getBeanMappingObject(Class src, Class target) {
        return mappings.get(mapperObjectName(src, target));
    }

    /**
     * 直接注册一个解析好的{@linkplain BeanMappingBuilder}
     */
    public void register(BeanMappingBuilder builder) {
        if (builder != null) {
            BeanMappingObject object = builder.get();
            mappings.put(mapperObjectName(object.getSrcClass(), object.getTargetClass()), object);
        }
    }

    /**
     * 直接注册一个解析号的{@linkplain BeanMappingObject}
     */
    public void register(BeanMappingObject object) {
        if (object != null) {
            mappings.put(mapperObjectName(object.getSrcClass(), object.getTargetClass()), object);
        }
    }

    /**
     * 直接注册为默认mapping
     * 
     * @param src
     * @param dest
     */
    public void register(Class src, Class target) {
        List<BeanMappingObject> objects = BeanMappingParser.parseMapping(src, target);
        for (BeanMappingObject object : objects) {
            mappings.put(mapperObjectName(object.getSrcClass(), object.getTargetClass()), object);
        }
    }

    /**
     * 直接注册bean和map的mapping关系
     */
    public void registerMap(Class src) {
        List<BeanMappingObject> objects = BeanMappingParser.parseMapMapping(src);
        for (BeanMappingObject object : objects) {
            mappings.put(mapperObjectName(object.getSrcClass(), object.getTargetClass()), object);
        }
    }

    /**
     * 注册beanMapping配置的流
     */
    public void registerConfig(InputStream in) {
        List<BeanMappingObject> objects = BeanMappingParser.parseMapping(in);
        for (BeanMappingObject object : objects) {
            mappings.put(mapperObjectName(object.getSrcClass(), object.getTargetClass()), object);
        }
    }

    // ==========================helper method =========================

    private String mapperObjectName(Class src, Class dest) {
        String name1 = src.getName();
        String name2 = dest.getName();

        return name1 + SEPERATOR + name2;
    }

}
