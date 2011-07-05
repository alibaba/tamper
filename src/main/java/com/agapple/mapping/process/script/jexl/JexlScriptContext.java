package com.agapple.mapping.process.script.jexl;

import java.util.Map;

import org.apache.commons.jexl2.MapContext;

import com.agapple.mapping.process.script.ScriptContext;

/**
 * 定义为Jexl context
 * 
 * @author jianghang 2011-5-25 下午07:57:59
 */
public class JexlScriptContext extends MapContext implements ScriptContext {

    public JexlScriptContext(Map map){
        super(map);
    }
}
