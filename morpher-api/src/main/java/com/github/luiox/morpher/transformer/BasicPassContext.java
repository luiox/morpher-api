package com.github.luiox.morpher.transformer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class BasicPassContext implements IPassContext{
    // pass声明周期操作
    private Map<AbstractPass, LifecycleHook> lifecycleHookMap = new HashMap<>();

    /**
     * 生命周期钩子注册
     */
    public LifecycleHook regLifecycleHook(AbstractPass owner) {
        if (!lifecycleHookMap.containsKey(owner)) {
            lifecycleHookMap.put(owner, new LifecycleHook());
        }
        return lifecycleHookMap.get(owner);
    }

    public void runBeforeAllClassesHooks() {
        for (var entry : lifecycleHookMap.entrySet()) {
            var hook = entry.getValue().beforeAllClassesHook;
            if (hook != null) {
                hook.run();
            }
        }
    }

    public void runAfterAllClassesHook() {
        for (var entry : lifecycleHookMap.entrySet()) {
            var hook = entry.getValue().afterAllClassesHook;
            if (hook != null) {
                hook.run();
            }
        }
    }

    public void setNewClassNode(ClassNode newNode) {
        currentClass = newNode;
    }

    public static class LifecycleHook {
        Runnable beforeAllClassesHook;
        Runnable afterAllClassesHook;

        public LifecycleHook beforeAllClasses(Runnable hook) {
            beforeAllClassesHook = hook;
            return this;
        }

        public LifecycleHook afterAllClasses(Runnable hook) {
            afterAllClassesHook = hook;
            return this;
        }
    }

    // 计数器
    private Map<Class<?>, Map<String, AtomicInteger>> passCounters = new HashMap<>();

    public AtomicInteger getCounter(Class<?> clazz, String name) {
        passCounters.computeIfAbsent(clazz, k -> new HashMap<>()).computeIfAbsent(name, k -> new AtomicInteger());
        return passCounters.get(clazz).get(name);
    }

    // 基础的上下文信息
    private ClassNode currentClass;
    // 如果为null就不过滤，其次就是传入一个谓词，用于判断是否需要过滤，如果类名让这个谓词返回true那么就被保留
    private Predicate<String> currentClassFilter;

    private AbstractPass prevPass;

    public void setCurrentClassFilter(Predicate<String> currentClassFilter) {
        this.currentClassFilter = currentClassFilter;
    }

    public Predicate<String> getCurrentClassFilter() {
        return currentClassFilter;
    }

    @Override
    public @NotNull ClassNode currentClass() {
        return currentClass;
    }

    @Override
    public void setCurrentClass(@NotNull ClassNode classNode) {
        currentClass = classNode;
    }

    @Override
    public @Nullable AbstractPass prevPass() {
        return prevPass;
    }

    @Override
    public void setPrevPass(@NotNull AbstractPass pass) {
        prevPass = pass;
    }
}
