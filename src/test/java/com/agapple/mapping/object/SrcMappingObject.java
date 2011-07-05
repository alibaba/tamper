package com.agapple.mapping.object;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author jianghang 2011-5-27 上午11:29:36
 */
public class SrcMappingObject {

    private int                    intValue;
    private Integer                integerValue;
    private boolean                start;
    private String                 name;
    private NestedSrcMappingObject mapping;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NestedSrcMappingObject getMapping() {
        return mapping;
    }

    public void setMapping(NestedSrcMappingObject mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
