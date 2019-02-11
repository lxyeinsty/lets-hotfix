package com.github.lzy.hotfix;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;

import sun.misc.ClassLoaderUtil;

/**
 * @author liuzhengyang
 */
public class HotfixAgent {
    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
        if (agentArgs == null) {
            throw new IllegalArgumentException(agentArgs);
        }
        String[] splits = agentArgs.split(",");
        if (splits.length < 2) {
            throw new IllegalArgumentException(agentArgs);
        }
        System.out.println("Current Class loader " + HotfixAgent.class.getClassLoader());
        String className = splits[0];
        String replaceTargetClassFile = splits[1];
        Class<?> clazz = findTargetClass(className, instrumentation);
        File file = Paths.get(replaceTargetClassFile).toFile();
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] newClazzByteCode = new byte[inputStream.available()];
            inputStream.read(newClazzByteCode);
            instrumentation.redefineClasses(new ClassDefinition(clazz, newClazzByteCode));
            System.out.println("Redefine done " + clazz);
        }
    }

    private static Class<?> findTargetClass(String className, Instrumentation instrumentation) {
        Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : allLoadedClasses) {
            if (className.equals(clazz.getCanonicalName())) {
                System.out.println("Found class " + clazz + " class loader " + clazz.getClassLoader());
                return clazz;
            }
        }
        return null;
    }
}
