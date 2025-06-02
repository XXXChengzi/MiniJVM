package com.yuelli.customer;

import java.io.*;

public class MyClassLoader extends ClassLoader{

    String rootDir;

    public MyClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    protected Class<?> findClass(String sqcn) throws ClassNotFoundException {
        // 调用defineClass方法将Byte数组转换为Class对象
        byte[] data = getBytesBySqcn(sqcn);
        return defineClass(sqcn, data, 0, data.length);
    }
    // sqcn: 类的全限定名
    private byte[] getBytesBySqcn(String sqcn) {
        // 1.根据类名获取类的字节码
        String fileName = rootDir + File.separator + sqcn.replace(".", File.separator) + ".class";
        // 2.将字节码文件读取为Byte数组
        try (InputStream is = new FileInputStream(fileName)) {
            int len = is.available();
            byte[] bytes = new byte[len];
            is.read(bytes);
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyClassLoader myClassLoader = new MyClassLoader("/Users/bytedance/IdeaProjects/MiniJVM/target/classes");
        Class<?> aClass = myClassLoader.findClass("com.yuelli.customer.User");
        Object o = aClass.newInstance();
        System.out.println(o.getClass());
        User user = new User();
        System.out.println(o instanceof User);
        System.out.println(o.getClass().getClassLoader());
        System.out.println(user.getClass().getClassLoader());

    }
}
