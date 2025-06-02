package com.yuelli.jvm;


import tech.medivh.classpy.classfile.bytecode.Instruction;

import java.util.Iterator;

/// 程序计数器
/// 线程执行pc寄存器中的指令
public class PcRegister implements Iterable<Instruction>{

    private final JvmStack jvmStack;

    public PcRegister(JvmStack jvmStack) {
        this.jvmStack = jvmStack;
    }

    public Iterator<Instruction> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<Instruction> {

        @Override
        public boolean hasNext() {
            return !jvmStack.isEmpty();
        }

        @Override
        public Instruction next() {
            StackFrame stackFrame = jvmStack.peek();
            return stackFrame.getNextInstruction();
        }
    }
}
