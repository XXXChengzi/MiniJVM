package com.yuelli.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.util.Arrays;


public class Hotspot {

    private String mainClass;

    private BootstrapClassLoader classLoader;

    public Hotspot(String mainClass, String classPathString) {
        this.mainClass = mainClass;
        this.classLoader = new BootstrapClassLoader(Arrays.asList(classPathString.split(File.pathSeparator)));
    }

    // 加载主类
    public void start() throws ClassNotFoundException {
        ClassFile mainClassFile = classLoader.loadClass(mainClass);
    }

}
