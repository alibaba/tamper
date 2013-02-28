package com.alibaba.tamper;

import java.util.List;

import com.alibaba.tamper.core.BeanMappingException;
import com.alibaba.tamper.core.BeanMappingExecutor;
import com.alibaba.tamper.core.BeanMappingParam;
import com.alibaba.tamper.core.builder.BeanMappingBuilder;
import com.alibaba.tamper.core.config.BeanMappingConfigHelper;
import com.alibaba.tamper.core.config.BeanMappingEnvironment;
import com.alibaba.tamper.core.config.BeanMappingObject;
import com.alibaba.tamper.core.helper.ContextObjectHolder;
import com.alibaba.tamper.core.process.ValueProcess;
import com.alibaba.tamper.process.script.ScriptHelper;

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
 * 
 * changelog
 *  v1.0.2 
 *      mapping执行会有context的概念，缓存一下当前的一些执行信息
 * </pre>
 * 
 * @author jianghang 2011-6-8 上午11:10:24
 */
public class BeanMapping {

    private BeanMappingObject config; // 对应的Bean Mapping配置

    public BeanMapping(BeanMappingObject config){
        this.config = config;
    }

    public BeanMapping(BeanMappingBuilder builder){
        this.config = builder.get();
    }

    /**
     * 创建指定name的BeanMapping操作
     */
    public static BeanMapping create(String mappingName) {
        BeanMappingObject config = BeanMappingConfigHelper.getInstance().getBeanMappingObject(mappingName);
        if (config == null) {
            throw new BeanMappingException("can not found mapping config for name[" + mappingName + "]");
        }

        return new BeanMapping(config);
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
        boolean first = ContextObjectHolder.getInstance().enter();
        boolean isBeanMappingSupportScript = BeanMappingEnvironment.isBeanMappingSupportScript();
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(target);
        param.setConfig(this.config);
        List<ValueProcess> vps = BeanMappingEnvironment.getBeanMappingVps();
        param.setProcesses(vps);

        // 执行mapping处理
        try {
            BeanMappingExecutor.execute(param);
        } finally {
            if (first) {// 第一个进入的负责清空数据，可能存在递归调用处理
                ContextObjectHolder.getInstance().clear();
                if (isBeanMappingSupportScript) {
                    ScriptHelper.getInstance().getScriptExecutor().disposeFunctions();// 清空记录
                }
            }
        }
    }
}
