package com.agapple.mapping.object;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author jianghang 2011-5-27 上午11:29:54
 */
public class TargetMappingObject {

    private int                       intValue;
    private Integer                   integerValue;
    private boolean                   start;
    private String                    targetName;
    private NestedTargetMappingObject mapping;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public NestedTargetMappingObject getMapping() {
        return mapping;
    }

    public void setMapping(NestedTargetMappingObject mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
