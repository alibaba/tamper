package com.agapple.mapping.performace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.agapple.mapping.BeanMap;

/**
 * @author jianghang 2011-6-10 下午04:20:31
 */
public class MapPerformance extends AbstractPerformance {

    public static void main(String args[]) throws Exception {
        final int testCount = 1000 * 100 * 20;
        CopyBean bean = getBean();
        // BeanMap测试
        final CopyBean beanMapTarget = new CopyBean();
        final BeanMap beanMap = BeanMap.create(CopyBean.class);
        testTemplate(new TestCallback() {

            public String getName() {
                return "BeanMap";
            }

            public CopyBean call(CopyBean source) {
                try {
                    Map result = beanMap.describe(source);
                    beanMap.populate(beanMapTarget, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanMapTarget;
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
                    Map result = BeanUtils.describe(source);
                    BeanUtils.populate(beanUtilsTarget, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanUtilsTarget;
            }

        }, bean, testCount);
        // Cglib测试
        final CopyBean cglibTarget = new CopyBean();
        final net.sf.cglib.beans.BeanMap cglibBeanMap = net.sf.cglib.beans.BeanMap.create(bean);
        testTemplate(new TestCallback() {

            public String getName() {
                return "Cglib.BeanMap";
            }

            public CopyBean call(CopyBean source) {
                try {
                    cglibBeanMap.setBean(source);
                    Set set = cglibBeanMap.keySet();
                    Map result = new HashMap();
                    for (Iterator iter = set.iterator(); iter.hasNext();) {
                        Object key = iter.next();
                        result.put(key, cglibBeanMap.get(key));
                    }

                    cglibBeanMap.setBean(cglibTarget);
                    cglibBeanMap.putAll(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return beanUtilsTarget;
            }

        }, bean, testCount);

    }
}
