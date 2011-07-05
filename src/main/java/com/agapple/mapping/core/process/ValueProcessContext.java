package com.agapple.mapping.core.process;

import java.io.Serializable;
import java.util.Map;

import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.helper.BatchObjectHolder;

/**
 * ValueProcess处理的上下文，允许ValueProcess基于context进行自定义的处理
 * 
 * @author jianghang 2011-5-27 下午09:01:39
 */
public class ValueProcessContext implements Serializable {

    private static final long serialVersionUID = 5690443269510349965L;
    private BeanMappingParam  param;
    private BeanMappingObject beanObject;
    private BeanMappingField  currentField;
    private BatchObjectHolder holder;
    private Map               custom;                                 // 允许自定义的context

    public ValueProcessContext(BeanMappingParam param, BeanMappingObject object, BeanMappingField field,
                               BatchObjectHolder holder){
        this(param, object, field, holder, null);
    }

    public ValueProcessContext(BeanMappingParam param, BeanMappingObject object, BeanMappingField field,
                               BatchObjectHolder holder, Map customContext){
        this.param = param;
        this.beanObject = object;
        this.currentField = field;
        this.holder = holder;
        this.custom = customContext;
    }

    public BeanMappingParam getParam() {
        return param;
    }

    public void setParam(BeanMappingParam param) {
        this.param = param;
    }

    public BeanMappingObject getBeanObject() {
        return beanObject;
    }

    public void setBeanObject(BeanMappingObject beanObject) {
        this.beanObject = beanObject;
    }

    public BeanMappingField getCurrentField() {
        return currentField;
    }

    public void setCurrentField(BeanMappingField currentField) {
        this.currentField = currentField;
    }

    public BatchObjectHolder getHolder() {
        return holder;
    }

    public void setHolder(BatchObjectHolder holder) {
        this.holder = holder;
    }

    public Map getCustom() {
        return custom;
    }

    public void setCustom(Map custom) {
        this.custom = custom;
    }

}
