package com.agapple.mapping.core.introspect;

import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.reflect.FastMethod;

import com.agapple.mapping.core.BeanMappingException;

/**
 * 基于Property属性的batch处理：支持get/set的批量处理
 * 
 * @author jianghang 2011-5-31 下午09:00:24
 */
public class PropertyBatchExecutor extends AbstractBatchExecutor {

    private BulkBean bulkBean = null;

    public PropertyBatchExecutor(Introspector is, Class<?> clazz, String[] fields, Class[] args){
        super(clazz);
        bulkBean = buildGetBulkBean(is, clazz, fields, args);
    }

    public Object[] gets(Object obj) throws BeanMappingException {
        return bulkBean.getPropertyValues(obj);
    }

    public void sets(Object obj, Object[] values) throws BeanMappingException {
        bulkBean.setPropertyValues(obj, values);
    }

    /**
     * 判断当前executor是否可用
     * 
     * @return
     */
    public final boolean isAlive() {
        return (bulkBean != null);
    }

    protected BulkBean buildGetBulkBean(Introspector is, Class<?> clazz, String[] fields, Class[] args) {
        if (fields.length != args.length) {
            throw new BeanMappingException("fields and args size is not match!");
        }

        String[] getters = new String[fields.length];
        String[] setters = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String property = fields[i];
            Class arg = args[i];
            FastMethod setMethod = PropertySetExecutor.discover(is, clazz, property, arg);
            FastMethod getMethod = PropertyGetExecutor.discover(is, clazz, property);
            if (setMethod == null) {
                throw new BeanMappingException("class[" + clazz.getName() + "] field[" + property + "] arg["
                                               + arg.getName() + "] set Method is not exist!");
            }

            if (getMethod == null) {
                throw new BeanMappingException("class[" + clazz.getName() + "] field[" + property
                                               + "] get Method is not exist!");
            }

            if (getMethod.getReturnType() != arg) {
                throw new BeanMappingException("class[" + clazz.getName() + "] field[" + property
                                               + "] getMethod does not match declared type");
            }
            setters[i] = setMethod.getName();
            getters[i] = getMethod.getName();
        }

        bulkBean = BulkBean.create(clazz, getters, setters, args);
        return bulkBean;
    }

}
