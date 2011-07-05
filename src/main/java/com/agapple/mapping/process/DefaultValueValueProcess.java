package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * mapping默认值的处理，get流程处理
 * 
 * @author jianghang 2011-5-27 下午09:19:46
 */
public class DefaultValueValueProcess implements ValueProcess {

    @Override
    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        // 处理下自己的业务
        BeanMappingField currentField = invocation.getContext().getCurrentField();
        if (value == null && StringUtils.isNotEmpty(currentField.getDefaultValue())
            && currentField.isMapping() == false) {
            if (currentField.getSrcField().getClazz() != null) {// 有指定对应的SrcClass
                Convertor convertor = ConvertorHelper.getInstance().getConvertor(String.class,
                                                                                 currentField.getSrcField().getClazz());
                if (convertor != null) {
                    // 先对String字符串进行一次转化
                    value = convertor.convert(currentField.getDefaultValue(), currentField.getSrcField().getClazz());
                }
            }

            if (value == null) {
                // 不存在对默认值处理的convertor，不予处理，后续尝试下自定义的convertor
                value = currentField.getDefaultValue();
            }
        }

        return invocation.proceed(value);
    }
}
