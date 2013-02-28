package com.alibaba.mapping.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.mapping.core.BeanMappingException;
import com.alibaba.mapping.core.config.BeanMappingField;
import com.alibaba.mapping.core.config.BeanMappingObject;
import com.alibaba.mapping.core.helper.ContextObjectHolder;
import com.alibaba.mapping.core.process.ValueProcess;
import com.alibaba.mapping.core.process.ValueProcessInvocation;
import com.alibaba.mapping.process.script.ScriptExecutor;
import com.alibaba.mapping.process.script.ScriptHelper;

/**
 * 自定义script脚本的处理器 , get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:25:17
 */
public class ScriptValueProcess implements ValueProcess {

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        BeanMappingField currentField = invocation.getContext().getCurrentField();
        if (StringUtils.isNotEmpty(currentField.getScript())) {
            BeanMappingObject beanObject = invocation.getContext().getBeanObject();

            Map param = (Map) ContextObjectHolder.getInstance().get(ContextObjectHolder.SCRIPT_CONTEXT);// 使用第一次记录的script_context
            if (param == null) {
                param = new HashMap();
                param.put(beanObject.getSrcKey(), invocation.getContext().getParam().getSrcRef());
                param.put(beanObject.getTargetKey(), invocation.getContext().getParam().getTargetRef());
                ContextObjectHolder.getInstance().put(ContextObjectHolder.SCRIPT_CONTEXT, param);
            }

            Map custom = invocation.getContext().getCustom();
            if (custom != null && custom.containsKey(ContextObjectHolder.SCRIPT_CONTEXT)) {
                Map newParam = (Map) custom.get(ContextObjectHolder.SCRIPT_CONTEXT);
                param.putAll(newParam);
            }

            // 进行值转化处理
            ScriptExecutor scriptExecutor = ScriptHelper.getInstance().getScriptExecutor();
            value = scriptExecutor.evaluate(param, currentField.getScript());
        }

        // 继续走到下一步处理
        return invocation.proceed(value);

    }
}
