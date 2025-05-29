package com.yuelli.jvm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/// Jvm栈:其中存储的是栈帧
public class JvmStack {

    private Deque<StackFrame> stack = new ArrayDeque<>();

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void push(StackFrame stackFrame) {
        stack.push(Objects.requireNonNull(stackFrame));
    }

    public StackFrame pop() {
        return stack.pop();
    }

    public StackFrame peek() {
        return stack.peek();
    }
}
