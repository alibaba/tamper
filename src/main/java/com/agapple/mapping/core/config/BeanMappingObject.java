package com.agapple.mapping.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.agapple.mapping.core.introspect.BatchExecutor;

/**
 * 解析完成后的一个BeanMapping配置对象
 * 
 * @author jianghang 2011-5-26 下午07:16:10
 */
public class BeanMappingObject implements Serializable {

    private static final long      serialVersionUID = 9099474060890980056L;
    private String                 srcKey           = "src";                            // format上下文中src key，默认:src
    private Class                  srcClass         = null;                             // mapping的原始class
    private String                 targetKey        = "target";                         // format上下文中targetkey,默认:target
    private Class                  targetClass      = null;                             // mapping的目标class
    private boolean                reversable       = true;                             // 原始和目标的mapping是否可逆，如果有自定义的convertor,强制修改为不可逆
    private List<BeanMappingField> beanFields       = new ArrayList<BeanMappingField>(); // 具体字段的mapping配置
    private boolean                batch            = false;                            // 优化参数，是否开启批量处理操作.
    private BeanMappingBehavior    behavior         = null;                             // mapping的处理行为参数

    // ======================= 内部数据，外部请勿直接操作 ==================
    private BatchExecutor          getBatchExecutor = null;                             // get操作的batch执行引擎
    private BatchExecutor          setBatchExecutor = null;                             // set操作的batch执行引擎

    public Class getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(Class srcClass) {
        this.srcClass = srcClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public boolean isReversable() {
        return reversable;
    }

    public void setReversable(boolean reversable) {
        this.reversable = reversable;
    }

    public List<BeanMappingField> getBeanFields() {
        return beanFields;
    }

    public void setBeanFields(List<BeanMappingField> beanFields) {
        this.beanFields = beanFields;
    }

    public void addBeanField(BeanMappingField beanField) {
        if (beanFields == null) {
            beanFields = new ArrayList<BeanMappingField>();
        }

        beanFields.add(beanField);
    }

    public String getSrcKey() {
        return srcKey;
    }

    public void setSrcKey(String srcKey) {
        this.srcKey = srcKey;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public BatchExecutor getGetBatchExecutor() {
        return getBatchExecutor;
    }

    public void setGetBatchExecutor(BatchExecutor getBatchExecutor) {
        this.getBatchExecutor = getBatchExecutor;
    }

    public BatchExecutor getSetBatchExecutor() {
        return setBatchExecutor;
    }

    public void setSetBatchExecutor(BatchExecutor setBatchExecutor) {
        this.setBatchExecutor = setBatchExecutor;
    }

    public BeanMappingBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(BeanMappingBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
