package com.alibaba.tamper.core.builder.impl;

import com.alibaba.tamper.core.builder.BeanMappingBuilder;
import com.alibaba.tamper.core.builder.Builder;
import com.alibaba.tamper.core.config.BeanMappingBehavior;
import com.alibaba.tamper.core.config.BeanMappingField;

/**
 * {@linkplain BeanMappingField}构造器
 * 
 * @author jianghang 2011-6-22 上午10:42:38
 */
public class BeanMappingFieldBuilder implements Builder<BeanMappingField> {

    private BeanMappingField field;

    public BeanMappingFieldBuilder(BeanMappingFieldAttributesBuilder srcField,
                                   BeanMappingFieldAttributesBuilder targetField, BeanMappingBehavior parent){
        this(srcField, targetField, false, parent);
    }

    public BeanMappingFieldBuilder(BeanMappingFieldAttributesBuilder srcField,
                                   BeanMappingFieldAttributesBuilder targetField, boolean mapping,
                                   BeanMappingBehavior parent){
        field = new BeanMappingField();
        field.setSrcField(srcField.get());
        field.setTargetField(targetField.get());
        field.setMapping(mapping);
        field.setBehavior(parent.clone());
    }

    /**
     * 是否打印debug信息
     */
    public BeanMappingFieldBuilder debug(boolean debug) {
        field.getBehavior().setDebug(debug);
        return this;
    }

    /**
     * 针对null value是否进行mapping set操作
     */
    public BeanMappingFieldBuilder mappingNullValue(boolean mappingNullValue) {
        field.getBehavior().setMappingNullValue(mappingNullValue);
        return this;
    }

    /**
     * 针对empty String value是否进行mapping set操作
     */
    public BeanMappingFieldBuilder mappingEmptyStrings(boolean mappingEmptyStrings) {
        field.getBehavior().setMappingEmptyStrings(mappingEmptyStrings);
        return this;
    }

    /**
     * 是否需要进行trim操作
     */
    public BeanMappingFieldBuilder trimStrings(boolean trimStrings) {
        field.getBehavior().setTrimStrings(trimStrings);
        return this;
    }

    /**
     * 指定使用的Convertor alias
     */
    public BeanMappingFieldBuilder convertor(String alias) {
        field.setConvertor(alias);
        return this;
    }

    /**
     * 指定使用的Convertor Class
     */
    public BeanMappingFieldBuilder convertor(Class convertorClass) {
        field.setConvertorClass(convertorClass);
        return this;
    }

    /**
     * 指定使用的script脚本
     */
    public BeanMappingFieldBuilder script(String script) {
        field.setScript(script);
        return this;
    }

    /**
     * 指定使用的defaultValue
     */
    public BeanMappingFieldBuilder defaultValue(String value) {
        field.setDefaultValue(value);
        return this;
    }

    /**
     * 设置是否需要进行递归mapping处理
     */
    public BeanMappingFieldBuilder recursiveMapping(boolean mapping) {
        field.setMapping(mapping);
        return this;
    }

    /**
     * 设置是否需要进行递归mapping处理
     */
    public BeanMappingFieldBuilder recursiveMapping(boolean mapping, String nestname) {
        field.setMapping(mapping);
        field.setNestName(nestname);
        return this;
    }

    /**
     * 设置是进行递归mapping对象的name
     */
    public BeanMappingFieldBuilder nestName(String nestname) {
        field.setNestName(nestname);
        return this;
    }

    /**
     * 设置是进行递归mapping对象
     */
    public BeanMappingFieldBuilder nestObject(BeanMappingBuilder nestBuilder) {
        field.setNestObject(nestBuilder.get());
        return this;
    }

    public BeanMappingField get() {
        return field;
    }

}
