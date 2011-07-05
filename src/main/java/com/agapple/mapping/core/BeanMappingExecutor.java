package com.agapple.mapping.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.helper.BatchObjectHolder;
import com.agapple.mapping.core.introspect.BatchExecutor;
import com.agapple.mapping.core.introspect.GetExecutor;
import com.agapple.mapping.core.introspect.MapSetExecutor;
import com.agapple.mapping.core.introspect.SetExecutor;
import com.agapple.mapping.core.introspect.Uberspector;
import com.agapple.mapping.core.process.ValueProcessContext;
import com.agapple.mapping.core.process.ValueProcessInvocation;

/**
 * Bean mapping具体的执行器
 * 
 * @author jianghang 2011-5-26 下午04:27:35
 */
public class BeanMappingExecutor {

    private final static Logger logger = LoggerFactory.getLogger(BeanMappingExecutor.class);

    /**
     * 根据传递的param，进行mapping处理
     */
    public static void execute(BeanMappingParam param) {
        BeanMappingObject config = param.getConfig();
        boolean isDebug = config.getBehavior().isDebug();
        if (isDebug && logger.isDebugEnabled()) {
            logger.debug("====================== start mapping \n\t srcClass["
                         + param.getSrcRef().getClass().toString() + "] \n\t targetClass["
                         + param.getTargetRef().getClass().toString() + "]");
        }
        BatchObjectHolder holder = null;
        BatchExecutor getBatchExecutor = null;
        BatchExecutor setBatchExecutor = null;
        if (config.isBatch()) { // 执行一次batch get操作，注意batch的获取操作需要放置在doFieldMapping/doBeanMapping之前
            getBatchExecutor = getGetBatchExecutor(param, config);
            setBatchExecutor = getSetBatchExecutor(param, config);
        }
        if (config.isBatch() && getBatchExecutor != null) { // 执行一次batch get操作
            Object[] batchValues = getBatchExecutor.gets(param.getSrcRef());
            holder = new BatchObjectHolder(batchValues);
        }
        List<BeanMappingField> beanFields = config.getBeanFields();
        for (int i = 0, size = beanFields.size(); i < size; i++) {
            BeanMappingField beanField = beanFields.get(i);
            if (beanField.isMapping()) {
                doBeanMapping(param, beanField, holder);
            } else {
                doFieldMapping(param, beanField, holder);
            }
        }
        if (config.isBatch() && setBatchExecutor != null && holder != null) { // 执行一次batch set操作
            setBatchExecutor.sets(param.getTargetRef(), holder.getBatchValues());
        }

        if (isDebug && logger.isDebugEnabled()) {
            logger.debug("====================== end mapping");
        }
    }

    /**
     * 获取set操作的BatchExecutor
     */
    private static BatchExecutor getSetBatchExecutor(BeanMappingParam param, BeanMappingObject config) {
        BatchExecutor executor = config.getSetBatchExecutor();
        if (executor != null) { // 如果已经生成，则直接返回
            return executor;
        }

        if (canBatch(config.getBehavior()) == false) {
            config.setBatch(false);
            return null;
        }

        // 处理target操作数据搜集
        List<String> targetFields = new ArrayList<String>();
        List<Class> targetArgs = new ArrayList<Class>();
        Class locatorClass = param.getTargetRef().getClass(); // 检查一下locatorClass
        for (BeanMappingField beanField : config.getBeanFields()) {
            String targetField = beanField.getTargetField().getName();
            Class targetArg = beanField.getTargetField().getClazz();
            if (StringUtils.isEmpty(targetField) || targetArg == null) {
                return null; // 直接不予处理
            }

            Class selfLocatorClass = beanField.getTargetField().getLocatorClass();
            if (selfLocatorClass != null && selfLocatorClass != locatorClass) {
                config.setBatch(false);// 直接改写为false，发现locatorClass存在于不同的class
            }

            if (canBatch(beanField.getBehavior()) == false) {
                config.setBatch(false);
                return null;
            }
            // 搜集信息
            targetFields.add(targetField);
            targetArgs.add(targetArg);
        }
        // 生成下target批量处理器
        executor = Uberspector.getInstance().getBatchExecutor(locatorClass,
                                                              targetFields.toArray(new String[targetFields.size()]),
                                                              targetArgs.toArray(new Class[targetArgs.size()]));
        if (config.getBehavior().isDebug() && logger.isDebugEnabled()) {
            logger.debug("TargetClass[" + param.getTargetRef().getClass() + "]SetBatchExecutor is init");
        }
        config.setSetBatchExecutor(executor);
        return executor;
    }

