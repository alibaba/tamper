package com.agapple.mapping;

import java.util.Arrays;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.process.BeanCreatorValueProcess;
import com.agapple.mapping.process.BehaviorValueProcess;
import com.agapple.mapping.process.ConvertorValueProcess;
import com.agapple.mapping.process.DebugValueProcess;
import com.agapple.mapping.process.DefaultValueValueProcess;
import com.agapple.mapping.process.ScriptValueProcess;

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

    private static final ValueProcess beanCreatorValueProcess  = new BeanCreatorValueProcess();
    private static final ValueProcess convetorValueProcess     = new ConvertorValueProcess();
    private static final ValueProcess scriptValueProcess       = new ScriptValueProcess();
    private static final ValueProcess defaultValueValueProcess = new DefaultValueValueProcess();
    private static final ValueProcess behaviorValueProcess     = new BehaviorValueProcess();
    private static final ValueProcess debugValueProcess        = new DebugValueProcess();
    private BeanMappingObject         config;                                                   // 对应的Bean Mapping配置

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
        param.setProcesses(Arrays.asList(scriptValueProcess, defaultValueValueProcess, convetorValueProcess,
                                         beanCreatorValueProcess, behaviorValueProcess, debugValueProcess));
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }

}
