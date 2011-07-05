package com.agapple.mapping.process.convetor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.agapple.mapping.BeanCopy;
import com.agapple.mapping.BeanMap;
import com.agapple.mapping.BeanMapping;
import com.agapple.mapping.core.BeanMappingException;
import com.agapple.mapping.core.helper.ReflectionHelper;

/**
 * Collection <-> Collection 的转化器 , Collection范围包括Array(几种原型数组),List,Set各种实现类
 * 
 * @author jianghang 2011-5-26 上午09:23:00
 */
public class CollectionAndCollectionConvertor {

    public static abstract class BaseCollectionConvertor extends AbastactConvertor {

        @Override
        public Object convert(Object src, Class destClass) {
            return convertCollection(src, destClass, getComponentClass(src, destClass));
        }

        protected abstract Class getComponentClass(Object src, Class destClass);

        protected MappingConfig initMapping(Class srcClass, Class targetClass, Class[] componentClasses) {
            MappingConfig config = new MappingConfig();
            // 1. 先找mapping配置中的映射
            // 2. 尝试处理下BeanCopy和BeanMap配置
            // 先找一次转化,可能是原始类型
            // 多级数组映射暂不考虑，TMD太复杂了，需要客户端自定义转化器
            config.convertor = ConvertorHelper.getInstance().getConvertor(srcClass, targetClass);
            if (config.convertor != null) {
                if (componentClasses != null && componentClasses.length > 1) {// 处理嵌套的componentClass传递
                    Class[] newComponentClasses = new Class[componentClasses.length - 1];
                    config.componentClasses = newComponentClasses;
                    System.arraycopy(componentClasses, 1, newComponentClasses, 0, componentClasses.length - 1);// 拷贝n-1的长度
                }
            } else {
                try {
                    config.beanMapping = BeanMapping.create(srcClass, targetClass);
                } catch (BeanMappingException e) {
                    // 处理下分支2
                    boolean isSrcMap = Map.class.isAssignableFrom(srcClass);
                    boolean isTargetMap = Map.class.isAssignableFrom(targetClass);
                    if (isSrcMap && isTargetMap) { // 如果都为Map对象
                        config.isMap = true;
                    } else if (isSrcMap) {
                        config.beanMap = BeanMap.create(targetClass);
                        config.populate = true;
                    } else if (isTargetMap) {
                        config.beanMap = BeanMap.create(srcClass);
                        config.populate = false;
                    } else {
                        config.beanCopy = BeanCopy.create(srcClass, targetClass); // 默认进行类型转化
                    }

                }
            }
            return config;
        }

        protected Object doMapping(Object src, Class targetClass, MappingConfig config) {
            Object newObj = null;
            if (config.convertor != null) {
                if (config.componentClasses != null) {
                    newObj = config.convertor.convertCollection(src, targetClass, config.componentClasses); // 处理递归嵌套
                } else {
                    newObj = config.convertor.convert(src, targetClass);
                }

            } else {
                if (config.isMap) {
                    newObj = createMap(targetClass);
                    ((Map) newObj).putAll((Map) src);
                } else {
                    newObj = ReflectionHelper.newInstance(targetClass);
                    if (config.beanMapping != null) {
                        config.beanMapping.mapping(src, newObj);
                    } else if (config.beanCopy != null) {
                        config.beanCopy.copy(src, newObj);
                    } else if (config.beanMap != null) {
                        if (config.populate) {
                            config.beanMap.populate(newObj, (Map) src);
                        } else {
                            newObj = config.beanMap.describe(src);
                        }
                    }
                }
            }

            return newObj;
        }

        protected Map createMap(Class destClass) {
            if (destClass == Map.class || destClass == HashMap.class) {
                return new HashMap();
            }

            if (destClass == TreeMap.class) {
                return new TreeMap();
            }

            if (destClass == LinkedHashMap.class) {
                return new LinkedHashMap();
            }

            throw new BeanMappingException("Unsupported Map: [" + destClass.getName() + "]");

        }

