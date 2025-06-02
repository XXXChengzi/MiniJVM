package com.yuelli.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.GetStatic;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.bytecode.InvokeVirtual;
import tech.medivh.classpy.classfile.constant.ConstantMethodrefInfo;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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

    private Class<?> nameToClass(String className) {
        if (className == "int") {
            return int.class;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    // 线程执行pc寄存器中的指令
    public void start() throws Exception {
        for (Instruction instruction : pcRegister) {
            System.out.println(instruction);
            ConstantPool constantPool = stack.peek().constantPool;
            switch (instruction.getOpcode()) {
                case getstatic -> {
                    // 获取属性，然后把这个属性push到操作数栈顶
                    GetStatic getstatic = (GetStatic) instruction;
                    // 获取常量池：栈帧->函数->类->常量池
                    String className = getstatic.getClassName(constantPool);
                    String fieldName = getstatic.getFieldName(constantPool);
                    Object staticField;
                    // 简化操作：如果是Java的内部类就直接利用反射加载
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Field field = aClass.getDeclaredField(fieldName);
                        staticField = field.get(null);
                        // 把该属性push到操作数栈顶
                        stack.peek().pushObjectToOprandStack(staticField);
                    }
                }
                case invokevirtual -> {
                    // 调用方法
                    InvokeVirtual invokeVirtual = (InvokeVirtual) instruction;
                    ConstantMethodrefInfo methodInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> params = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        // 简化操作：如果是Java的内部类就直接利用反射加载
                        Class<?> aClass = Class.forName(className);
                        // 通过stream流根据参数的类名转化为Class类型数组
                        Method declaredMethod = aClass.getDeclaredMethod(methodName,
                                params.stream().map(this::nameToClass).toArray(Class[]::new));
                        // 从操作数栈中获取参数
                        Object[] args = new Object[params.size()];
                        for (int index = args.length - 1; index >= 0; index--) {
                            args[index] = stack.peek().operandStack.pop();
                        }
                        // 调用方法
                        Object result = declaredMethod.invoke(stack.peek().operandStack.pop(), args);
                        // 把返回值push到操作数栈顶
                        if (!methodInfo.isVoid(constantPool)) {
                            stack.peek().pushObjectToOprandStack(result);
                        }
                        break;
                    }
                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    // 参数中还包含 this 所以是params.size()+1
                    Object[] args = new Object[params.size() + 1];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stack.peek().operandStack.pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }
                case iconst_1 -> {
                    // 把int 1push到栈顶
                    stack.peek().pushObjectToOprandStack(1);
                }
                case _return -> {
                    // 当前栈帧执行结束，弹出栈帧
                    stack.pop();
                }
            }
        }
    }
}
