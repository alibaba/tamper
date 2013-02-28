package com.alibaba.tamper.process.script.lifecyle;

/**
 * 支持Initializing的script接口,在一次Mapping之前进行回调初始化
 * 
 * @author jianghang 2011-11-23 下午04:39:37
 */
public interface InitializingScript {

    public void initial();
}
