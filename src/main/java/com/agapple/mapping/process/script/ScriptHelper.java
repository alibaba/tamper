package com.agapple.mapping.process.script;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.agapple.mapping.core.helper.ReflectionHelper;
import com.agapple.mapping.process.script.jexl.JexlScriptExecutor;

/**
 * script的function class操作helper类
 * 
 * @author jianghang 2011-6-27 下午07:32:25
 */
public class ScriptHelper {

    private static final String          DEFAULT_SCRIPT = JexlScriptExecutor.class.getName();
    private static final String          property       = "BeanMapping.Script.Driver";
    private static volatile ScriptHelper singleton      = null;
    private volatile ScriptExecutor      executor       = null;

    public ScriptHelper(){
    }

    /**
     * 单例方法
     */
    public static ScriptHelper getInstance() {
        if (singleton == null) {
            synchronized (ScriptHelper.class) {
                if (singleton == null) { // double check
                    singleton = new ScriptHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * @return 返回对应的{@linkplain ScriptExecutor}
     */
    public ScriptExecutor getScriptExecutor() {
        if (executor == null) {
            synchronized (ScriptHelper.class) {
                if (executor == null) {
                    executor = createScriptExecutor();
                }
            }
        }

        return executor;
    }

    /**
     * 注册对应的function，并绑定为指定的name
     */
    public void registerFunctionClass(String name, Object function) {
        getScriptExecutor().addFunction(name, function);
    }

    /**
     * 创建ScriptExecutor
     * 
     * <pre>
     * 1. 从jvm system property加载对应的classname
     * 2. 从META-INF/services/加载对应的classname
     * </pre>
     */
    private ScriptExecutor createScriptExecutor() {
        String className = null;
        ClassLoader loader = ScriptExecutor.class.getClassLoader();

        // 1. try the JVM-instance-wide system property
        try {
            className = System.getProperty(property);
        } catch (RuntimeException e) { /* normally fails for applets */
        }

        // 2. if that fails, try META-INF/services/
        if (className == null) {
            try {
                String service = "META-INF/services/" + property;
                InputStream in;
                BufferedReader reader;

                if (loader == null) {
                    in = ClassLoader.getSystemResourceAsStream(service);
                } else {
                    in = loader.getResourceAsStream(service);
                }

                if (in != null) {
                    reader = new BufferedReader(new InputStreamReader(in, System.getProperty("file.encoding", "UTF-8")));
                    className = reader.readLine();
                    in.close();
                }
            } catch (Exception e) {
            }
        }

        // 3. Distro-specific fallback
        if (className == null) {
            className = DEFAULT_SCRIPT;
        }

        Class clazz = ReflectionHelper.forName(className, loader);
        return (ScriptExecutor) ReflectionHelper.newInstance(clazz);
    }

}
