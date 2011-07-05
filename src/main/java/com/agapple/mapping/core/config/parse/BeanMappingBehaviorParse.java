package com.agapple.mapping.core.config.parse;

import org.w3c.dom.Node;

import com.agapple.mapping.core.config.BeanMappingBehavior;

/**
 * 解析下Beahvior相关配置
 * 
 * @author jianghang 2011-6-21 下午01:28:28
 */
public class BeanMappingBehaviorParse {

    public static BeanMappingBehavior parse(Node node, BeanMappingBehavior parent) {
        Node debug = node.getAttributes().getNamedItem("debug");
        Node mappingNullValue = node.getAttributes().getNamedItem("mappingNullValue");
        Node mappingEmptyStrings = node.getAttributes().getNamedItem("mappingEmptyStrings");
        Node trimStrings = node.getAttributes().getNamedItem("trimStrings");

        boolean isConfig = false;
        BeanMappingBehavior behavior = parent.clone();// 从上一个节点复制配置，进行替换处理

        if (debug != null) {
            behavior.setDebug(Boolean.valueOf(debug.getNodeValue()));
            isConfig = true;
        }
        if (mappingNullValue != null) {
            behavior.setMappingNullValue(Boolean.valueOf(mappingNullValue.getNodeValue()));
            isConfig = true;
        }
        if (mappingEmptyStrings != null) {
            behavior.setMappingEmptyStrings(Boolean.valueOf(mappingEmptyStrings.getNodeValue()));
            isConfig = true;
        }
        if (trimStrings != null) {
            behavior.setTrimStrings(Boolean.valueOf(trimStrings.getNodeValue()));
            isConfig = true;
        }

        return isConfig ? behavior : parent;// 如果未设置，则直接使用parent
    }
}
