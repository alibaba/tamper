package com.agapple.mapping.core.config.parse;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.helper.ReflectionHelper;

/**
 * 解析一下BeanMapping配置
 * 
 * @author jianghang 2011-6-21 下午01:40:18
 */
public class BeanMappingParse {

    public static BeanMappingObject parse(Node node, BeanMappingBehavior parent) {
        BeanMappingObject config = new BeanMappingObject();
        // mapping source class
        Node srcNode = node.getAttributes().getNamedItem("srcClass");
        // mapping target class
        Node targetNode = node.getAttributes().getNamedItem("targetClass");

        if (srcNode == null || targetNode == null) {
            throw new BeanMappingException("Parse error for bean-mapping srcClass or targetClass is null");
        }
        Node srcKeyNode = node.getAttributes().getNamedItem("srcKey");
        Node targetKeyNode = node.getAttributes().getNamedItem("targetKey");
        // 设置reversable
        Node reversableNode = node.getAttributes().getNamedItem("reversable");
        Node batchNode = node.getAttributes().getNamedItem("batch");

        String src = srcNode.getNodeValue();
        String target = targetNode.getNodeValue();
        config.setSrcClass(ReflectionHelper.forName(src));
        config.setTargetClass(ReflectionHelper.forName(target));
        if (srcKeyNode != null) {
            config.setSrcKey(srcKeyNode.getNodeValue());
        }
        if (targetNode != null) {
            config.setTargetKey(targetKeyNode.getNodeValue());
        }
        if (reversableNode != null) {
            config.setReversable(Boolean.valueOf(reversableNode.getNodeValue()));
        }
        if (batchNode != null) {
            config.setBatch(Boolean.valueOf(batchNode.getNodeValue()));
        }

        // 解析下BeanHaivor参数
        BeanMappingBehavior beanbeHavior = BeanMappingBehaviorParse.parse(node, parent);
        config.setBehavior(beanbeHavior);

        if (beanbeHavior.isMappingEmptyStrings() == false || beanbeHavior.isMappingNullValue() == false) {
            config.setBatch(false);// 强制设置为false，因为batch处理无法过滤null/empty value不做set处理
        }

        // 解析bean fields
        NodeList nodeList = node.getChildNodes();
        List<BeanMappingField> beanFields = new ArrayList<BeanMappingField>(10);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node clildNode = nodeList.item(i);
            if ("field-mapping".equals(clildNode.getNodeName())) {
                BeanMappingField field = parseField(clildNode);
                BeanMappingBehavior fieldbehavior = BeanMappingBehaviorParse.parse(clildNode, beanbeHavior);// 解析下behaivor参数
                field.setBehavior(fieldbehavior);
                if (fieldbehavior.isMappingEmptyStrings() == false || fieldbehavior.isMappingNullValue() == false) {
                    config.setBatch(false);// 强制设置为false，因为batch处理无法过滤null/empty value不做set处理
                }
                beanFields.add(field);
            }
        }
        config.setBeanFields(beanFields);
        return config;
    }

    public static BeanMappingField parseField(Node node) {
        BeanMappingField beanField = new BeanMappingField();
        Node srcNameNode = node.getAttributes().getNamedItem("srcName");
        Node srcClassNode = node.getAttributes().getNamedItem("srcClass");
        Node srcLocatorNode = node.getAttributes().getNamedItem("srcLocatorClass");
        Node srcComponentNode = node.getAttributes().getNamedItem("srcComponentClass");

        Node targetNameNode = node.getAttributes().getNamedItem("targetName");
        Node targetClassNode = node.getAttributes().getNamedItem("targetClass");
        Node targetLocatorNode = node.getAttributes().getNamedItem("targetLocatorClass");
        Node targetComponentNode = node.getAttributes().getNamedItem("targetComponentClass");
        Node defaultValueNode = node.getAttributes().getNamedItem("defaultValue");
        Node convetorNode = node.getAttributes().getNamedItem("convetor");
        Node scriptNode = node.getAttributes().getNamedItem("script");
        if (scriptNode == null && srcNameNode == null) {
            throw new BeanMappingException("srcName or script is requied");
        }
        if (targetNameNode == null) {
            throw new BeanMappingException("targetName is requied");
        }

        if (srcNameNode != null) {
            beanField.getSrcField().setName(srcNameNode.getNodeValue());
        }

        if (srcClassNode != null) {
            beanField.getSrcField().setClazz(ReflectionHelper.forName(srcClassNode.getNodeValue()));
        }
        if (srcLocatorNode != null) {
            beanField.getSrcField().setLocatorClass(ReflectionHelper.forName(srcLocatorNode.getNodeValue()));
        }

        if (srcComponentNode != null) {
            beanField.getSrcField().addComponentClasses(ReflectionHelper.forName(srcComponentNode.getNodeValue()));
        }

        if (targetNameNode != null) {
            beanField.getTargetField().setName(targetNameNode.getNodeValue());
        }

        if (targetClassNode != null) {
            beanField.getTargetField().setClazz(ReflectionHelper.forName(targetClassNode.getNodeValue()));
        }

        if (targetLocatorNode != null) {
            beanField.getTargetField().setLocatorClass(ReflectionHelper.forName(targetLocatorNode.getNodeValue()));
        }

        if (targetComponentNode != null) {
            beanField.getTargetField().addComponentClasses(ReflectionHelper.forName(targetComponentNode.getNodeValue()));
        }

        if (defaultValueNode != null) {
            beanField.setDefaultValue(defaultValueNode.getNodeValue());
        }
        if (convetorNode != null) {
            beanField.setConvertor(convetorNode.getNodeValue());
        }
        if (scriptNode != null) {
            beanField.setScript(scriptNode.getNodeValue());
        }
        // 处理下mapping
        Node mappingNode = node.getAttributes().getNamedItem("mapping");
        if (mappingNode != null) {
            beanField.setMapping(Boolean.valueOf(mappingNode.getNodeValue()));
        }

        return beanField;
    }
}
