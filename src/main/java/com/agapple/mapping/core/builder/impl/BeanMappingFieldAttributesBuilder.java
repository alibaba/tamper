package com.agapple.mapping.core.builder.impl;

import java.util.Arrays;

import com.agapple.mapping.core.builder.Builder;
import com.agapple.mapping.core.config.BeanMappingFieldAttributes;

/**
 * {@linkplain BeanMappingFieldAttributes} 构造器
 * 
 * @author jianghang 2011-6-22 上午10:44:13
 */
public class BeanMappingFieldAttributesBuilder implements Builder<BeanMappingFieldAttributes> {

    private BeanMappingFieldAttributes attributes;

    public BeanMappingFieldAttributesBuilder(String name){
        this(name, null);
    }

    public BeanMappingFieldAttributesBuilder(String name, Class clazz){
        attributes = new BeanMappingFieldAttributes();
        attributes.setName(name);
        attributes.setClazz(clazz);
    }

    /**
     * 设置属性对应的class对象
     */
    public BeanMappingFieldAttributesBuilder clazz(Class clazz) {
        attributes.setClazz(clazz);
        return this;
    }

    /**
     * 设置查找对应属性的目标class，默认会以mapping中定义的class进行查找
     * <p/>
     * 针对存在子父属性进行mapping时,可设置此locatorClass进行区分
     */
    public BeanMappingFieldAttributesBuilder locatorClass(Class locatorClass) {
        attributes.setLocatorClass(locatorClass);
        return this;
    }

    /**
     * 针对Collection的属性,可以设置嵌套的内部对象类型
     * 
     * <pre>
     * <strong>针对嵌套处理说明:</strong> 比如List&lt;Set&lt;List&lt;Model&gt;&gt;&gt;,
     * 此时对应的componentClasses存在3个Class,分别为Set.class(第一层),List.class(第二层),Model.class(第三层)
     * 
     * </pre>
     */
    public BeanMappingFieldAttributesBuilder componentClasses(Class... componentClasses) {
        attributes.setComponentClasses(Arrays.asList(componentClasses));
        return this;
    }

    public BeanMappingFieldAttributes get() {
        return attributes;
    }
}
