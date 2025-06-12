package com.github.luiox.morpher.asm.insn;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.INVOKEDYNAMIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * 专门处理描述符和类型的问题
 * 单独每次手动处理实在是太复杂了
 * 因为实在是太混乱了，不得不规定一下各种类型的名字规则
 * 根本原因是Java的反射接口的那些类型和ASM的不匹配，而且转换复杂，
 * 这里提供一套接口进行互相转换，使用的地方必须搞清楚自己输入的是什么形式的名字
 * <p>
 * 规定常用的几种名字形式
 * 内部形式的类名internalName是Lcom/example/MyClass;这样
 * 点分隔的类名dotName是com.example.MyClass这样
 * 斜杠分隔形式的类名splashName是com/example/MyClass这样
 * <p>
 * Java内Class的getName方法返回的是dotName
 * ClassNode的name是splashName
 * 但是方法描述符里面的类名是internalName
 */
public class TypeDescUtil {
    public static final Map<String, Type> DescToType = new HashMap<>();
    public static final Map<Type, String> TypeToDesc = new HashMap<>();

    static {
        DescToType.put("Z", Type.BOOLEAN_TYPE);
        DescToType.put("C", Type.CHAR_TYPE);
        DescToType.put("B", Type.BYTE_TYPE);
        DescToType.put("S", Type.SHORT_TYPE);
        DescToType.put("I", Type.INT_TYPE);
        DescToType.put("J", Type.LONG_TYPE);
        DescToType.put("F", Type.FLOAT_TYPE);
        DescToType.put("D", Type.DOUBLE_TYPE);
        DescToType.put("V", Type.VOID_TYPE);

        TypeToDesc.put(Type.BOOLEAN_TYPE, "Z");
        TypeToDesc.put(Type.CHAR_TYPE, "C");
        TypeToDesc.put(Type.BYTE_TYPE, "B");
        TypeToDesc.put(Type.SHORT_TYPE, "S");
        TypeToDesc.put(Type.INT_TYPE, "I");
        TypeToDesc.put(Type.LONG_TYPE, "J");
        TypeToDesc.put(Type.FLOAT_TYPE, "F");
        TypeToDesc.put(Type.DOUBLE_TYPE, "D");
        TypeToDesc.put(Type.VOID_TYPE, "V");

    }

