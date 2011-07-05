package com.agapple.mapping.core.builder.impl;

import com.agapple.mapping.core.builder.Builder;
import com.agapple.mapping.core.config.BeanMappingBehavior;

/**
 * {@linkplain BeanMappingBehavior}构造器
 * 
 * @author jianghang 2011-6-22 下午12:47:50
 */
public class BeanMappingBehaviorBuilder implements Builder<BeanMappingBehavior> {

    private BeanMappingBehavior behavior;

    public BeanMappingBehaviorBuilder(){
        this.behavior = new BeanMappingBehavior();
    }

    public BeanMappingBehaviorBuilder(boolean debug, boolean mappingNullValue, boolean mappingEmptyStrings,
                                      boolean trimStrings){
        this.behavior = new BeanMappingBehavior();
        behavior.setDebug(debug);
        behavior.setMappingNullValue(mappingNullValue);
        behavior.setMappingEmptyStrings(mappingEmptyStrings);
        behavior.setTrimStrings(trimStrings);
    }

    public BeanMappingBehaviorBuilder debug(boolean debug) {
        behavior.setDebug(debug);
        return this;
    }

    public BeanMappingBehaviorBuilder mappingNullValue(boolean mappingNullValue) {
        behavior.setMappingNullValue(mappingNullValue);
        return this;
    }

    public BeanMappingBehaviorBuilder mappingEmptyStrings(boolean mappingEmptyStrings) {
        behavior.setMappingEmptyStrings(mappingEmptyStrings);
        return this;
    }

    public BeanMappingBehaviorBuilder trimStrings(boolean trimStrings) {
        behavior.setTrimStrings(trimStrings);
        return this;
    }

    @Override
    public BeanMappingBehavior get() {
        return behavior;
    }

}
