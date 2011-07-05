package com.agapple.mapping.script;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.agapple.mapping.BeanMappingUtil;
import com.agapple.mapping.TestUtils;
import com.agapple.mapping.core.config.BeanMappingConfigHelper;
import com.agapple.mapping.core.config.BeanMappingConfigRespository;
import com.agapple.mapping.object.SrcMappingObject;

/**
 * 测试下script配置
 * 
 * @author jianghang 2011-5-31 下午09:33:57
 */
public class ScriptTest extends TestCase {

    @Before
    public void setUp() {
        try {
            // 清空下repository下的数据
            TestUtils.setField(BeanMappingConfigHelper.getInstance(), "repository", new BeanMappingConfigRespository());
            BeanMappingConfigHelper.getInstance().registerConfig("mapping/script-mapping.xml");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testScript_Ok() {
        // 测试下简单的script， 比如src.xxx。替代正常的srcName
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("name");
        srcRef.setStart(true);

        Map targetRef = new HashMap();// 测试一下mapping到一个Object对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        assertEquals(2, targetRef.get("intValue"));
        assertEquals(1, targetRef.get("integerValue"));
        assertEquals(srcRef.getName(), targetRef.get("name"));
        assertEquals(srcRef.isStart(), targetRef.get("start"));

        // 反过来测试一下
        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef);
        assertEquals(3, newSrcRef.getIntValue());
        assertEquals(Integer.valueOf("1"), newSrcRef.getIntegerValue());
        assertEquals(newSrcRef.getName(), targetRef.get("name"));
        assertEquals(newSrcRef.isStart(), targetRef.get("start"));
    }

    @Test
    public void testScript_value_null() {
        // 测试下简单的script， 比如src.xxx。替代正常的srcName
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(null);// 设置为null值
        srcRef.setIntValue(1);
        srcRef.setName("name");
        srcRef.setStart(true);

        Map targetRef = new HashMap();// 测试一下mapping到一个Object对象
        BeanMappingUtil.mapping(srcRef, targetRef);
        assertEquals(2, targetRef.get("intValue"));
        assertEquals(1, targetRef.get("integerValue"));// 这里为默认值1
        assertEquals(srcRef.getName(), targetRef.get("name"));
        assertEquals(srcRef.isStart(), targetRef.get("start"));

        // 反过来测试一下
        SrcMappingObject newSrcRef = new SrcMappingObject();
        BeanMappingUtil.mapping(targetRef, newSrcRef);
        assertEquals(3, newSrcRef.getIntValue());
        assertEquals(Integer.valueOf(1), newSrcRef.getIntegerValue());// 这里为默认值1
        assertEquals(newSrcRef.getName(), targetRef.get("name"));
        assertEquals(newSrcRef.isStart(), targetRef.get("start"));
    }

}
