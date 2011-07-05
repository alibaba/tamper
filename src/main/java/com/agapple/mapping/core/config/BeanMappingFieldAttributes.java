package com.agapple.mapping.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * mapping配置的field信息配置
 * 
 * @author jianghang 2011-6-21 上午11:32:56
 */
public class BeanMappingFieldAttributes implements Serializable {

    private static final long serialVersionUID = -4833554213883478310L;
    private Class             locatorClass;                            // 指定在该LocatorClass，查找对应的目标数据name的属性方法，解决继承属性
    private String            name;                                    // 源数据的name
    private Class             clazz;                                   // 源数据的class

    // 允许出现嵌套数组，比如List<Set<List<Model>>>,
    // 此时对应的componentClasses存在3个Class,分别为Set.class(第一层),List.class(第二层),Model.class(第三层)
    private List<Class>       componentClasses;                        // 指定Collection/Array的ComponentType

    public Class getLocatorClass() {
        return locatorClass;
    }

    public void setLocatorClass(Class locatorClass) {
        this.locatorClass = locatorClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Class> getComponentClasses() {
        return componentClasses;
    }

    public void setComponentClasses(List<Class> componentClasses) {
        this.componentClasses = componentClasses;
    }

    public void addComponentClasses(Class componentClass) {
        if (componentClasses == null) {
            componentClasses = new ArrayList<Class>();
        }

        componentClasses.add(componentClass);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
