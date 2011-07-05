package com.agapple.mapping.core.helper;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 批量处理对象的holder处理
 * 
 * @author jianghang 2011-6-2 下午12:44:26
 */
public class BatchObjectHolder {

    private Object[] batchValues = null;
    private int      currentIndex;

    public void setBatchValues(Object[] batchValues) {
        this.batchValues = batchValues;
    }

    public BatchObjectHolder(Object[] values){
        if (values == null) {
            throw new BeanMappingException("batch values is null!");
        }
        this.batchValues = values;
        this.currentIndex = -1;
    }

    public Object[] getBatchValues() {
        return batchValues;
    }

    public Object getNext() {
        currentIndex = currentIndex + 1;
        if (currentIndex > batchValues.length) {
            throw new BeanMappingException("batch values index is out of Array!");
        }
        return batchValues[currentIndex];
    }

    public void setObject(Object value) {
        batchValues[currentIndex] = value;
    }

}