        protected Collection createCollection(Class destClass) {
            if (destClass == List.class || destClass == ArrayList.class) {
                return new ArrayList(); // list默认为ArrayList
            }

            if (destClass == LinkedList.class) {
                return new LinkedList();
            }

            if (destClass == Vector.class) {
                return new Vector();
            }

            if (destClass == Set.class || destClass == HashSet.class) {
                return new HashSet(); // set默认为HashSet
            }

            if (destClass == LinkedHashSet.class) {
                return new LinkedHashSet();
            }

            if (destClass == TreeSet.class) {
                return new TreeSet();
            }

            throw new BeanMappingException("Unsupported Collection: [" + destClass.getName() + "]");
        }

        protected void arraySet(Object src, Class compoentType, int i, Object value) {
            Array.set(src, i, value);
        }

        protected Object arrayGet(Object src, Class compoentType, int i) {
            return Array.get(src, i);
        }
    }

    /**
     * Collection -> Collection 转化
     */
    public static class CollectionToCollection extends BaseCollectionConvertor {

        public Object convertCollection(Object src, Class destClass, Class... componentClasses) {
            if (Collection.class.isAssignableFrom(src.getClass()) && Collection.class.isAssignableFrom(destClass)) { // 必须都是Collection
                Collection collection = (Collection) src;
                Collection target = createCollection(destClass);

                boolean isInit = false;
                MappingConfig config = null;

                Class componentClass = null;
                if (componentClasses != null && componentClasses.length >= 1) {
                    componentClass = componentClasses[0];
                }

                for (Iterator iter = collection.iterator(); iter.hasNext();) {
                    Object item = iter.next();
                    Class componentSrcClass = item.getClass();
                    if (componentClass != null && componentSrcClass != componentClass) {
                        if (isInit == false) {
                            isInit = true;
                            config = initMapping(componentSrcClass, componentClass, componentClasses);
                        }

                        // 添加为mapping convertor
                        target.add(doMapping(item, componentClass, config));
                    } else {
                        target.add(item);
                    }
                }
                return target;
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

        @Override
        protected Class getComponentClass(Object src, Class destClass) {
            if (Collection.class.isAssignableFrom(src.getClass()) && Collection.class.isAssignableFrom(destClass)) { // 必须都是Collection
                Collection collection = (Collection) src;
                for (Iterator iter = collection.iterator(); iter.hasNext();) {
                    Object item = iter.next();
                    if (item != null) {
                        return item.getClass();
                    }
                }
            }

            return null;
        }
    }

    /**
     * array -> array 转化
     */
    public static class ArrayToArray extends BaseCollectionConvertor {

        public Object convertCollection(Object src, Class destClass, Class... componentClasses) {
            if (src.getClass().isArray() && destClass.isArray()) { // 特殊处理下数组
                int size = Array.getLength(src);
                Class componentSrcClass = src.getClass().getComponentType();
                Class componentDestClass = destClass.getComponentType();
                MappingConfig config = null;
                Object[] objs = (Object[]) Array.newInstance(componentDestClass, size);

                Class componentClass = null;
                if (componentClasses != null && componentClasses.length >= 1) {
                    componentClass = componentClasses[0];
                }

                if (componentDestClass != componentClass) {
                    throw new BeanMappingException("error ComponentClasses config for [" + componentDestClass.getName()
                                                   + "] to [" + componentDestClass.getName() + "]");
                }

                if (componentClass != null && componentSrcClass != componentClass) { // 需要进行类型转化
                    config = initMapping(componentSrcClass, componentDestClass, componentClasses);
                }
                for (int i = 0; i < size; i++) {
                    Object obj = arrayGet(src, componentSrcClass, i);
                    if (config != null) { // 需要进行类型转化
                        Object newObj = doMapping(obj, componentDestClass, config);
                        arraySet(objs, componentDestClass, i, newObj);
                    } else {
                        arraySet(objs, componentDestClass, i, obj);
                    }
                }
                return objs;
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

        @Override
        protected Class getComponentClass(Object src, Class destClass) {
            if (src.getClass().isArray() && destClass.isArray()) { // 特殊处理下数组
                return src.getClass().getComponentType();
            }
            return null;
        }
    }

    /**
     * array -> Collection 转化
     */
    public static class ArrayToCollection extends BaseCollectionConvertor {

        public Object convertCollection(Object src, Class destClass, Class... componentClasses) {
            if (src.getClass().isArray() && Collection.class.isAssignableFrom(destClass)) { // 特殊处理下数组
                Collection target = createCollection(destClass);
                int size = Array.getLength(src);
                Class componentSrcClass = src.getClass().getComponentType();

                MappingConfig config = null;
                Class componentClass = null;
                if (componentClasses != null && componentClasses.length >= 1) {
                    componentClass = componentClasses[0];
                }

                if (componentClass != null && componentSrcClass != componentClass) { // 处理下类型转化
                    config = initMapping(componentSrcClass, componentClass, componentClasses);
                }

                for (int i = 0; i < size; i++) {
                    Object obj = arrayGet(src, componentSrcClass, i);
                    if (config != null) { // 需要进行类型转化
                        Object newObj = doMapping(obj, componentClass, config);
                        target.add(newObj);
                    } else {
                        target.add(obj);
                    }
                }
                return target;
            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

        @Override
        protected Class getComponentClass(Object src, Class destClass) {
            if (src.getClass().isArray() && Collection.class.isAssignableFrom(destClass)) { // 特殊处理下数组
                return src.getClass().getComponentType();
            }

            return null;
        }

    }

    /**
     * Collection -> array 转化
     */
    public static class CollectionToArray extends BaseCollectionConvertor {

        public Object convertCollection(Object src, Class destClass, Class... componentClasses) {
            if (Collection.class.isAssignableFrom(src.getClass()) && destClass.isArray()) {
                Collection collection = (Collection) src;
                Class componentDestClass = destClass.getComponentType();
                Object objs = Array.newInstance(componentDestClass, collection.size());

                boolean isInit = false;
                MappingConfig config = null;
                Class componentClass = null;
                if (componentClasses != null && componentClasses.length >= 1) {
                    componentClass = componentClasses[0];
                }

                if (componentDestClass != componentClass) {
                    throw new BeanMappingException("error ComponentClasses config for [" + componentDestClass.getName()
                                                   + "] to [" + componentDestClass.getName() + "]");
                }

                int i = 0;
                for (Iterator iter = collection.iterator(); iter.hasNext();) {
                    Object item = iter.next();
                    Class componentSrcClass = item.getClass();
                    if (componentClass != null && componentSrcClass != componentDestClass) {
                        if (isInit == false) {
                            config = initMapping(componentSrcClass, componentDestClass, componentClasses);
                        }

                        Object newObj = doMapping(item, componentDestClass, config);
                        arraySet(objs, componentDestClass, i, newObj);
                    } else {
                        arraySet(objs, componentDestClass, i, item);
                    }
                    i = i + 1;
                }
                return objs;

            }

            throw new BeanMappingException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

        @Override
        protected Class getComponentClass(Object src, Class destClass) {
            if (Collection.class.isAssignableFrom(src.getClass()) && destClass.isArray()) {
                return destClass.getComponentType();// 以目标地址为准
            }

            return null;
        }
    }

}

class MappingConfig {

    Convertor   convertor        = null;
    BeanMapping beanMapping      = null;
    BeanCopy    beanCopy         = null;
    BeanMap     beanMap          = null;
    boolean     populate         = false;
    boolean     isMap            = false;
    Class[]     componentClasses = null;

}
