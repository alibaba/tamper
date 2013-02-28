package com.alibaba.tamper.process.convertor;

import com.alibaba.tamper.core.config.BeanMappingField;

/**
 * 自定义的collection convertor接口, 外部可不关注该接口
 * 
 * @author jianghang 2012-4-5 下午05:18:31
 */
public interface CollectionConvertor extends Convertor {

    /**
     * 支持多级collection映射，需指定多级的componentClass
     */
    public Object convertCollection(BeanMappingField context, Object src, Class destClass, Class... componentClasses);
}
