/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.agapple.mapping.process;

import org.apache.commons.lang.StringUtils;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.process.ValueProcess;
import com.agapple.mapping.core.process.ValueProcessInvocation;

/**
 * 处理下Behavior的行为控制, 可参见 {@linkplain BeanMappingBehavior}
 * 
 * @author jianghang 2011-6-22 上午09:50:16
 */
public class BehaviorValueProcess implements ValueProcess {

    public Object process(Object value, ValueProcessInvocation invocation) throws BeanMappingException {
        BeanMappingField field = invocation.getContext().getCurrentField();
        BeanMappingBehavior behavior = field.getBehavior(); // 每个属性mapping都会有一个behavior，允许覆盖上层

        // 判断一下value的null 情况
        if (value == null && behavior.isMappingNullValue() == false) {
            return value;
        }

        // 进行trim处理
        if (value instanceof String && behavior.isTrimStrings()) {
            value = StringUtils.trim((String) value);
        }

        // 判断一下String的null / empty情况
        if ((value == null || (value instanceof String && StringUtils.isEmpty((String) value)))
            && behavior.isMappingEmptyStrings() == false) {
            return value;
        }

        return invocation.proceed(value);
    }
}
