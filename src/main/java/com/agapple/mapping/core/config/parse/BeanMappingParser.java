package com.agapple.mapping.core.config.parse;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.config.BeanMappingBehavior;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingField;
import com.agapple.mapping.core.config.BeanMappingFieldAttributes;
import com.agapple.mapping.core.config.BeanMappingObject;
import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.core.helper.XmlHelper;

/**
 * 解析对应的mapping配置
 * 
 * @author jianghang 2011-5-26 下午07:13:45
 */
public class BeanMappingParser {

    private static final String MAPPING_SCHEMA = "META-INF/mapping.xsd";

    public static List<BeanMappingObject> parseMapping(InputStream in) throws BeanMappingException {
        List<BeanMappingObject> result = parseMappingObject(in);
        List<BeanMappingObject> reseverResult = new ArrayList<BeanMappingObject>(result.size());
        for (BeanMappingObject object : result) {
            if (object.isReversable()) {
                BeanMappingObject reverseObject = reverse(object);
                if (reverseObject != null) {
                    reseverResult.add(reverseObject);
                }
            }
        }
        result.addAll(reseverResult);
        return result;
    }

    // 自动解析下src/target匹配的属性
    public static List<BeanMappingObject> parseMapping(Class src, Class target) throws BeanMappingException {
        List<BeanMappingObject> result = new ArrayList<BeanMappingObject>(2);
        PropertyDescriptor[] targetPds = ReflectionHelper.getPropertyDescriptors(target);
        PropertyDescriptor[] srcPds = ReflectionHelper.getPropertyDescriptors(src);
        BeanMappingObject object = new BeanMappingObject();
        object.setSrcClass(src);
        object.setTargetClass(target);
        object.setBatch(true);
        BeanMappingBehavior globalBehavior = BeanMappingConfigHelper.getInstance().getGlobalBehavior();
        List<BeanMappingField> fields = new ArrayList<BeanMappingField>();
        for (PropertyDescriptor targetPd : targetPds) {
            String property = targetPd.getName();
            PropertyDescriptor srcPd = getMatchPropertyDescriptor(srcPds, property);
            if (srcPd == null) {// 没有匹配属性
                continue;
            }

            if (targetPd.getWriteMethod() != null && srcPd.getReadMethod() != null) {
                BeanMappingField field = new BeanMappingField();
                BeanMappingFieldAttributes srcFieldAttribute = new BeanMappingFieldAttributes();
                srcFieldAttribute.setName(property);
                srcFieldAttribute.setClazz(srcPd.getPropertyType());
                BeanMappingFieldAttributes targetFieldAttribute = new BeanMappingFieldAttributes();
                targetFieldAttribute.setName(property);
                targetFieldAttribute.setClazz(targetPd.getPropertyType());
                // 添加记录
                field.setSrcField(srcFieldAttribute);
                field.setTargetField(targetFieldAttribute);
                field.setBehavior(globalBehavior);
                fields.add(field);
            }

        }
        object.setBeanFields(fields);
        object.setBehavior(globalBehavior);// 设置为global
        result.add(object);
        if (object.isReversable()) {
            BeanMappingObject reverseObject = reverse(object);
            if (reverseObject != null) {
                result.add(reverseObject);
            }
        }
        return result;
    }

