package com.agapple.mapping;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingEnvironment;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.process.script.ScriptHelper;

/**
 * Bean Mapping操作的处理单元
 * 
 * <pre>
 * <code>
 * 使用例子：
 *  BeanMapping beanMapping = BeanMapping.create(srcClass,targetClass);
 *  beanMapping.mapping(src,target);// 将src的属性mapping到target
 *  
 *  注意：srcClass/targetClass的映射关系必须实现通过{@linkplain BeanMappingConfigHelper}的registerConfig方法注册mapping配置
 * </code>
 * </pre>
 * 
 * @author jianghang 2011-6-8 上午11:10:24
 */
public class BeanMapping {

    private BeanMappingObject config; // 对应的Bean Mapping配置

    BeanMapping(BeanMappingObject config){
        this.config = config;
    }

    public BeanMapping(BeanMappingBuilder builder){
        this.config = builder.get();
    }

    /**
     * 创建srcClass和targetClass之间的BeanMapping操作
     */
    public static BeanMapping create(Class srcClass, Class targetClass) {
        BeanMappingObject config = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass);
        if (config == null) {
            throw new BeanMappingException("can not found mapping config for srcClass[" + srcClass.toString()
                                           + "] targetClass[" + targetClass + "]");
        }

        return new BeanMapping(config);
    }

    /**
     * 根据定义的bean-mapping配置进行对象属性的mapping拷贝 , 允许自定义{@linkplain ValueProcess} {@linkplain SetValueProcess}
     * 
     * @param src
     * @param target
     * @throws BeanMappingException
     */
    public void mapping(Object src, Object target) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(this.config);
        param.setProcesses(BeanMappingEnvironment.getBeanMappingVps());
        // 执行mapping处理
        try {
            BeanMappingExecutor.execute(param);
        } finally {
            ScriptHelper.getInstance().getScriptExecutor().disposeFunctions();// 清空记录
        }
    }

}
