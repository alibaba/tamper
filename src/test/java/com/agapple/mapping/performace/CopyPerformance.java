package com.agapple.mapping.performace;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.agapple.mapping.BeanCopy;

/**
 * BeanCopier , Beanutils/PropertyUtils , BeanMapping几种机制的copy操作的性能测试
 * 
 * @author jianghang 2011-5-31 下午02:44:40
 */
public class CopyPerformance extends AbstractPerformance {

    public static void main(String args[]) throws Exception {
        final int testCount = 1000 * 100 * 20;
        CopyBean bean = getBean();
        // BeanMapping copy测试
        final CopyBean beanMappingTarget = new CopyBean();
        final BeanCopy beanCopy = BeanCopy.create(CopyBean.class, CopyBean.class);
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanMapping.copy";
            }

            public CopyBean call(CopyBean source) {
                try {
                    beanCopy.copy(source, beanMappingTarget);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanMappingTarget;
            }

        }, bean, testCount);
        // method反射测试
        List<Method> getMethodList = getGetMethod();
        List<Method> setMethodList = getSetMethod();
        final Method[] getterMethods = getMethodList.toArray(new Method[getMethodList.size()]);
        final Method[] setterMethods = setMethodList.toArray(new Method[setMethodList.size()]);
        final CopyBean methodCopierTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "Method Copy";
            }

            public CopyBean call(CopyBean source) {
                try {
                    for (int i = 0; i < getterMethods.length; i++) {
                        Object temp = getterMethods[i].invoke(source);
                        setterMethods[i].invoke(methodCopierTarget, temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return methodCopierTarget;
            }
        }, bean, testCount);
        // fastMethod反射测试
        List<FastMethod> getFastMethodList = getGetFastMethod();
        List<FastMethod> setFastMethodList = getSetFastMethod();
        final FastMethod[] getterFastMethods = getFastMethodList.toArray(new FastMethod[getFastMethodList.size()]);
        final FastMethod[] setterFastMethods = setFastMethodList.toArray(new FastMethod[setFastMethodList.size()]);
        final CopyBean fastMethodCopierTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "FastMethod Copy";
            }

            public CopyBean call(CopyBean source) {
                try {
                    for (int i = 0; i < getterFastMethods.length; i++) {
                        Object temp = getterFastMethods[i].invoke(source, new Object[] {});
                        setterFastMethods[i].invoke(fastMethodCopierTarget, new Object[] { temp });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return methodCopierTarget;
            }
        }, bean, testCount);
        // bulkbean测试
        String[] getters = new String[] { "getIntValue", "isBoolValue", "getFloatValue", "getDoubleValue",
                "getLongValue", "getCharValue", "getShortValue", "getByteValue", "getIntegerValue", "getBoolObjValue",
                "getFloatObjValue", "getDoubleObjValue", "getLongObjValue", "getCharacterValue", "getShortObjValue",
                "getByteObjValue", "getBigIntegerValue", "getBigDecimalValue", "getStringValue" };
        String[] setters = new String[] { "setIntValue", "setBoolValue", "setFloatValue", "setDoubleValue",
                "setLongValue", "setCharValue", "setShortValue", "setByteValue", "setIntegerValue", "setBoolObjValue",
                "setFloatObjValue", "setDoubleObjValue", "setLongObjValue", "setCharacterValue", "setShortObjValue",
                "setByteObjValue", "setBigIntegerValue", "setBigDecimalValue", "setStringValue" };
        Class[] clazzes = new Class[] { int.class, boolean.class, float.class, double.class, long.class, char.class,
                short.class, byte.class, Integer.class, Boolean.class, Float.class, Double.class, Long.class,
                Character.class, Short.class, Byte.class, BigInteger.class, BigDecimal.class, String.class };
        final BulkBean bulkBean = BulkBean.create(CopyBean.class, getters, setters, clazzes);
        final CopyBean bulkBeanTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BulkBean";
            }

            public CopyBean call(CopyBean source) {
                Object[] values = bulkBean.getPropertyValues(source);
                bulkBean.setPropertyValues(bulkBeanTarget, values);
                return bulkBeanTarget;
            }
        }, bean, testCount);
        // beanCopier测试
        final BeanCopier beanCopier = BeanCopier.create(CopyBean.class, CopyBean.class, false);
        final CopyBean beanCopierTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanCopier";
            }

            public CopyBean call(CopyBean source) {
                beanCopier.copy(source, beanCopierTarget, null);
                return beanCopierTarget;
            }
        }, bean, testCount);
        // HardCode测试
        final CopyBean hardCodeTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "HardCopy";
            }

            public CopyBean call(CopyBean source) {
                hardCodeTarget.setIntValue(source.getIntValue());
                hardCodeTarget.setBoolValue(source.isBoolValue());
                hardCodeTarget.setFloatValue(source.getFloatValue());
                hardCodeTarget.setDoubleValue(source.getDoubleValue());
                hardCodeTarget.setLongValue(source.getLongValue());
                hardCodeTarget.setCharValue(source.getCharValue());
                hardCodeTarget.setShortValue(source.getShortValue());
                hardCodeTarget.setByteValue(source.getByteValue());
                hardCodeTarget.setIntegerValue(source.getIntegerValue());
                hardCodeTarget.setBoolObjValue(source.getBoolObjValue());
                hardCodeTarget.setFloatObjValue(source.getFloatObjValue());
                hardCodeTarget.setDoubleObjValue(source.getDoubleObjValue());
                hardCodeTarget.setLongObjValue(source.getLongObjValue());
                hardCodeTarget.setCharacterValue(source.getCharacterValue());
                hardCodeTarget.setShortObjValue(source.getShortObjValue());
                hardCodeTarget.setByteObjValue(source.getByteObjValue());
                hardCodeTarget.setBigIntegerValue(source.getBigIntegerValue());
                hardCodeTarget.setBigDecimalValue(source.getBigDecimalValue());
                hardCodeTarget.setStringValue(source.getStringValue());
                return hardCodeTarget;
            }
        }, bean, testCount);

        // PropertyUtils测试
        final CopyBean propertyUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "PropertyUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    PropertyUtils.copyProperties(propertyUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return propertyUtilsTarget;
            }

        }, bean, testCount);
        // BeanUtils测试
        final CopyBean beanUtilsTarget = new CopyBean();
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanUtils";
            }

            public CopyBean call(CopyBean source) {
                try {
                    BeanUtils.copyProperties(beanUtilsTarget, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanUtilsTarget;
            }

        }, bean, testCount);

    }

    private static List<Method> getGetMethod() {
        List<Method> result = new ArrayList<Method>();
        result.add(getMethod("getIntValue", new Class[] {}));
        result.add(getMethod("isBoolValue", new Class[] {}));
        result.add(getMethod("getFloatValue", new Class[] {}));
        result.add(getMethod("getDoubleValue", new Class[] {}));
        result.add(getMethod("getLongValue", new Class[] {}));
        result.add(getMethod("getCharValue", new Class[] {}));
        result.add(getMethod("getShortValue", new Class[] {}));
        result.add(getMethod("getByteValue", new Class[] {}));
        result.add(getMethod("getIntegerValue", new Class[] {}));
        result.add(getMethod("getBoolObjValue", new Class[] {}));
        result.add(getMethod("getFloatObjValue", new Class[] {}));
        result.add(getMethod("getDoubleObjValue", new Class[] {}));
        result.add(getMethod("getLongObjValue", new Class[] {}));
        result.add(getMethod("getCharacterValue", new Class[] {}));
        result.add(getMethod("getShortObjValue", new Class[] {}));
        result.add(getMethod("getByteObjValue", new Class[] {}));
        result.add(getMethod("getBigIntegerValue", new Class[] {}));
        result.add(getMethod("getBigDecimalValue", new Class[] {}));
        result.add(getMethod("getStringValue", new Class[] {}));
        return result;
    }

    private static List<Method> getSetMethod() {
        List<Method> result = new ArrayList<Method>();
        result.add(getMethod("setIntValue", new Class[] { int.class }));
        result.add(getMethod("setBoolValue", new Class[] { boolean.class }));
        result.add(getMethod("setFloatValue", new Class[] { float.class }));
        result.add(getMethod("setDoubleValue", new Class[] { double.class }));
        result.add(getMethod("setLongValue", new Class[] { long.class }));
        result.add(getMethod("setCharValue", new Class[] { char.class }));
        result.add(getMethod("setShortValue", new Class[] { short.class }));
        result.add(getMethod("setByteValue", new Class[] { byte.class }));
        result.add(getMethod("setIntegerValue", new Class[] { Integer.class }));
        result.add(getMethod("setBoolObjValue", new Class[] { Boolean.class }));
        result.add(getMethod("setFloatObjValue", new Class[] { Float.class }));
        result.add(getMethod("setDoubleObjValue", new Class[] { Double.class }));
        result.add(getMethod("setLongObjValue", new Class[] { Long.class }));
        result.add(getMethod("setCharacterValue", new Class[] { Character.class }));
        result.add(getMethod("setShortObjValue", new Class[] { Short.class }));
        result.add(getMethod("setByteObjValue", new Class[] { Byte.class }));
        result.add(getMethod("setBigIntegerValue", new Class[] { BigInteger.class }));
        result.add(getMethod("setBigDecimalValue", new Class[] { BigDecimal.class }));
        result.add(getMethod("setStringValue", new Class[] { String.class }));
        return result;
    }

    private static List<FastMethod> getGetFastMethod() {
        List<FastMethod> result = new ArrayList<FastMethod>();
        FastClass fc = FastClass.create(CopyBean.class);
        List<Method> methods = getGetMethod();
        for (Method method : methods) {
            result.add(fc.getMethod(method));
        }
        return result;
    }

    private static List<FastMethod> getSetFastMethod() {
        List<FastMethod> result = new ArrayList<FastMethod>();
        FastClass fc = FastClass.create(CopyBean.class);
        List<Method> methods = getSetMethod();
        for (Method method : methods) {
            result.add(fc.getMethod(method));
        }
        return result;
    }
}
