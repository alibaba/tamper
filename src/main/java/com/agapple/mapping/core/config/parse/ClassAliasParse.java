package com.agapple.mapping.core.config.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.helper.ReflectionHelper;

/**
 * 解析下Class alias的相关配置
 * 
 * @author jianghang 2011-6-21 下午01:33:28
 */
public class ClassAliasParse {

    private final static Logger logger = LoggerFactory.getLogger(ClassAliasParse.class);

    public static void parseAndRegister(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node aliasClassNode = nodeList.item(i);
            if ("classAlias".equals(aliasClassNode.getNodeName())) {
                Node aliasNode = aliasClassNode.getAttributes().getNamedItem("alias");
                Node clazzNode = aliasClassNode.getAttributes().getNamedItem("class");
                if (aliasNode == null || clazzNode == null) {
                    throw new BeanMappingException("alias or class is null , please check!");
                }

                String alias = aliasNode.getNodeValue();
                Class clazz = ReflectionHelper.forName(clazzNode.getNodeValue());
                ReflectionHelper.registerClassAlias(aliasNode.getNodeValue(), clazz);
                if (logger.isDebugEnabled()) {
                    logger.debug("register class[" + clazz.toString() + "] to alias[" + alias + "]");
                }
            }
        }
    }
}
