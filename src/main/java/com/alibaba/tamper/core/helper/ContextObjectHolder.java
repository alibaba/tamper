package com.alibaba.tamper.core.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个mapping执行过程都认为是在一个独立的Region中进行处理，在Region中会记录一下当前的一些信息
 * 
 * @author jianghang 2012-4-5 下午02:27:41
 */
public class ContextObjectHolder {

    private static volatile ContextObjectHolder singleton       = null;
    public static final String                  MAPPING_ENTER   = "_mapping_enter_";
    public static final String                  SCRIPT_CONTEXT  = "_script_context_";
    public static final String                  PROCESS_CONTEXT = "_process_context_";
    private ThreadLocal<Map<Object, Object>>    context         = new ThreadLocal<Map<Object, Object>>() {

                                                                    protected Map<Object, Object> initialValue() {
                                                                        return new HashMap<Object, Object>(10);
                                                                    }

                                                                };

    public ContextObjectHolder(){

    }

    /**
     * 单例方法
     */
    public static ContextObjectHolder getInstance() {
        if (singleton == null) {
            synchronized (ContextObjectHolder.class) {
                if (singleton == null) { // double check
                    singleton = new ContextObjectHolder();
                }
            }
        }
        return singleton;
    }

    public boolean enter() {
        Map map = context.get();
        Object value = map.put(MAPPING_ENTER, true);
        return value == null; // 如果value为null，说明是第一次进入
    }

    public void clear() {
        context.get().clear();
    }

    public void put(Object key, Object value) {
        this.context.get().put(key, value);
    }

    public Object get(Object key) {
        return this.context.get().get(key);
    }

    public Object remove(Object key) {
        return this.context.get().remove(key);
    }
}
