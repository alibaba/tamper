package com.agapple.mapping.process;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;

/**
 * set操作流程中, 尝试创建一下嵌套的bean实例，通过反射newInstance,
 * 
 * @author jianghang 2011-5-28 下午11:32:38
 */
public class BeanCreatorValueProcess implements ValueProcess {

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        if (value != null) {
            BeanMappingField currentField = invocation.getContext().getCurrentField();
            if (currentField.isMapping()) {
                // 判断下是否在处理嵌套的mapping
                // 这里的value代表从get取出来的嵌套对象，如果有值，说明在目标对象上也需要创建targetClass对象进行复制
                value = ReflectionHelper.newInstance(invocation.getContext().getCurrentField().getTargetField().getClazz());
            }
        }

        // 继续下一步的调用
        return invocation.proceed(value);
    }

}
