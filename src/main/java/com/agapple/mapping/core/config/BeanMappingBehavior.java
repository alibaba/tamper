package com.agapple.mapping.core.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * BeanMapping的一些mapping行为参数
 * 
 * @author jianghang 2011-6-13 下午10:27:07
 */
public class BeanMappingBehavior {

    private boolean debug               = false; // 是否打印debug信息
    private boolean mappingNullValue    = true; // 针对nullValue是否需要进行处理
    private boolean mappingEmptyStrings = true; // 针对Empty String是否需要进行处理
    private boolean trimStrings         = false; // 针对String进行trim处理

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isMappingNullValue() {
        return mappingNullValue;
    }

    public void setMappingNullValue(boolean mappingNullValue) {
        this.mappingNullValue = mappingNullValue;
    }

    public boolean isMappingEmptyStrings() {
        return mappingEmptyStrings;
    }

    public void setMappingEmptyStrings(boolean mappingEmptyStrings) {
        this.mappingEmptyStrings = mappingEmptyStrings;
    }

    public boolean isTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public BeanMappingBehavior clone() {
        BeanMappingBehavior behavior = new BeanMappingBehavior();
        behavior.setDebug(this.debug);
        behavior.setMappingEmptyStrings(this.mappingEmptyStrings);
        behavior.setMappingNullValue(this.mappingNullValue);
        behavior.setTrimStrings(this.trimStrings);
        return behavior;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
