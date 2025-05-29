package com.yuelli.jvm;

import tech.medivh.classpy.classfile.bytecode.Instruction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Thread {

    private final String threadName;

    private final JvmStack stack;

    private final PcRegister pcRegister;

    private final BootstrapClassLoader classLoader;

    public Thread(String threadName, StackFrame stackFrame, BootstrapClassLoader classLoader) {
        this.threadName = threadName;
        this.stack = new JvmStack();
        stack.push(stackFrame);
        this.pcRegister = new PcRegister(stack);
        this.classLoader = classLoader;
    }

    // 线程执行pc寄存器中的指令
    public void start() {
        for (Instruction instruction : pcRegister) {

        }
    }
}
