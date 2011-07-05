package com.agapple.mapping.process;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * {@linkplain Convetor}转化的处理器,set流程处理
 * 
 * @author jianghang 2011-5-27 下午09:30:40
 */
public class ConvertorValueProcess implements ValueProcess {

    private final static Logger logger = LoggerFactory.getLogger(ConvertorValueProcess.class);

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        if (value != null && invocation.getContext().getCurrentField().isMapping() == false) {
            BeanMappingField currentField = invocation.getContext().getCurrentField();
            String customConvertorName = currentField.getConvertor();
            Convertor convertor = currentField.getConvertorRef();// 取上一次实例化后的convertor对象

            if (convertor == null && currentField.getConvertorClass() != null) {
                // 进行自定义convertor初始化
                Class clazz = currentField.getConvertorClass();
                convertor = (Convertor) ReflectionHelper.newInstance(clazz);
                currentField.setConvertorRef(convertor);
            }

            if (StringUtils.isNotEmpty(customConvertorName)) { // 判断是否有自定义的convertor
                convertor = ConvertorHelper.getInstance().getConvertor(customConvertorName);
            } else if (convertor == null) {
                // srcClass针对直接使用script的情况，会出现为空，这时候需要依赖value.getClass进行转化
                // 优先不选择使用value.getClass()的原因：原生类型会返回对应的Object类型，导出会出现不必要的convetor转化
                Class srcClass = currentField.getSrcField().getClazz();
                if (srcClass == null) {
                    srcClass = value.getClass();
                }
                Class targetClass = currentField.getTargetField().getClazz();
                if (targetClass != null) {
                    // targetClass可能存在为空，比如这里的Value配置了DefaultValue，在MapSetExecutor解析时会无法识别TargetClass
                    // 无法识别后，就不做转化
                    convertor = ConvertorHelper.getInstance().getConvertor(srcClass, targetClass);

                    if (convertor == null && srcClass != targetClass && logger.isWarnEnabled()) {
                        // 记录下日志
                        StringBuilder builder = new StringBuilder();
                        builder.append("srcName[" + currentField.getSrcField().getName());
                        builder.append("],srcClass[" + ObjectUtils.toString(srcClass, "null"));
                        builder.append("],targetName[" + currentField.getTargetField().getName());
                        builder.append("],targetClass[" + ObjectUtils.toString(targetClass, "null") + "]");
                        logger.warn(builder.toString() + " convertor is null!");
                    }
                }
            }

            if (convertor != null && currentField.getTargetField().getClazz() != null) {
                List<Class> componentClasses = currentField.getTargetField().getComponentClasses();
                Class[] array = null;
                if (componentClasses != null && componentClasses.size() > 0) {
                    array = componentClasses.toArray(new Class[componentClasses.size()]);
                }
                if (array != null) {
                    // 进行嵌套对象处理
                    value = convertor.convertCollection(value, currentField.getTargetField().getClazz(), array);
                } else {
                    value = convertor.convert(value, currentField.getTargetField().getClazz());
                }
            }
        }

        // 继续下一步的调用
        return invocation.proceed(value);
    }
}
