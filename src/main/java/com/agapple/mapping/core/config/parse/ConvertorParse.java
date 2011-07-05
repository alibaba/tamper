package com.agapple.mapping.core.config.parse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * 解析下Convertor的相关配置,直接注册到仓库
 * 
 * @author jianghang 2011-6-21 下午01:33:38
 */
public class ConvertorParse {

    private final static Logger logger = LoggerFactory.getLogger(ConvertorParse.class);

    public static void parseAndRegister(Node convetorsNode) {
        NodeList nodeList = convetorsNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if ("convertor".equals(node.getNodeName())) {
                Node aliasNode = node.getAttributes().getNamedItem("alias");
                Node clazzNode = node.getAttributes().getNamedItem("class");
                Node srcClassNode = node.getAttributes().getNamedItem("srcClass");
                Node targetClassNode = node.getAttributes().getNamedItem("targetClass");
                if (clazzNode != null) {
                    Class clazz = ReflectionHelper.forName(clazzNode.getNodeValue());
                    if (!Convertor.class.isAssignableFrom(clazz)) { // 检查下必须为Convertor的子类
                        throw new BeanMappingException(clazz.toString() + " is not implements Convertor");
                    }

                    Convertor convertor = (Convertor) ReflectionHelper.newInstance(clazz);
                    if (aliasNode != null) {
                        // 注册为别名
                        String alias = aliasNode.getNodeValue();
                        ConvertorHelper.getInstance().registerConvertor(alias, convertor);

                        if (logger.isDebugEnabled()) {
                            logger.debug("register Convertor[" + clazz.toString() + "] to alias[" + alias + "]");
                        }
                    } else {
                        String srcClass = srcClassNode.getNodeValue();
                        String targetClass = targetClassNode.getNodeValue();
                        if (StringUtils.isEmpty(srcClass) || StringUtils.isEmpty(targetClass)) {
                            throw new BeanMappingException(clazz.toString() + " should fix srcClass and targetClass!");
                        }
                        Class srcClazz = ReflectionHelper.forName(srcClassNode.getNodeValue());
                        Class targetClazz = ReflectionHelper.forName(targetClassNode.getNodeValue());
                        // 根据srcClass/targetClass进行自动匹配
                        ConvertorHelper.getInstance().registerConvertor(srcClazz, targetClazz, convertor);
                        if (logger.isDebugEnabled()) {
                            logger.debug("register Convertor[" + clazz.toString() + "] used by srcClass["
                                         + srcClass.toString() + "]" + " targetClass[" + targetClass.toString() + "]");
                        }
                    }
                }
            }
        }
    }
}
