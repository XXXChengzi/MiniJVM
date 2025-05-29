package com.yuelli.jvm;

import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.constant.ConstantPool;


import java.util.*;

/// Jvm栈中的一个栈帧，其中包括局部变量表和操作数栈，以及当前指令的位置
/// 栈帧是Jvm执行方法的基本单位
public class StackFrame {

    int curIndex;
    final MethodInfo methodInfo;
    final Object[] localVariables;
    final Deque<Object> operandStack;
    final ConstantPool constantPool;
    final List<Instruction> codes;

    public StackFrame(MethodInfo methodInfo, ConstantPool constantPool, Object... args) {
        this.methodInfo = methodInfo;
        this.constantPool = constantPool;
        this.codes = methodInfo.getCodes();
        this.curIndex = 0;
        this.localVariables = new Object[methodInfo.getMaxLocals()];
        this.operandStack = new ArrayDeque<>();
        System.arraycopy(args, 0, localVariables, 0, args.length);
    }



    // 获取下一个指令
    public Instruction getNextInstruction() {
        return codes.get(curIndex++);
    }

    // 将值压入操作数栈栈顶
    public void pushObjectToOprandStack(Object object) {
        operandStack.push(object);
    }

    // 支持指令跳转，根据Instruction中的编号跳转到对应的指令
    public void jumpTo(int index) {
        for (int i = 0; i < codes.size(); i++) {
            Instruction instruction = codes.get(i);
            if (instruction.getPc() == index) {
                curIndex = i;
                break;
            }
        }
    }
}
