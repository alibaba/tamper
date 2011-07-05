package com.agapple.mapping.performace;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * @author jianghang 2011-6-10 下午04:21:33
 */
public class AbstractPerformance {

    protected static final DecimalFormat integerFormat = new DecimalFormat("#,###");

    protected static void testTemplate(TestCallback callback, CopyBean source, int count) {
        int warmup = 10070;
        // 先进行预热，加载一些类，避免影响测试
        for (int i = 0; i < warmup; i++) {
            callback.call(source);
        }
        restoreJvm(); // 进行GC回收
        // 进行测试
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            callback.call(source);
        }
        long nscost = (System.nanoTime() - start);
        System.out.println(callback.getName() + " total cost=" + integerFormat.format(nscost) + "ns , each cost="
                           + nscost / count + "ns");
        restoreJvm();// 进行GC回收

    }

    protected static CopyBean getBean() {
        CopyBean bean = new CopyBean();
        bean.setIntValue(1);
        bean.setBoolValue(false);
        bean.setFloatValue(1.0f);
        bean.setDoubleValue(1.0d);
        bean.setLongValue(1l);
        bean.setCharValue('a');
        bean.setShortValue((short) 1);
        bean.setByteValue((byte) 1);
        bean.setIntegerValue(new Integer("1"));
        bean.setBoolObjValue(new Boolean("false"));
        bean.setFloatObjValue(new Float("1.0"));
        bean.setDoubleObjValue(new Double("1.0"));
        bean.setLongObjValue(new Long("1"));
        bean.setCharacterValue('a');
        bean.setShortObjValue(new Short("1"));
        bean.setByteObjValue(new Byte("1"));
        bean.setBigIntegerValue(new BigInteger("1"));
        bean.setBigDecimalValue(new BigDecimal("1"));
        bean.setStringValue("1");
        return bean;
    }

    protected static Method getMethod(String methodName, Class... type) {
        try {
            return CopyBean.class.getMethod(methodName, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void restoreJvm() {
        int maxRestoreJvmLoops = 10;
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();

            long memUsedNow = memoryUsed();
            // break early if have no more finalization and get constant mem used
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                && (memUsedNow >= memUsedPrev)) {
                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }
    }

    protected static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

}

interface TestCallback {

    String getName();

    CopyBean call(CopyBean source);
}
