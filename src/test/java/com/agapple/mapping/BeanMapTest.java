package com.agapple.mapping;

import java.math.BigDecimal;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.SrcMappingObject;

/**
 * @author jianghang 2011-5-29 上午01:08:39
 */
public class BeanMapTest extends TestCase {

    public BeanMap beanMap = BeanMap.create(SrcMappingObject.class);

    @Test
    public void testDescribe_Populate_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        srcRef.setMapping(nestedSrcRef);

        Map map = beanMap.describe(srcRef);
        assertEquals(map.get("integerValue"), srcRef.getIntegerValue());
        assertEquals(map.get("intValue"), srcRef.getIntValue());
        assertEquals(map.get("name"), srcRef.getName());
        assertEquals(map.get("start"), srcRef.isStart());
        assertNotNull(map.get("mapping"));
        NestedSrcMappingObject nested = (NestedSrcMappingObject) map.get("mapping");
        assertEquals(srcRef.getMapping(), nested);// 没有做递归的describe
        // assertEquals(nested.get("bigDecimalValue"), nestedSrcRef.getBigDecimalValue());
        // assertEquals(nested.get("name"), null);// 没有设置，为null

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        beanMap.populate(newSrcRef, map);
        assertEquals(map.get("integerValue"), newSrcRef.getIntegerValue());
        assertEquals(map.get("intValue"), newSrcRef.getIntValue());
        assertEquals(map.get("name"), newSrcRef.getName());
        assertEquals(map.get("start"), newSrcRef.isStart());
        assertNotNull(map.get("mapping"));
        NestedSrcMappingObject newNested = (NestedSrcMappingObject) newSrcRef.getMapping();
        assertEquals(map.get("mapping"), newNested);// 没有做递归的describe
    }
}
