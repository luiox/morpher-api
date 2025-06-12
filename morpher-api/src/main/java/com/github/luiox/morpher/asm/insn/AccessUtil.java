package com.github.luiox.morpher.asm.insn;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Set;

/**
 * node的access字段操作工具类
 */
public class AccessUtil {
    private AccessUtil() {
    }

    private static final Set<Integer> ClassNodeAccesses = new HashSet<>();
    private static final Set<Integer> MethodNodeAccesses = new HashSet<>();
    private static final Set<Integer> FieldNodeAccesses = new HashSet<>();
    private static final int ClassNodeAllAccess;
    private static final int MethodNodeAllAccess;
    private static final int FieldNodeAllAccess;

    static {
        ClassNodeAccesses.add(Opcodes.ACC_PUBLIC);
        ClassNodeAccesses.add(Opcodes.ACC_PRIVATE);
        ClassNodeAccesses.add(Opcodes.ACC_PROTECTED);
        ClassNodeAccesses.add(Opcodes.ACC_FINAL);
        ClassNodeAccesses.add(Opcodes.ACC_SUPER);
        ClassNodeAccesses.add(Opcodes.ACC_INTERFACE);
        ClassNodeAccesses.add(Opcodes.ACC_ABSTRACT);
        ClassNodeAccesses.add(Opcodes.ACC_SYNTHETIC);
        ClassNodeAccesses.add(Opcodes.ACC_ANNOTATION);
        ClassNodeAccesses.add(Opcodes.ACC_ENUM);

        MethodNodeAccesses.add(Opcodes.ACC_PUBLIC);
        MethodNodeAccesses.add(Opcodes.ACC_PRIVATE);
        MethodNodeAccesses.add(Opcodes.ACC_PROTECTED);
        MethodNodeAccesses.add(Opcodes.ACC_STATIC);
        MethodNodeAccesses.add(Opcodes.ACC_FINAL);
        MethodNodeAccesses.add(Opcodes.ACC_SYNCHRONIZED);
        MethodNodeAccesses.add(Opcodes.ACC_BRIDGE);
        MethodNodeAccesses.add(Opcodes.ACC_VARARGS);
        MethodNodeAccesses.add(Opcodes.ACC_NATIVE);
        MethodNodeAccesses.add(Opcodes.ACC_ABSTRACT);
        MethodNodeAccesses.add(Opcodes.ACC_STRICT);
        MethodNodeAccesses.add(Opcodes.ACC_SYNTHETIC);
        MethodNodeAccesses.add(Opcodes.ACC_MANDATED);

        FieldNodeAccesses.add(Opcodes.ACC_PUBLIC);
        FieldNodeAccesses.add(Opcodes.ACC_PRIVATE);
        FieldNodeAccesses.add(Opcodes.ACC_PROTECTED);
        FieldNodeAccesses.add(Opcodes.ACC_STATIC);
        FieldNodeAccesses.add(Opcodes.ACC_FINAL);
        FieldNodeAccesses.add(Opcodes.ACC_VOLATILE);
        FieldNodeAccesses.add(Opcodes.ACC_TRANSIENT);
        FieldNodeAccesses.add(Opcodes.ACC_SYNTHETIC);
        FieldNodeAccesses.add(Opcodes.ACC_ENUM);
        FieldNodeAccesses.add(Opcodes.ACC_MANDATED);
        FieldNodeAccesses.add(Opcodes.ACC_DEPRECATED);

        ClassNodeAllAccess = ClassNodeAccesses.stream().reduce(0, (a, b) -> a | b);
        MethodNodeAllAccess = MethodNodeAccesses.stream().reduce(0, (a, b) -> a | b);
        FieldNodeAllAccess = FieldNodeAccesses.stream().reduce(0, (a, b) -> a | b);
    }

    /**
     * 移除访问符
     * @param access 访问符
     * @param remove 移除的访问符
     * @return 移除后的访问符
     */
    public static int removeAccess(int access, int... remove) {
        for (int r : remove) {
            access &= ~r;
        }
        return access;
    }

    public static int addAccess(int access, int... add) {
        for (int a : add) {
            access |= a;
        }
        return access;
    }

    public static boolean hasAccess(int access, int... has) {
        for (int a : has) {
            if ((access & a) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是合法的访问修饰符
     *
     * @param classNode 类节点
     * @return 合法返回true，不合法返回false
     */
    public static boolean isValid(@NotNull ClassNode classNode) {
        return (ClassNodeAllAccess & classNode.access) == classNode.access;
    }

    public static boolean isValid(@NotNull MethodNode methodNode) {
        return (MethodNodeAllAccess & methodNode.access) == methodNode.access;
    }

    public static boolean isValid(@NotNull FieldNode fieldNode) {
        return (FieldNodeAllAccess & fieldNode.access) == fieldNode.access;
    }

    /**
     * 移除非法的修饰符
     * @param classNode 类节点
     */
    public static void removeInvalid(@NotNull ClassNode classNode) {
        classNode.access = ClassNodeAllAccess & classNode.access;
    }

    public static void removeInvalid(@NotNull MethodNode methodNode) {
        methodNode.access = MethodNodeAllAccess & methodNode.access;
    }

    public static void removeInvalid(@NotNull FieldNode fieldNode) {
        fieldNode.access = FieldNodeAllAccess & fieldNode.access;
    }
}
