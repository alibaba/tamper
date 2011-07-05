package com.agapple.mapping.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;
import com.agapple.mapping.process.script.ScriptContext;
import com.agapple.mapping.process.script.ScriptExecutor;
import com.agapple.mapping.process.script.ScriptHelper;
import com.agapple.mapping.process.script.jexl.JexlScriptContext;

/**
 * 自定义script脚本的处理器 , get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:25:17
 */
public class ScriptValueProcess implements ValueProcess {

    public final String SCRIPT_CONTEXT = "_script_context";

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        BeanMappingField currentField = invocation.getContext().getCurrentField();
        if (StringUtils.isNotEmpty(currentField.getScript())) {
            BeanMappingObject beanObject = invocation.getContext().getBeanObject();

            Map param = new HashMap();
            param.put(beanObject.getSrcKey(), invocation.getContext().getParam().getSrcRef());
            param.put(beanObject.getTargetKey(), invocation.getContext().getParam().getTargetRef());

            Map custom = invocation.getContext().getCustom();
            if (custom != null && custom.containsKey(SCRIPT_CONTEXT)) {
                Map newParam = (Map) custom.get(SCRIPT_CONTEXT);
                param.putAll(newParam);
            }

            ScriptContext scriptContext = new JexlScriptContext(param);
            // 进行值转化处理
            ScriptExecutor scriptExecutor = ScriptHelper.getInstance().getScriptExecutor();
            value = scriptExecutor.evaluate(scriptContext, currentField.getScript());
        }

        // 继续走到下一步处理
        return invocation.proceed(value);

    }
}
