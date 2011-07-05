package com.agapple.mapping.core.builder.impl;

import com.agapple.mapping.core.builder.Builder;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingObject;

/**
 * {@linkplain BeanMappingObject}构造器
 * 
 * @author jianghang 2011-6-22 上午10:10:30
 */
public class BeanMappingObjectBuilder implements Builder<BeanMappingObject> {

    private static final long serialVersionUID = 358128791476093909L;

    private BeanMappingObject object;

    public BeanMappingObjectBuilder(Class srcClass, Class targetClass, BeanMappingBehavior parent){
        object = new BeanMappingObject();
        object.setSrcClass(srcClass);
        object.setTargetClass(targetClass);
        object.setBehavior(parent.clone());
    }

    /**
     * 是否打印debug信息
     */
    public BeanMappingObjectBuilder debug(boolean debug) {
        object.getBehavior().setDebug(debug);
        return this;
    }

    /**
     * 针对null value是否进行mapping set操作
     */
    public BeanMappingObjectBuilder mappingNullValue(boolean mappingNullValue) {
        object.getBehavior().setMappingNullValue(mappingNullValue);
        return this;
    }

    /**
     * 针对empty String value是否进行mapping set操作
     */
    public BeanMappingObjectBuilder mappingEmptyStrings(boolean mappingEmptyStrings) {
        object.getBehavior().setMappingEmptyStrings(mappingEmptyStrings);
        return this;
    }

    /**
     * 是否需要进行trim操作
     */
    public BeanMappingObjectBuilder trimStrings(boolean trimStrings) {
        object.getBehavior().setTrimStrings(trimStrings);
        return this;
    }

    /**
     * 设置是否进行batch优化，默认为true
     */
    public BeanMappingObjectBuilder batch(boolean isBatch) {
        object.setBatch(isBatch);
        return this;
    }

    /**
     * 设置是否允许反向mapping，默认为false
     */
    public BeanMappingObjectBuilder reversable(boolean reversable) {
        object.setReversable(reversable);
        return this;
    }

    /**
     * 指定对应的key,用于script中属性获取
     */
    public BeanMappingObjectBuilder keys(String srcKey, String targetKey) {
        object.setSrcKey(srcKey);
        object.setTargetKey(targetKey);
        return this;
    }

    public BeanMappingObject get() {
        return object;
    }
}
