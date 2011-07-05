package com.agapple.mapping.core.config.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.process.script.ScriptHelper;

/**
 * 解析下Function class的相关配置
 * 
 * @author jianghang 2011-6-21 下午01:33:28
 */
public class FunctionClassParse {

    private final static Logger logger = LoggerFactory.getLogger(FunctionClassParse.class);

    public static void parseAndRegister(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node aliasClassNode = nodeList.item(i);
            if ("functionClass".equals(aliasClassNode.getNodeName())) {
                Node nameNode = aliasClassNode.getAttributes().getNamedItem("name");
                Node clazzNode = aliasClassNode.getAttributes().getNamedItem("class");
                if (nameNode == null || clazzNode == null) {
                    throw new BeanMappingException("alias or class is null , please check!");
                }

                String name = nameNode.getNodeValue();
                Class clazz = ReflectionHelper.forName(clazzNode.getNodeValue());
                ScriptHelper.getInstance().registerFunctionClass(name, ReflectionHelper.newInstance(clazz));
                if (logger.isDebugEnabled()) {
                    logger.debug("register class[" + clazz.toString() + "] to name[" + name + "]");
                }
            }
        }
    }
}