    /**
     * 根据类型判断需要多少个slot
     */
    public static int getSlotCountForType(Type type) {
        return switch (type.getSort()) {
            case Type.BOOLEAN, Type.BYTE, Type.CHAR, Type.SHORT, Type.INT, Type.OBJECT, Type.ARRAY, Type.FLOAT -> 1;
            case Type.LONG, Type.DOUBLE -> 2;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    /**
     * 根据一个方法的调用指令，计算出这个调用会产生多少个stack的slot消耗
     */
    public static int getRequiredStackSlotsForMethodInvocation(AbstractInsnNode insnNode) {
        String desc;
        if (insnNode instanceof MethodInsnNode methodInsn) {
            desc = methodInsn.desc;
        } else if (insnNode instanceof InvokeDynamicInsnNode invokeDynamicInsn) {
            desc = invokeDynamicInsn.desc;
        } else {
            throw new IllegalStateException("Not a method instruction");
        }

        int count = Type.getArgumentCount(desc); // Arguments count = Stack values count
        if (insnNode.getOpcode() != INVOKESTATIC && insnNode.getOpcode() != INVOKEDYNAMIC) {
            count++; // "this" reference
        }

        return count;
    }

    public static String toClassName(String descriptor) {
        Type t = Type.getType(descriptor);
        if (t.getSort() == Type.ARRAY) {
            // why java, why?!
            return descriptor.replace('/', '.');
        }
        return t.getClassName();
    }

    public static boolean matchesParameters(Class<?>[] classes, String desc) {
        Type[] params = Type.getArgumentTypes(desc);
        if (classes.length != params.length)
            return false;
        for (int i = 0; i < classes.length; i++) {
            if (!classes[i].getName().equals(TypeDescUtil.toClassName(params[i].getDescriptor()))) {
                return false;
            }
        }
        return true;
    }

    // 从Class对象获取类名
    public static String getClassName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }


    /**
     * 将内部形式的类名转换为点分隔的类名。
     * 例如，将 "Lcom/example/MyClass;" 转换为 "com.example.MyClass"。
     *
     * @param internalName 内部形式的类名
     * @return 点分隔的类名
     */
    public static @NotNull String internalNameToDotName(@NotNull String internalName) {
        if (internalName.startsWith("L") && internalName.endsWith(";")) {
            return internalName.substring(1, internalName.length() - 1).replace('/', '.');
        }
        throw new IllegalArgumentException("Invalid internal name: " + internalName);
    }

    /**
     * 将内部形式的类名转换为斜杠分隔的类名。
     * 例如，将 "Lcom/example/MyClass;" 转换为 "com/example/MyClass"。
     *
     * @param internalName 内部形式的类名
     * @return 斜杠分隔的类名
     */
    public static @NotNull String internalNameToSplashName(@NotNull String internalName) {
        if (internalName.startsWith("L") && internalName.endsWith(";")) {
            return internalName.substring(1, internalName.length() - 1);
        }
        throw new IllegalArgumentException("Invalid internal name: " + internalName);
    }

    /**
     * 将点分隔的类名转换为斜杠分隔的类名。
     * 例如，将 "com.example.MyClass" 转换为 "com/example/MyClass"。
     *
     * @param dotName 点分隔的类名
     * @return 斜杠分隔的类名
     */
    public static @NotNull String dotNameToSplashName(@NotNull String dotName) {
        return dotName.replace('.', '/');
    }

    /**
     * 将点分隔的类名转换为内部形式的类名。
     * 例如，将 "com.example.MyClass" 转换为 "Lcom/example/MyClass;"。
     *
     * @param dotName 点分隔的类名
     * @return 内部形式的类名
     */
    public static @NotNull String dotNameToInternalName(@NotNull String dotName) {
        return "L" + dotName.replace('.', '/') + ";";
    }

    /**
     * 将斜杠分隔的类名转换为内部形式的类名。
     * 例如，将 "com/example/MyClass" 转换为 "Lcom/example/MyClass;"。
     *
     * @param splashName 斜杠分隔的类名
     * @return 内部形式的类名
     */
    public static @NotNull String splashNameToInternalName(@NotNull String splashName) {
        return "L" + splashName + ";";
    }

    /**
     * 将斜杠分隔的类名转换为点分隔的类名。
     * 例如，将 "com/example/MyClass" 转换为 "com.example.MyClass"。
     *
     * @param splashName 斜杠分隔的类名
     * @return 点分隔的类名
     */
    public static @NotNull String splashNameToDotName(@NotNull String splashName) {
        return splashName.replace('/', '.');
    }

    public static String simplify(String descriptor) {
        int squareIndex = descriptor.lastIndexOf("[");
        String prefix = descriptor.substring(0, squareIndex + 1);

        String simpleName = descriptor.substring(squareIndex + 1);
        if (simpleName.startsWith("L") && simpleName.endsWith(";")) {
            simpleName = simpleName.substring(1, simpleName.length() - 1);
        }

        int slashIndex = simpleName.lastIndexOf("/");
        simpleName = simpleName.substring(slashIndex + 1);

        return prefix + simpleName;
    }

    /**
     * 鉴定一个类名是不是符合jvm要求
     * @param className 类名
     * @return 是否合法
     */
    public static boolean isValidClassName(@NotNull String className) {
        // 类名里面不能包含";"、"/"、"\u0020"
        // "L"是可以的，"""引号也可以，括号也可以，"\\"反斜杠也可以，零宽度空格可以，普通空格不行，不换行空格"\u00a0"也可以用
        return !className.contains(";") && !className.contains("/");
    }

    /**
     * 判断一个方法名字是否符合jvm要求
     * @param methodName 方法名
     * @return 是否合法
     */
    public static boolean isValidMethodName(@NotNull String methodName) {
        // <init> 和 <clinit> 是合法的
        if(methodName.equals("<init>") || methodName.equals("<clinit>")){
            return true;
        }
        // 方法名字不能为空
        if(methodName.isEmpty()){
            return false;
        }
        // 也不能包含. ; [ /
        return !methodName.matches(".*[\\.\\;\\[\\/].*");
    }
}
