package com.alibaba.tamper.process.script.lifecyle;

/**
 * 支持Disposable的script接口,在一次Mapping完成后进行回调，完成数据清理动作
 * 
 * @author jianghang 2011-11-23 下午04:39:37
 */
public interface DisposableScript {

    public void dispose();
}