    // 解析class的属性为BeanMapping对象
    public static List<BeanMappingObject> parseMapMapping(Class src) throws BeanMappingException {
        List<BeanMappingObject> result = new ArrayList<BeanMappingObject>(2);
        PropertyDescriptor[] targetPds = ReflectionHelper.getPropertyDescriptors(src);
        BeanMappingObject object = new BeanMappingObject();
        object.setSrcClass(src);
        object.setTargetClass(HashMap.class);
        object.setBatch(true);
        List<BeanMappingField> fields = new ArrayList<BeanMappingField>();
        BeanMappingBehavior globalBehavior = BeanMappingConfigHelper.getInstance().getGlobalBehavior();
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null && targetPd.getReadMethod() != null) {
                BeanMappingField field = new BeanMappingField();
                BeanMappingFieldAttributes srcFieldAttribute = new BeanMappingFieldAttributes();
                srcFieldAttribute.setName(targetPd.getName());
                srcFieldAttribute.setClazz(targetPd.getPropertyType());
                BeanMappingFieldAttributes targetFieldAttribute = new BeanMappingFieldAttributes();
                targetFieldAttribute.setName(targetPd.getName());
                targetFieldAttribute.setClazz(targetPd.getPropertyType());
                field.setSrcField(srcFieldAttribute);
                field.setTargetField(targetFieldAttribute);
                field.setBehavior(globalBehavior);// 设置为global
                fields.add(field);
            }
        }
        object.setBeanFields(fields);
        object.setBehavior(globalBehavior);// 设置为global
        result.add(object);
        if (object.isReversable()) {
            BeanMappingObject reverseObject = reverse(object);
            if (reverseObject != null) {
                result.add(reverseObject);
            }
        }
        return result;
    }

    // 反转处理BeanMappingObject对象
    private static BeanMappingObject reverse(BeanMappingObject object) {
        BeanMappingObject newObject = new BeanMappingObject();
        // 反转一下属性，主要是一些srcName,srcClass等
        newObject.setSrcClass(object.getTargetClass());
        newObject.setTargetClass(object.getSrcClass());
        newObject.setReversable(object.isReversable());
        newObject.setBatch(object.isBatch());
        newObject.setSrcKey(object.getSrcKey());
        newObject.setTargetKey(object.getTargetKey());
        newObject.setBehavior(object.getBehavior());
        List<BeanMappingField> fields = newObject.getBeanFields();
        for (BeanMappingField field : object.getBeanFields()) {
            // 倒转一下
            BeanMappingField newField = new BeanMappingField();
            BeanMappingFieldAttributes srcFieldAttribute = new BeanMappingFieldAttributes();
            srcFieldAttribute.setName(field.getTargetField().getName());
            srcFieldAttribute.setClazz(field.getTargetField().getClazz());
            BeanMappingFieldAttributes targetFieldAttribute = new BeanMappingFieldAttributes();
            targetFieldAttribute.setName(field.getSrcField().getName());
            targetFieldAttribute.setClazz(field.getSrcField().getClazz());

            newField.setSrcField(srcFieldAttribute);
            newField.setTargetField(targetFieldAttribute);
            newField.setDefaultValue(field.getDefaultValue());
            newField.setMapping(field.isMapping());
            newField.setBehavior(field.getBehavior());
            if (StringUtils.isNotEmpty(field.getConvertor()) || StringUtils.isNotEmpty(field.getScript())) {
                object.setReversable(false);// 强制设置为false
                return null;
            }
            fields.add(newField);
        }
        return newObject;
    }

    // 根据属性名获取一下匹配的PropertyDescriptor
    private static PropertyDescriptor getMatchPropertyDescriptor(PropertyDescriptor[] srcPds, String property) {
        for (PropertyDescriptor srcPd : srcPds) {
            if (srcPd.getName().equals(property)) {
                return srcPd;
            }
        }

        return null;
    }

    // 解析一下bean-mapping
    private static List<BeanMappingObject> parseMappingObject(InputStream in) {
        Document doc = XmlHelper.createDocument(
                                                in,
                                                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                                                                                                   MAPPING_SCHEMA));
        Element root = doc.getDocumentElement();

        NodeList globalNodeList = root.getElementsByTagName("global-configurations");
        if (globalNodeList.getLength() > 1) {
            throw new BeanMappingException("global-configurations is exceed one node!");
        }

        BeanMappingBehavior globalBehavior = BeanMappingConfigHelper.getInstance().getGlobalBehavior();
        if (globalNodeList.getLength() == 1) {
            globalBehavior = BeanMappingBehaviorParse.parse(globalNodeList.item(0), globalBehavior);
            BeanMappingConfigHelper.getInstance().setGlobalBehavior(globalBehavior);
        }

        NodeList classAliasNodeList = root.getElementsByTagName("class-alias-configurations");
        for (int i = 0; i < classAliasNodeList.getLength(); i++) {
            ClassAliasParse.parseAndRegister(classAliasNodeList.item(i));
        }

        NodeList convetorNodeList = root.getElementsByTagName("convetors-configurations");
        for (int i = 0; i < convetorNodeList.getLength(); i++) {
            ConvertorParse.parseAndRegister(convetorNodeList.item(i));
        }

        NodeList functionClassNodeList = root.getElementsByTagName("function-class-configurations");
        for (int i = 0; i < functionClassNodeList.getLength(); i++) {
            FunctionClassParse.parseAndRegister(functionClassNodeList.item(i));
        }

        NodeList nodeList = root.getElementsByTagName("bean-mapping");
        List<BeanMappingObject> mappings = new ArrayList<BeanMappingObject>();
        // 解析BeanMappingObject属性
        for (int i = 0; i < nodeList.getLength(); i++) {
            BeanMappingObject config = BeanMappingParse.parse(nodeList.item(i), globalBehavior);
            // 添加到返回结果
            mappings.add(config);
        }

        return mappings;
    }

}
