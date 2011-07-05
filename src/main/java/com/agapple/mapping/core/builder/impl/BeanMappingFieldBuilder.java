package com.agapple.mapping.core.builder.impl;

import com.agapple.mapping.core.builder.Builder;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingField;

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

    public BeanMappingField get() {
        return field;
    }

}
