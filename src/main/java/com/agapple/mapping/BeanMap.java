package com.agapple.mapping;

import java.util.HashMap;
import java.util.Map;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.BeanMappingExecutor;
import com.agapple.mapping.core.BeanMappingParam;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingObject;

/**
 * Bean<->Map操作的处理单元
 * 
 * <pre>
 * <code>
 * 使用例子：
 *  BeanMap beanMap = BeanMap.create(srcClass);
 *  Map properties = beanMap.describe(src);// 将bean的属性设置到map对象上
 *  beanMap.populate(src,properties); // 将map对象数据设置到bean的属性上
 * </code>
 * </pre>
 * 
 * TODO : 后期支持单个属性的put/get操作
 * 
 * @author jianghang 2011-6-8 上午11:11:13
 */
public class BeanMap {

    private BeanMappingObject describeConfig; // 对应的Bean Mapping配置
    private BeanMappingObject populateConfig; // 对应的Bean Mapping配置

    BeanMap(BeanMappingObject describeConfig, BeanMappingObject populateConfig){
        this.describeConfig = describeConfig;
        this.populateConfig = populateConfig;
    }

    /**
     * 创建srcClass和targetClass之间的BeanMapping操作
     */
    public static BeanMap create(Class srcClass) {
        BeanMappingObject describeConfig = BeanMappingConfigHelper.getInstance().getBeanMapObject(srcClass,
                                                                                                  HashMap.class, true);

        BeanMappingObject populateConfig = BeanMappingConfigHelper.getInstance().getBeanMapObject(HashMap.class,
                                                                                                  srcClass, true);
        return new BeanMap(describeConfig, populateConfig);
    }

    /**
     * 将bean的属性转化为Map对象
     * 
     * @param src
     * @return
     * @throws BeanMappingException
     */
    public Map describe(Object src) throws BeanMappingException {
        Map result = new HashMap();
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(src);
        param.setTargetRef(result);
        param.setConfig(this.describeConfig);
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
        return result;
    }

    /**
     * 将map的属性映射到bean对象
     * 
     * @param target
     * @param properties
     * @throws BeanMappingException
     */
    public void populate(Object target, Map properties) throws BeanMappingException {
        BeanMappingParam param = new BeanMappingParam();
        param.setSrcRef(properties);
        param.setTargetRef(target);
        param.setConfig(this.populateConfig);
        // 执行mapping处理
        BeanMappingExecutor.execute(param);
    }
}