    /**
     * 获取get操作的BatchExecutor
     */
    private static BatchExecutor getGetBatchExecutor(BeanMappingParam param, BeanMappingObject config) {
        BatchExecutor executor = config.getGetBatchExecutor();
        if (executor != null) { // 如果已经生成，则直接返回
            return executor;
        }

        if (canBatch(config.getBehavior()) == false) {
            config.setBatch(false);
            return null;
        }

        List<String> srcFields = new ArrayList<String>();
        List<Class> srcArgs = new ArrayList<Class>();
        // 处理src操作数据搜集
        Class locatorClass = param.getSrcRef().getClass();
        for (BeanMappingField beanField : config.getBeanFields()) {
            String srcField = beanField.getSrcField().getName();
            Class srcArg = beanField.getSrcField().getClazz();
            if (StringUtils.isEmpty(srcField) || srcArg == null) {
                return null; // 直接不予处理
            }

            Class selfLocatorClass = beanField.getSrcField().getLocatorClass();
            if (selfLocatorClass != null && selfLocatorClass != locatorClass) {
                config.setBatch(false);// 直接改写为false，发现locatorClass存在于不同的class
            }

            if (canBatch(beanField.getBehavior()) == false) {
                config.setBatch(false);
                return null;
            }

            // 搜集信息
            srcFields.add(srcField);
            srcArgs.add(srcArg);
        }
        // 生成下src批量处理器
        executor = Uberspector.getInstance().getBatchExecutor(locatorClass,
                                                              srcFields.toArray(new String[srcFields.size()]),
                                                              srcArgs.toArray(new Class[srcArgs.size()]));
        if (config.getBehavior().isDebug() && logger.isDebugEnabled()) {
            logger.debug("SrcClass[" + param.getSrcRef().getClass() + "]GetBatchExecutor is init");
        }
        config.setGetBatchExecutor(executor);
        return executor;
    }

    /**
     * 处理下模型的field的mapping动作
     */
    private static void doFieldMapping(BeanMappingParam param, BeanMappingField beanField, BatchObjectHolder holder) {
        // 定义valueContext
        ValueProcessContext valueContext = new ValueProcessContext(param, param.getConfig(), beanField, holder,
                                                                   param.getCustomValueContext());
        // 设置getExecutor
        GetExecutor getExecutor = beanField.getGetExecutor();// 优先从beanField里取
        if (getExecutor == null && StringUtils.isNotEmpty(beanField.getSrcField().getName())) {// 如果不为空,可能存在script
            Class locatorClass = beanField.getSrcField().getLocatorClass();// 从locatorClass中获取
            if (locatorClass == null) {
                locatorClass = param.getSrcRef().getClass();
                beanField.getSrcField().setLocatorClass(locatorClass);
            }
            getExecutor = Uberspector.getInstance().getGetExecutor(locatorClass, beanField.getSrcField().getName());
            beanField.setGetExecutor(getExecutor);
        }
        // 设置setExecutor
        SetExecutor setExecutor = beanField.getSetExecutor();// 优先从beanField里取
        if (setExecutor == null && StringUtils.isNotEmpty(beanField.getTargetField().getName())) {
            Class locatorClass = beanField.getTargetField().getLocatorClass();// 从locatorClass中获取
            if (locatorClass == null) {
                locatorClass = param.getTargetRef().getClass();
                beanField.getTargetField().setLocatorClass(locatorClass);
            }
            setExecutor = Uberspector.getInstance().getSetExecutor(locatorClass, beanField.getTargetField().getName(),
                                                                   beanField.getTargetField().getClazz());
            beanField.setSetExecutor(setExecutor);

        }

        // 获取get结果
        ValueProcessInvocation invocation = new ValueProcessInvocation(getExecutor, setExecutor, valueContext,
                                                                       param.getProcesses());
        Object getResult = invocation.getInitialValue();
        // 设置下srcClass
        Class getResultClass = (getResult != null) ? getResult.getClass() : null;
        if (getExecutor != null && beanField.getSrcField().getClazz() == null) {
            beanField.getSrcField().setClazz(
                                             Uberspector.getInstance().getGetClass(getExecutor,
                                                                                   param.getSrcRef().getClass(),
                                                                                   getResultClass));
        }

        // 设置下targetClass
        if (setExecutor != null && beanField.getTargetField().getClazz() == null) {
            beanField.getTargetField().setClazz(
                                                Uberspector.getInstance().getSetClass(setExecutor,
                                                                                      param.getTargetRef().getClass(),
                                                                                      getResultClass));
        }
        // 开始ValueProcess流程
        invocation.proceed(getResult);
    }

