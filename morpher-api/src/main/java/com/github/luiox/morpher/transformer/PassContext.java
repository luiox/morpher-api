package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.jar.IJarCaches;
import com.github.luiox.morpher.jar.JarCachesEntry;
import com.github.luiox.morpher.jar.JarCachesEntryType;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class PassContext {
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
    public IJarCaches jarCaches;
    public ClassNode currentClass;
    // 如果为null就不过滤，其次就是传入一个谓词，用于判断是否需要过滤，如果类名让这个谓词返回true那么就被保留
    public Predicate<String> currentClassFilter;
    // 是否需要继续循环，如果设置为true，那么就会再来一遍
    private boolean needContinueLoop;

    public PassContext() {
        this(false);
    }

    public PassContext(boolean fullLoad) {
    }

    public void setCurrentClassFilter(Predicate<String> currentClassFilter) {
        this.currentClassFilter = currentClassFilter;
    }

    public Predicate<String> getCurrentClassFilter() {
        return currentClassFilter;
    }

    public void setNeedContinueLoop(boolean val) {
        this.needContinueLoop = val;
    }

    public boolean isNeedContinueLoop() {
        return needContinueLoop;
    }

    // class name
    private List<String> classDeleteList = new ArrayList<>();
    // class name -> byte[]
    private Map<String,byte[]> classAddList = new HashMap<>();

    public void deleteClassNode(String className) {
        classDeleteList.add(className);
    }

    public void deleteClassNode(@NotNull ClassNode classNode) {
        classDeleteList.add(classNode.name);
    }

    public void addClassNode(String className, byte[] classBytes) {
        classAddList.put(className, classBytes);
    }

    public void applyClassModify() {
        for (var className : classDeleteList) {
            jarCaches.removeEntry(className + ".class");
        }
        for (var entry : classAddList.entrySet()) {
            var e = new JarCachesEntry();
            e.content = entry.getValue();
            e.type = JarCachesEntryType.Class;
            e.path = entry.getKey() + ".class";
            jarCaches.addEntry(e);
        }
    }
}
