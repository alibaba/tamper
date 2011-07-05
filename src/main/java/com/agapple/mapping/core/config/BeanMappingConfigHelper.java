package com.agapple.mapping.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.agapple.mapping.core.builder.BeanMappingBuilder;

/**
 * Bean Mapping配置操作的相关helper类
 * 
 * @author jianghang 2011-5-28 上午11:00:34
 */
public class BeanMappingConfigHelper {

    private static volatile BeanMappingConfigHelper singleton      = null;
    private BeanMappingConfigRespository            repository     = null; // 基于文件的配置
    private BeanMappingConfigRespository            autoRepository = null; // 自动注册的配置
    private volatile BeanMappingBehavior            globalBehavior = null; // 定义为voolatile，允许动态修改

    public BeanMappingConfigHelper(){
        repository = new BeanMappingConfigRespository();
        autoRepository = new BeanMappingConfigRespository();
        globalBehavior = new BeanMappingBehavior();
    }

    public BeanMappingConfigHelper(BeanMappingConfigRespository repository){
        // 允许传入自定义仓库
        this.repository = repository;
        autoRepository = new BeanMappingConfigRespository();
        globalBehavior = new BeanMappingBehavior();
    }

    /**
     * 单例方法
     */
    public static BeanMappingConfigHelper getInstance() {
        if (singleton == null) {
            synchronized (BeanMappingConfigHelper.class) {
                if (singleton == null) { // double check
                    singleton = new BeanMappingConfigHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * 根据class查找对应的{@linkplain BeanMappingObject}
     */
    public BeanMappingObject getBeanMappingObject(Class src, Class target) {
        return repository.getBeanMappingObject(src, target);
    }

    /**
     * 根据class查找对应的{@linkplain BeanMappingObject}，如果不存在则进行自动注册
     */
    public BeanMappingObject getBeanMappingObject(Class src, Class target, boolean autoRegister) {
        BeanMappingObject object = autoRepository.getBeanMappingObject(src, target);
        if (object == null && autoRegister) {
            autoRepository.register(src, target);
            object = autoRepository.getBeanMappingObject(src, target);
        }
        return object;
    }

    /**
     * 根据class查找对应的{@linkplain BeanMappingObject}，如果不存在则进行自动注册
     */
    public BeanMappingObject getBeanMapObject(Class src, Class target, boolean autoRegister) {
        BeanMappingObject object = autoRepository.getBeanMappingObject(src, target);
        if (object == null && autoRegister) {
            if (src == HashMap.class) {
                autoRepository.registerMap(target);
            } else {
                autoRepository.registerMap(src);
            }
            object = autoRepository.getBeanMappingObject(src, target);
        }
        return object;
    }

    /**
     * 直接注册一个解析好的{@linkplain BeanMappingObject}
     */
    public void register(BeanMappingObject object) {
        repository.register(object);
    }

    /**
     * 直接注册一个解析好的{@linkplain BeanMappingBuilder}
     */
    public void register(BeanMappingBuilder builder) {
        repository.register(builder);
    }

    public void registerConfig(String file) {
        InputStream in = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = BeanMappingConfigRespository.class.getClassLoader();
        }
        in = cl.getResourceAsStream(file);
        // 自己打开的文件需要关闭
        try {
            repository.registerConfig(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public void registerConfig(InputStream in) {
        repository.registerConfig(in);
    }

    // ========================= setter / getter ===================

    public void setRepository(BeanMappingConfigRespository repository) {
        this.repository = repository;
    }

    public BeanMappingBehavior getGlobalBehavior() {
        return globalBehavior;
    }

    public void setGlobalBehavior(BeanMappingBehavior globalBehavior) {
        this.globalBehavior = globalBehavior;
    }

}
