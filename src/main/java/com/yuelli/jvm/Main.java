package com.yuelli.jvm;

public class Main {
    public static void main(String[] args) throws Exception {
        Hotspot hotspot = new Hotspot("com.yuelli.code.Demo",
                "/Users/bytedance/IdeaProjects/MiniJVM/target/classes;");
        hotspot.start();
    }
}