    /**
     * 处理下子模型的嵌套mapping动作
     */
    private static void doBeanMapping(BeanMappingParam param, BeanMappingField beanField, BatchObjectHolder holder) {
        // 定义valueContext
        ValueProcessContext valueContext = new ValueProcessContext(param, param.getConfig(), beanField, holder,
                                                                   param.getCustomValueContext());
        // 检查一下targetClass是否有设置，针对bean对象有效
        // 如果目标对象是map，需要客户端强制设定targetClass
        GetExecutor getExecutor = beanField.getGetExecutor();
        if (getExecutor == null && StringUtils.isNotEmpty(beanField.getSrcField().getName())) {// 可能存在为空
            Class locatorClass = beanField.getSrcField().getLocatorClass();// 从locatorClass中获取
            if (locatorClass == null) {
                locatorClass = param.getSrcRef().getClass();
                beanField.getSrcField().setLocatorClass(locatorClass);
            }
            getExecutor = Uberspector.getInstance().getGetExecutor(locatorClass, beanField.getSrcField().getName());
            beanField.setGetExecutor(getExecutor);
        }

        SetExecutor setExecutor = beanField.getSetExecutor();
        if (setExecutor == null && StringUtils.isNotEmpty(beanField.getTargetField().getName())) {// 可能存在为空
            Class locatorClass = beanField.getTargetField().getLocatorClass();// 从locatorClass中获取
            if (locatorClass == null) {
                locatorClass = param.getTargetRef().getClass();
                beanField.getTargetField().setLocatorClass(locatorClass);
            }
            setExecutor = Uberspector.getInstance().getSetExecutor(locatorClass, beanField.getTargetField().getName(),
                                                                   beanField.getTargetField().getClazz());
            beanField.setSetExecutor(setExecutor);
        }

        // 获取新的srcRef
        // 获取get结果
        ValueProcessInvocation invocation = new ValueProcessInvocation(getExecutor, setExecutor, valueContext,
                                                                       param.getProcesses());
        Object srcRef = invocation.getInitialValue();

        // 设置下srcClass
        Class getResultClass = (srcRef != null) ? srcRef.getClass() : null;
        // 设置下srcClass
        if (getExecutor != null && beanField.getSrcField().getClazz() == null) {
            beanField.getSrcField().setClazz(
                                             Uberspector.getInstance().getGetClass(getExecutor,
                                                                                   param.getSrcRef().getClass(),
                                                                                   getResultClass));
        }

        if (setExecutor != null && beanField.getTargetField().getClazz() == null) {
            beanField.getTargetField().setClazz(
                                                Uberspector.getInstance().getSetClass(setExecutor,
                                                                                      param.getTargetRef().getClass(),
                                                                                      getResultClass));
            if (setExecutor instanceof MapSetExecutor) {
                beanField.getTargetField().setClazz(HashMap.class);// 注意这里强制传递为HashMap.class
            }
        }

        // 执行set,反射构造一个子Model
        // 如果嵌套对象为null，则直接略过该对象处理，目标对象也为null,此时srcRef可能为null
        Object value = invocation.proceed(srcRef); // 在目标节点对象上，创建一个子节点
        if (srcRef == null && value == null) {
            return; // 如果为null，则不做递归处理
        }

        if (beanField.getSrcField().getClazz() == null || beanField.getTargetField().getClazz() == null) {
            throw new BeanMappingException("srcClass or targetClass is null , " + beanField.toString());
        }
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(
                                                                                              beanField.getSrcField().getClazz(),
                                                                                              beanField.getTargetField().getClazz());
        if (object == null) {
            throw new BeanMappingException("no bean mapping config for " + beanField.toString());
        }
        BeanMappingParam newParam = new BeanMappingParam();
        newParam.setTargetRef(value);// 为新创建的子model，注意使用value，可以在SetValueProcess中会创建新对象
        newParam.setSrcRef(srcRef);
        newParam.setConfig(object);
        // 复制并传递
        newParam.setProcesses(param.getProcesses());
        // 进行递归调用
        execute(newParam);
    }

    /**
     * 判断一下是否可以执行batch优化
     */
    private static boolean canBatch(BeanMappingBehavior behavior) {
        return behavior.isMappingEmptyStrings() && behavior.isMappingNullValue();
    }

}
