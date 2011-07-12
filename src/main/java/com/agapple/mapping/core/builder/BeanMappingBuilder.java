package com.agapple.mapping.core.builder;

import com.agapple.mapping.core.builder.impl.BeanMappingBehaviorBuilder;
import com.agapple.mapping.core.builder.impl.BeanMappingFieldAttributesBuilder;
import com.agapple.mapping.core.builder.impl.BeanMappingFieldBuilder;
import com.agapple.mapping.core.builder.impl.BeanMappingObjectBuilder;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingConfigRespository;
import com.agapple.mapping.core.config.BeanMappingObject;

/**
 * mapping对象的构造器, 可直接注册到{@linkplain BeanMappingConfigRespository}
 * 
 * <pre>
 * BeanMappingConfigRespository.register(BeanMappingBuilder);
 * </pre>
 * 
 * @author jianghang 2011-6-22 上午10:31:35
 */
public class BeanMappingBuilder implements Builder<BeanMappingObject> {

    private BeanMappingObject   object;
    private BeanMappingBehavior global = BeanMappingConfigHelper.getInstance().getGlobalBehavior();

    public BeanMappingBuilder(){
        configure();
    }

    protected void configure() {
        // 需要客户端实现
    }

    public BeanMappingObjectBuilder mapping(Class srcClass, Class targetClass) {
        BeanMappingObjectBuilder builder = new BeanMappingObjectBuilder(srcClass, targetClass, global);
        object = builder.get();
        return builder;
    }

    public BeanMappingFieldBuilder fields(BeanMappingFieldAttributesBuilder srcFieldBuilder,
                                          BeanMappingFieldAttributesBuilder targetFieldBuilder) {
        BeanMappingFieldBuilder fieldCriterion = new BeanMappingFieldBuilder(srcFieldBuilder, targetFieldBuilder,
                                                                             this.object.getBehavior());
        this.object.addBeanField(fieldCriterion.get());
        return fieldCriterion;
    }

    public BeanMappingBehaviorBuilder behavior() {
        BeanMappingBehaviorBuilder builder = new BeanMappingBehaviorBuilder();
        global = builder.get();
        return builder;

    }

    public BeanMappingBehaviorBuilder behavior(boolean debug, boolean mappingNullValue, boolean mappingEmptyStrings,
                                               boolean trimStrings) {
        BeanMappingBehaviorBuilder builder = new BeanMappingBehaviorBuilder(debug, mappingNullValue,
                                                                            mappingEmptyStrings, trimStrings);
        global = builder.get();
        return builder;

    }

    public BeanMappingFieldAttributesBuilder srcField(String name) {
        return new BeanMappingFieldAttributesBuilder(name);
    }

    public BeanMappingFieldAttributesBuilder srcField(String name, Class clazz) {
        return new BeanMappingFieldAttributesBuilder(name, clazz);
    }

    public BeanMappingFieldAttributesBuilder targetField(String name) {
        return new BeanMappingFieldAttributesBuilder(name);
    }

    public BeanMappingFieldAttributesBuilder targetField(String name, Class clazz) {
        return new BeanMappingFieldAttributesBuilder(name, clazz);
    }

    @Override
    public BeanMappingObject get() {
        return object;
    }

}
