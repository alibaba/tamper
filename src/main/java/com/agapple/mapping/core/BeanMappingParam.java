package com.agapple.mapping.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.ValueProcess;

/**
 * bean mapping传递的参数
 * 
 * @author jianghang 2011-5-26 下午06:39:29
 */
public class BeanMappingParam implements Serializable {

    private static final long  serialVersionUID   = 2371233083866029415L;
    private Object             srcRef;                                   // 待转化src
    private Object             targetRef;                                // 转化的目标dest
    private BeanMappingObject  config;                                   // bean mapping相关配置

    // =========================== ValueProcess 扩展参数==============================
    private List<ValueProcess> processes;                                // 自定义的valueProcess
    private Map                customValueContext = new HashMap();       // 自定义的valueProcess上下文处理

    public Object getSrcRef() {
        return srcRef;
    }

    public void setSrcRef(Object srcRef) {
        this.srcRef = srcRef;
    }

    public Object getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(Object targetRef) {
        this.targetRef = targetRef;
    }

    public BeanMappingObject getConfig() {
        return config;
    }

    public void setConfig(BeanMappingObject config) {
        this.config = config;
    }

    public Map getCustomValueContext() {
        return customValueContext;
    }

    public void setCustomValueContext(Map customValueContext) {
        this.customValueContext = customValueContext;
    }

    public List<ValueProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ValueProcess> processes) {
        this.processes = processes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
