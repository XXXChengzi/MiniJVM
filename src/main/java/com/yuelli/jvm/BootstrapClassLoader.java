package com.yuelli.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class BootstrapClassLoader {

    private final List<String> classPath;

    public BootstrapClassLoader(List<String> classPath) {
        this.classPath = classPath;
    }

    // fqcn: 全限定名
    public ClassFile loadClass(String fqcn) throws ClassNotFoundException {
        return classPath.stream()
                .map(path -> tryLoad(path, fqcn))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ClassNotFoundException(fqcn + "未找到"));
    }

    // 尝试加载
    private ClassFile tryLoad(String path, String fqcn) {
        // 1.获取类路径,拼接成为 a.b.c -> a/b/c.class
        File mainClassPath = new File(path, fqcn.replace(".", File.separator) + ".class");
        if (!mainClassPath.exists()) {
            return null;
        }
        try {
            // 2.根据字节码文件，加载类
            byte[] bytes = Files.readAllBytes(mainClassPath.toPath());
            return new ClassFileParser().parse(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
