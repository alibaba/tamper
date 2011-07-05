package com.agapple.mapping.core.process;

import java.util.List;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.introspect.GetExecutor;
import com.agapple.mapping.core.introspect.SetExecutor;

/**
 * ValueProcess执行的get操作的控制器
 * 
 * @author jianghang 2011-5-30 上午07:44:02
 */
public class ValueProcessInvocation {

    private ValueProcessContext context;          // valueProcess执行的上下文参数
    private List<ValueProcess>  processes;        // 处理的process列表
    private GetExecutor         getExecutor;      // get方法调用
    private SetExecutor         setExecutor;      // set方法调用
    private int                 currentIndex = -1; // 当前执行的valueProcess下标

    public ValueProcessInvocation(GetExecutor getExecutor, SetExecutor setExecutor, ValueProcessContext context,
                                  List<ValueProcess> processes){
        this.getExecutor = getExecutor;
        this.setExecutor = setExecutor;
        this.context = context;
        this.processes = processes;
    }

    /**
     * 获取初始value值
     */
    public Object getInitialValue() {
        return invokeGetExecutor();
    }

    public Object proceed(Object value) throws BeanMappingException {
        if (processes == null) { // 如果处理列表为空，则直接调用
            return invokeSetExecutor(value);
        } else {
            if (this.currentIndex == this.processes.size() - 1) {
                return invokeSetExecutor(value);
            } else {
                ValueProcess vp = this.processes.get(++this.currentIndex);
                return vp.process(value, this);
            }
        }

    }

    /**
     * 执行GetExecutor
     */
    protected Object invokeGetExecutor() {
        if (isGetBatch()) { // 如果是batch模式
            return context.getHolder().getNext();
        }

        if (getExecutor != null) {
            return getExecutor.invoke(context.getParam().getSrcRef());
        } else {
            return null;
        }
    }

    /**
     * 执行SetExecutor
     */
    protected Object invokeSetExecutor(Object value) {
        if (isSetBatch()) { // 如果是batch模式
            context.getHolder().setObject(value); // 更新下holder的value值
            return value;
        }

        if (setExecutor != null) {
            return setExecutor.invoke(context.getParam().getTargetRef(), value);
        } else {
            return null;
        }
    }

    /**
     * 判断当前是否处于debug模式
     */
    public boolean isDebug() {
        return getContext().getBeanObject().getBehavior().isDebug();
    }

    /**
     * 判断一下是否处于get batch处理模式
     */
    private boolean isGetBatch() {
        return context.getBeanObject().isBatch() && context.getBeanObject().getGetBatchExecutor() != null;
    }

    /**
     * 判断一下是否处于set batch处理模式
     */
    private boolean isSetBatch() {
        return context.getBeanObject().isBatch() && context.getBeanObject().getSetBatchExecutor() != null;
    }

    // =================== get 操作===============

    public ValueProcessContext getContext() {
        return context;
    }

}
