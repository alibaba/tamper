package com.agapple.mapping.script;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.BeanMapping;
import com.agapple.mapping.core.builder.BeanMappingBuilder;
import com.agapple.mapping.object.SrcMappingObject;
import com.agapple.mapping.process.script.ScriptHelper;

/**
 * @author jianghang 2011-6-28 下午05:07:51
 */
public class CustomFunctionTest extends TestCase {

    private static final String ONE_OTHER   = "oneOther";
    private static final String TWO_OTHER   = "twoOther";
    private static final String THREE_OTHER = "threeOther";

    @Test
    public void testCustomFunction() {
        ScriptHelper.getInstance().registerFunctionClass("customFunction", new CustomFunctionClass());
        BeanMappingBuilder builder = new BeanMappingBuilder() {

            protected void configure() {
                behavior().debug(true).mappingEmptyStrings(false).mappingNullValue(false).trimStrings(true);// 设置行为
                mapping(HashMap.class, HashMap.class);
                fields(srcField("intValue"), targetField(ONE_OTHER, String.class));
                fields(srcField("integerValue"), targetField(TWO_OTHER, String.class));
                fields(srcField(null), targetField(THREE_OTHER, String.class)).script(
                                                                                      "customFunction:sum(src.intValue,src.integerValue)");
            }

        };

        BeanMapping mapping = new BeanMapping(builder);
        Map src = new HashMap();
        src.put("intValue", 10);
        src.put("integerValue", Integer.valueOf(10));
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);

        Map dest = new HashMap();
        mapping.mapping(src, dest);
        assertEquals(dest.get(ONE_OTHER), "10");
        assertEquals(dest.get(TWO_OTHER), "10");
        assertEquals(dest.get(THREE_OTHER), "20");
    }
}
