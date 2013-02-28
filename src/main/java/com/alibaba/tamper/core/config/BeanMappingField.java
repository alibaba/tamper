package com.alibaba.tamper.core.config;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.tamper.core.introspect.GetExecutor;
import com.alibaba.tamper.core.introspect.SetExecutor;
import com.alibaba.tamper.process.convertor.Convertor;

/**
 * 解析完成后的一个BeanMapping的field配置对象
 * 
 * <pre>
 * changeLog 
 *  v1.0.2 
 *      两种条件满足时可以触发使用nestObject对象
 *      a. mapping=true，需要进行递归映射，会读取nestObject定义的映射规则
 *      b. 针对field为collection类型，需要进行循环映射，会读取nestObject定义的映射规则
 * </pre>
 * 
 * @author jianghang 2011-5-23 下午04:25:06
 */
public class BeanMappingField implements Serializable {

    private static final long          serialVersionUID = 3673414855182044438L;
    private BeanMappingFieldAttributes srcField         = new BeanMappingFieldAttributes(); // 源属性配置信息
    private BeanMappingFieldAttributes targetField      = new BeanMappingFieldAttributes(); // 目标属性配置信息
    private String                     defaultValue     = null;                            // 默认值,配置文件中定义的字符串
    private String                     convertor        = null;                            // 自定义conveterName
    private Class                      convertorClass   = null;                            // 指定convertor的class
    private String                     script           = null;                            // format script字符串
    private boolean                    mapping          = false;                           // 是否深度递归mapping
    private String                     nestName         = null;                            // 定义嵌套映射的名字
    private BeanMappingBehavior        behavior         = null;                            // mapping的处理行为参数

    // ======================= 内部数据，外部请勿直接操作 ==================
    private BeanMappingObject          nestObject       = null;                            // 定义嵌套的配置
    private Convertor                  convertorRef     = null;                            // convertor对应的对象引用
    private GetExecutor                getExecutor      = null;                            // get操作的执行引擎
    private SetExecutor                setExecutor      = null;                            // set操作的执行引擎

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getConvertor() {
        return convertor;
    }

    public void setConvertor(String convertor) {
        this.convertor = convertor;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean isMapping() {
        return mapping;
    }

    public void setMapping(boolean mapping) {
        this.mapping = mapping;
    }

    public Class getConvertorClass() {
        return convertorClass;
    }

    public void setConvertorClass(Class convertorClass) {
        this.convertorClass = convertorClass;
    }

    public Convertor getConvertorRef() {
        return convertorRef;
    }

    public void setConvertorRef(Convertor convertorRef) {
        this.convertorRef = convertorRef;
    }

    public BeanMappingBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(BeanMappingBehavior behavior) {
        this.behavior = behavior;
    }

    public GetExecutor getGetExecutor() {
        return getExecutor;
    }

    public void setGetExecutor(GetExecutor getExecutor) {
        this.getExecutor = getExecutor;
    }

    public SetExecutor getSetExecutor() {
        return setExecutor;
    }

    public void setSetExecutor(SetExecutor setExecutor) {
        this.setExecutor = setExecutor;
    }

    public BeanMappingFieldAttributes getSrcField() {
        return srcField;
    }

    public void setSrcField(BeanMappingFieldAttributes srcField) {
        this.srcField = srcField;
    }

    public BeanMappingFieldAttributes getTargetField() {
        return targetField;
    }

    public void setTargetField(BeanMappingFieldAttributes targetField) {
        this.targetField = targetField;
    }

    public BeanMappingObject getNestObject() {
        return nestObject;
    }

    public void setNestObject(BeanMappingObject nestObject) {
        this.nestObject = nestObject;
    }

    public String getNestName() {
        return nestName;
    }

    public void setNestName(String nestName) {
        this.nestName = nestName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
