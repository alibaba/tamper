package com.agapple.mapping.process.convertor;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingField;

/**
 * @author jianghang 2011-6-21 下午03:46:57
 */
public class AbastactConvertor implements Convertor, CollectionConvertor {

    @Override
    public Object convert(Object src, Class destClass) {
        throw new BeanMappingException("unSupport!");
    }

    @Override
    public Object convertCollection(Object src, Class destClass, Class... componentClasses) {
        throw new BeanMappingException("unSupport!");
    }

    @Override
    public Object convertCollection(BeanMappingField context, Object src, Class destClass, Class... componentClasses) {
        throw new BeanMappingException("unSupport!");
    }

}
