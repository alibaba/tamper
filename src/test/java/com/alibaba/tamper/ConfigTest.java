package com.alibaba.tamper;

import java.util.LinkedHashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;

import com.alibaba.tamper.core.config.BeanMappingBehavior;
import com.alibaba.tamper.core.config.BeanMappingConfigHelper;
import com.alibaba.tamper.core.config.BeanMappingField;
import com.alibaba.tamper.core.config.BeanMappingObject;
import com.alibaba.tamper.object.SrcMappingObject;
import com.alibaba.tamper.object.TargetMappingObject;

/**
 * @author jianghang 2011-5-27 上午09:26:38
 */
public class ConfigTest extends TestCase {

    @Test
    public void testFileParse() {
        String file = "mapping/config.xml";
        BeanMappingConfigHelper.getInstance().registerConfig(file);
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(LinkedHashMap.class,
                                                                                              LinkedHashMap.class);
        assertNull(object);
        object = BeanMappingConfigHelper.getInstance().getBeanMappingObject("testConfig");
        printObject(object);
        assertNotNull(object);
        BeanMappingBehavior globalBehavior = BeanMappingConfigHelper.getInstance().getGlobalBehavior();
        Assert.assertEquals(globalBehavior.isMappingNullValue(), true);
        Assert.assertEquals(globalBehavior.isMappingEmptyStrings(), true);
        Assert.assertEquals(globalBehavior.isDebug(), false);
        Assert.assertEquals(globalBehavior.isTrimStrings(), true);

        BeanMappingBehavior beanBehavior = object.getBehavior();
        Assert.assertEquals(beanBehavior.isMappingNullValue(), false);// 覆盖了global
        Assert.assertEquals(beanBehavior.isDebug(), true); // 覆盖了global
        Assert.assertEquals(beanBehavior.isMappingEmptyStrings(), true); // 继承了global
        Assert.assertEquals(beanBehavior.isTrimStrings(), true); // 继承了global

        BeanMappingField field = object.getBeanFields().get(0);
        BeanMappingBehavior fieldBehavior = field.getBehavior();
        Assert.assertEquals(fieldBehavior.isMappingNullValue(), true);// 覆盖了bean
        Assert.assertEquals(fieldBehavior.isDebug(), true); // 继承了bean
        Assert.assertEquals(fieldBehavior.isMappingEmptyStrings(), true); // 继承了bean
        Assert.assertEquals(fieldBehavior.isTrimStrings(), true); // 继承了bean

    }

    @Test
    public void testClassParse() {
        Class srcClass = SrcMappingObject.class;
        Class targetClass = TargetMappingObject.class;
        BeanMappingObject object = BeanMappingConfigHelper.getInstance().getBeanMappingObject(srcClass, targetClass,
                                                                                              true);// 自动注册
        assertNotNull(object);
        printObject(object);
    }

    private void printObject(BeanMappingObject object) {
        System.out.println(ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE));
        for (BeanMappingField field : object.getBeanFields()) {
            System.out.println(ToStringBuilder.reflectionToString(field, ToStringStyle.MULTI_LINE_STYLE));
        }
    }

}
