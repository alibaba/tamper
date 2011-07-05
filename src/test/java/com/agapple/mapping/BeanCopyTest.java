package com.agapple.mapping;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.object.NestedSrcMappingObject;
import com.agapple.mapping.object.NestedTargetMappingObject;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.object.TargetMappingObject;

/**
 * bean copy的相关测试
 * 
 * @author jianghang 2011-5-29 上午01:08:46
 */
public class BeanCopyTest extends TestCase {

    public BeanCopy srcCopyer          = BeanCopy.create(SrcMappingObject.class, TargetMappingObject.class);
    public BeanCopy targetCopyer       = BeanCopy.create(TargetMappingObject.class, SrcMappingObject.class);
    public BeanCopy nestedSrcCopyer    = BeanCopy.create(NestedSrcMappingObject.class, NestedTargetMappingObject.class);
    public BeanCopy nestedTargetCopyer = BeanCopy.create(NestedTargetMappingObject.class, NestedSrcMappingObject.class);

    @Test
    public void testCopy_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        // NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        // nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        // srcRef.setMapping(nestedSrcRef);

        TargetMappingObject targetRef = new TargetMappingObject();// 测试一下mapping到一个Object对象
        srcCopyer.copy(srcRef, targetRef);
        assertEquals(targetRef.getIntegerValue(), srcRef.getIntegerValue());
        assertEquals(targetRef.getIntValue(), srcRef.getIntValue());
        assertNull(targetRef.getTargetName());// 为null，因为属性不匹配
        assertEquals(targetRef.isStart(), srcRef.isStart());

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        targetCopyer.copy(targetRef, newSrcRef);
        assertEquals(newSrcRef.getIntegerValue(), targetRef.getIntegerValue());
        assertEquals(newSrcRef.getIntValue(), targetRef.getIntValue());
        assertNull(newSrcRef.getName());// 为null，因为属性不匹配
        assertEquals(newSrcRef.isStart(), targetRef.isStart());
    }

    @Test
    public void testSimpleCopy_ok() {
        NestedSrcMappingObject nestedSrcRef = new NestedSrcMappingObject();
        nestedSrcRef.setBigDecimalValue(BigDecimal.ONE);
        nestedSrcRef.setName("ljh");

        NestedTargetMappingObject nestedTargetRef = new NestedTargetMappingObject();// 测试一下mapping到一个Object对象
        nestedSrcCopyer.copy(nestedSrcRef, nestedTargetRef);
        assertNull(nestedTargetRef.getValue());// 属性不同，类型也不同
        assertEquals(nestedTargetRef.getName(), nestedSrcRef.getName());

        NestedSrcMappingObject newNestedSrcRef = new NestedSrcMappingObject();// 反过来再mapping一次
        nestedTargetCopyer.copy(nestedTargetRef, newNestedSrcRef);
        assertNull(newNestedSrcRef.getBigDecimalValue());// 属性不同，类型也不同
        assertEquals(newNestedSrcRef.getName(), nestedTargetRef.getName());

    }
}
