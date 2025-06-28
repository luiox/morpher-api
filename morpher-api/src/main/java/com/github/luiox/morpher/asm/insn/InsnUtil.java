package com.github.luiox.morpher.asm.insn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.*;


public class InsnUtil {
    private static final Logger logger = LoggerFactory.getLogger(InsnUtil.class);

    // 防止被创建对象
    private InsnUtil() {

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 从值到insn
    // 将常量值转换为表示此常量的指令

    /**
     * 获取加载这个数值的指令，因为int大一点小一点都是1个slot
     *
     * @param value 数值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getIntInsn(int value) {
        if (value >= -1 && value <= 5) {
            return switch (value) {
                case -1 -> new InsnNode(ICONST_M1);
                case 0 -> new InsnNode(ICONST_0);
                case 1 -> new InsnNode(ICONST_1);
                case 2 -> new InsnNode(ICONST_2);
                case 3 -> new InsnNode(ICONST_3);
                case 4 -> new InsnNode(ICONST_4);
                case 5 -> new InsnNode(ICONST_5);
                default -> null;
            };
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return new IntInsnNode(Opcodes.BIPUSH, value);
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return new IntInsnNode(Opcodes.SIPUSH, value);
        } else {
            // 是再存不下只能用ldc的Integer了，这个也是一个slot，无需担心slot不匹配的问题
            return new LdcInsnNode(value);
        }
    }

    /**
     * 获取加载这个数值的指令，注意long是2个slot
     *
     * @param value 数值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getLongInsn(long value) {
        // 因为是Long所以为了slot一样都是2，所以不用ICONST_0
        if (value == 0) {
            return new InsnNode(LCONST_0);
        } else if (value == 1) {
            return new InsnNode(LCONST_1);
        }
        return new LdcInsnNode(value);
    }

    /**
     * 获取加载这个数值的指令
     *
     * @param value 数值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getFloatInsn(float value) {
        if (value == 0f) {
            return new InsnNode(FCONST_0);
        }
        if (value == 1f) {
            return new InsnNode(FCONST_1);
        }
        if (value == 2f) {
            return new InsnNode(FCONST_2);
        }
        return new LdcInsnNode(value);
    }

    /**
     * 获取加载这个数值的指令
     *
     * @param value 数值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getDoubleInsn(double value) {
        if (value == 0d) {
            return new InsnNode(DCONST_0);
        }
        if (value == 1d) {
            return new InsnNode(DCONST_1);
        }
        return new LdcInsnNode(value);
    }

    /**
     * 获取加载这个数值的指令
     *
     * @param number 数值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getNumberInsn(@NotNull Number number) {
        if (number instanceof Integer || number instanceof Byte || number instanceof Short) {
            return getIntInsn(number.intValue());
        } else if (number instanceof Long) {
            return getLongInsn(number.longValue());
        } else if (number instanceof Float) {
            return getFloatInsn(number.floatValue());
        } else if (number instanceof Double) {
            return getDoubleInsn(number.doubleValue());
        }

        throw new IllegalArgumentException("Your number is not supported: " + number);
    }

    /**
     * 获取加载这个字符串的指令
     *
     * @param s 字符串
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getStringInsn(@NotNull String s) {
        return new LdcInsnNode(s);
    }

    /**
     * 获取加载布尔值的指令
     *
     * @param b 布尔值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getBooleanInsn(boolean b) {
        if (b) {
            return new InsnNode(ICONST_1);
        }
        return new InsnNode(ICONST_0);
    }

    /**
     * 获取加载Type的指令
     *
     * @param t type
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getTypeInsn(Type t) {
        return new LdcInsnNode(t);
    }

    /**
     * 获取加载这个常量值的指令
     *
     * @param value 常量值
     * @return 指令
     */

    public static @NotNull AbstractInsnNode getConstantInsn(@NotNull Object value) {
        if (value instanceof Number v) {
            return getNumberInsn(v);
        } else if (value instanceof String v) {
            return getStringInsn(v);
        } else if (value instanceof Type t) {
            return getTypeInsn(t);
        }
        throw new IllegalArgumentException("This is not a constant value");
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 从insn到值
    // 这些方法必须保证insn是产生一个常量值的，否则抛出异常

    /**
     * 从insn获取其int值，必须保证insn是产生一个常量int的，否则抛出异常
     *
     * @param insn 指令
     * @return 常量int值
     */

    public static int getIntValue(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case ICONST_M1 -> -1;
            case ICONST_0 -> 0;
            case ICONST_1 -> 1;
            case ICONST_2 -> 2;
            case ICONST_3 -> 3;
            case ICONST_4 -> 4;
            case ICONST_5 -> 5;
            case SIPUSH, BIPUSH -> ((IntInsnNode) insn).operand;
            case LDC -> (int) ((LdcInsnNode) insn).cst;
            default -> throw new IllegalArgumentException("Insn is not a push int instruction");
        };
    }

    /**
     * 从insn获取其long值，必须保证insn是产生一个常量long的，否则抛出异常
     *
     * @param insn 指令
     * @return 常量long值
     */

    public static long getLongValue(@NotNull AbstractInsnNode insn) {
        if (insn.getOpcode() == LCONST_0) {
            return 0;
        } else if (insn.getOpcode() == LCONST_1) {
            return 1;
        }
        if (insn instanceof LdcInsnNode ldcInsnNode) {
            if (ldcInsnNode.cst instanceof Long) {
                return (long) ldcInsnNode.cst;
            }
        }
        throw new IllegalArgumentException("Insn is not a push long instruction");
    }

    /**
     * 从insn获取其float值，必须保证insn是产生一个常量float的，否则抛出异常
     *
     * @param insn 指令
     * @return 常量float值
     */

    public static float getFloatValue(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case FCONST_0 -> 0f;
            case FCONST_1 -> 1f;
            case FCONST_2 -> 2f;
            case LDC -> (float) ((LdcInsnNode) insn).cst;
            default -> throw new IllegalStateException("Unexpected value: " + insn.getOpcode());
        };
    }

    /**
     * 从insn获取其double值，必须保证insn是产生一个常量double的，否则抛出异常
     *
     * @param insn 指令
     * @return 常量double值
     */

    public static double getDoubleValue(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case DCONST_0 -> 0d;
            case DCONST_1 -> 1d;
            case LDC -> (double) ((LdcInsnNode) insn).cst;
            default -> throw new IllegalStateException("Unexpected value: " + insn.getOpcode());
        };
    }


    public static @NotNull Number getNumberValue(@NotNull AbstractInsnNode insn) {
        if (isInteger(insn)) {
            return getIntValue(insn);
        } else if (isLong(insn)) {
            return getLongValue(insn);
        } else if (isDouble(insn)) {
            return getDoubleValue(insn);
        } else if (isFloat(insn)) {
            return getFloatValue(insn);
        }

        throw new IllegalArgumentException("This is not a number value");
    }


    public static @NotNull Type getTypeValue(@NotNull AbstractInsnNode insn) {
        return (Type) ((LdcInsnNode) insn).cst;
    }


    public static @NotNull String getStringValue(@NotNull AbstractInsnNode insn) {
        return (String) ((LdcInsnNode) insn).cst;
    }


    public static @Nullable Object getConstantValue(@NotNull AbstractInsnNode insn) {
        if (isNumber(insn)) {
            return getNumberValue(insn);
        } else if (isString(insn)) {
            return getStringValue(insn);
        } else if (isType(insn)) {
            return getTypeValue(insn);
        } else if (isNull(insn)) {
            return null;
        }
        throw new IllegalArgumentException("This is not a constant value");
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 判断一个insn是不是满足特定要求的

    /**
     * 判断一个insn是不是一个加载常量int
     *
     * @param insn 指令
     * @return 是否是常量int
     */

    public static boolean isInteger(@NotNull AbstractInsnNode insn) {
        if ((insn.getOpcode() >= ICONST_M1
                && insn.getOpcode() <= ICONST_5)
                || insn.getOpcode() == SIPUSH
                || insn.getOpcode() == BIPUSH)
            return true;
        if (insn instanceof LdcInsnNode ldcInsnNode) {
            return ldcInsnNode.cst instanceof Integer;
        }
        return false;
    }

    /**
     * 判断一个insn是不是一个加载常量long
     *
     * @param insn 指令
     * @return 是否是常量long
     */

    public static boolean isLong(@NotNull AbstractInsnNode insn) {
        if (insn.getOpcode() == LCONST_0
                || insn.getOpcode() == LCONST_1)
            return true;
        if (insn instanceof LdcInsnNode ldcInsnNode) {
            return ldcInsnNode.cst instanceof Long;
        }
        return false;
    }

    /**
     * 判断一个insn是不是一个加载常量String
     *
     * @param insn 指令
     * @return 是否是常量String
     */

    public static boolean isString(@NotNull AbstractInsnNode insn) {
        return insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof String;
    }

    /**
     * 判断一个insn是不是一个加载常量Type
     *
     * @param insn 指令
     * @return 是否是常量Type
     */

    public static boolean isType(@NotNull AbstractInsnNode insn) {
        return insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Type;
    }

    /**
     * 判断一个insn是不是一个加载常量float
     *
     * @param insn 指令
     * @return 是否是常量float
     */

    public static boolean isFloat(@NotNull AbstractInsnNode insn) {
        int opcode = insn.getOpcode();
        return (opcode >= FCONST_0 && opcode <= FCONST_2)
                || (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Float);
    }

    /**
     * 判断一个insn是不是一个加载常量double
     *
     * @param insn 指令
     * @return 是否是常量double
     */

    public static boolean isDouble(@NotNull AbstractInsnNode insn) {
        int opcode = insn.getOpcode();
        return (opcode >= DCONST_0 && opcode <= DCONST_1)
                || (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Double);
    }

    /**
     * 判断一个insn是不是一个加载常量数值
     *
     * @param insn 指令
     * @return 是否是常量数值
     */

    public static boolean isNumber(@NotNull AbstractInsnNode insn) {
        return (isInteger(insn) || isLong(insn) || isFloat(insn) || isDouble(insn));
    }

    public static boolean isConstant(@NotNull AbstractInsnNode insn) {
        return isNumber(insn) || isType(insn) || isString(insn) || isNull(insn);
    }

    public static boolean isDefective(@NotNull AbstractInsnNode insn) {
        return insn instanceof LabelNode || insn instanceof LineNumberNode
                || insn instanceof FrameNode || insn.getOpcode() == NOP;
    }

    /**
     * 判断一个insn是不是return指令
     *
     * @param insn 指令
     * @return 是否是return指令
     */

    public static boolean isReturn(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case ARETURN, IRETURN, FRETURN, DRETURN, LRETURN, RETURN -> true;
            default -> false;
        };
    }

    public static @NotNull AbstractInsnNode getLoadVarInsn(@NotNull Type type, int slot) {
        return switch (type.getDescriptor()) {
            case "Z", "C", "B", "S", "I" -> new VarInsnNode(ILOAD, slot);
            case "J" -> new VarInsnNode(LLOAD, slot);
            case "F" -> new VarInsnNode(FLOAD, slot);
            case "D" -> new VarInsnNode(DLOAD, slot);
            case "L" -> new VarInsnNode(ALOAD, slot);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }

    public static boolean isNull(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() == ACONST_NULL;
    }

    public static boolean isIf(@NotNull AbstractInsnNode insn) {
        int op = insn.getOpcode();
        return (op >= IFEQ && op <= IF_ACMPNE) || op == IFNULL || op == IFNONNULL;
    }

    public static boolean isThrow(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() == ATHROW;
    }

    public static boolean isTerminating(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case RETURN, ARETURN, IRETURN, FRETURN, DRETURN, LRETURN,
                 ATHROW, TABLESWITCH, LOOKUPSWITCH, GOTO -> true;
            default -> false;
        };
    }

    public static boolean isInstruction(@NotNull AbstractInsnNode node) {
        return !(node instanceof LineNumberNode) && !(node instanceof FrameNode) && !(node instanceof LabelNode);
    }

    public static boolean isSingleVarStore(@NotNull AbstractInsnNode abstractInsnNode) {
        return abstractInsnNode.getOpcode() == ISTORE
                || abstractInsnNode.getOpcode() == LSTORE
                || abstractInsnNode.getOpcode() == FSTORE
                || abstractInsnNode.getOpcode() == DSTORE
                || abstractInsnNode.getOpcode() == ASTORE;
    }

    public static boolean isSingleVarLoad(@NotNull AbstractInsnNode abstractInsnNode) {
        return abstractInsnNode.getOpcode() == ILOAD
                || abstractInsnNode.getOpcode() == LLOAD
                || abstractInsnNode.getOpcode() == FLOAD
                || abstractInsnNode.getOpcode() == DLOAD
                || abstractInsnNode.getOpcode() == ALOAD;
    }

    public static boolean isStackOperateInsn(@NotNull AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case DUP, DUP2, DUP_X1, DUP_X2, DUP2_X1, DUP2_X2, SWAP, POP, POP2 -> true;
            default -> false;
        };
    }

    public static boolean isArithmeticInsn(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() >= IADD && insn.getOpcode() <= LXOR;
    }

    public static boolean is2ConditionJump(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() >= IF_ICMPEQ && insn.getOpcode() <= IF_ICMPLE;
    }

    public static boolean is1ConditionJump(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() >= IFEQ && insn.getOpcode() <= IFLE;
    }

    public static boolean isControFlowInsn(@NotNull AbstractInsnNode abstractInsnNode) {
        return is1ConditionJump(abstractInsnNode) || is2ConditionJump(abstractInsnNode) || abstractInsnNode.getOpcode() == GOTO;
    }

    public static boolean isTypeCast(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() >= I2L && insn.getOpcode() <= D2F;
    }

    public static int reverseJumpOpcde(int opcode) {
        return switch (opcode) {
            case IFNE -> IFEQ;
            case IFEQ -> IFNE;
            case IFGE -> IFLT;
            case IFGT -> IFLE;
            case IFLE -> IFGT;
            case IFLT -> IFGE;
            case IFNONNULL -> IFNULL;
            case IFNULL -> IFNONNULL;
            case IF_ACMPEQ -> IF_ACMPNE;
            case IF_ACMPNE -> IF_ACMPEQ;
            case IF_ICMPEQ -> IF_ICMPNE;
            case IF_ICMPNE -> IF_ICMPEQ;
            case IF_ICMPGE -> IF_ICMPLT;
            case IF_ICMPGT -> IF_ICMPLE;
            case IF_ICMPLE -> IF_ICMPGT;
            case IF_ICMPLT -> IF_ICMPGE;
            default -> throw new IllegalStateException(String.format("Unable to reverse jump opcode: %d", opcode));
        };
    }

    public static @NotNull AbstractInsnNode getNext(@NotNull AbstractInsnNode node, int amount) {
        for (int i = 0; i < amount; i++) {
            node = getNext(node);
        }
        return node;
    }

    public static @NotNull AbstractInsnNode getNext(@NotNull AbstractInsnNode node) {
        AbstractInsnNode next = node.getNext();
        while (!isInstruction(next)) {
            next = next.getNext();
        }
        return next;
    }

    public static @NotNull AbstractInsnNode getPrevious(@NotNull AbstractInsnNode node, int amount) {
        for (int i = 0; i < amount; i++) {
            node = getPrevious(node);
        }
        return node;
    }

    public static @NotNull AbstractInsnNode getPrevious(@NotNull AbstractInsnNode node) {
        AbstractInsnNode prev = node.getPrevious();
        while (!isInstruction(prev)) {
            prev = prev.getPrevious();
        }
        return prev;
    }

    public static @NotNull InsnList arrayToInsnList(@NotNull AbstractInsnNode[] arr, int p) {
        InsnList list = new InsnList();
        for (int i = 0; i <= p; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    public static void repeat(InsnBuilder builder, Consumer<InsnBuilder> consumer, int count) {
        for (int i = 0; i < count; i++) {
            consumer.accept(builder);
        }
    }

    public static boolean isInvokeMethod(@NotNull AbstractInsnNode insn, boolean includeInvokeDynamic) {
        return insn.getOpcode() >= INVOKEVIRTUAL && (includeInvokeDynamic ? insn.getOpcode() <= INVOKEDYNAMIC : insn.getOpcode() < INVOKEDYNAMIC);
    }

    public static boolean isFieldInsn(@NotNull AbstractInsnNode insn) {
        return insn.getOpcode() >= GETSTATIC && insn.getOpcode() <= PUTFIELD;
    }

    /**
     * 拷贝InsnList
     *
     * @param insnList 要拷贝的指令列表
     * @return 拷贝后的指令列表
     */

    public static @NotNull List<AbstractInsnNode> copyInsnList(@NotNull InsnList insnList) {
        /*
            因为ow2 asm的InsnList设计为不可变，所以无法直接深拷贝
            深层原因是Label为了是同一个，就需要多个地方用同一个LabelNode
            所以这里需要用map来记录LabelNode，这样子拷贝的时候，相同的Label地方也是一样的引用
         */
        List<AbstractInsnNode> copy = new ArrayList<>();
        Map<LabelNode, LabelNode> labelMap = new HashMap<>();

        for (var insn : insnList) {
            AbstractInsnNode insnCopy = copyInsnNodeFull(insn, labelMap);
            copy.add(insnCopy);
        }

        return copy;
    }

    public static @NotNull InsnList covertListToInsnList(@NotNull List<AbstractInsnNode> insnList) {
        InsnList newList = new InsnList();
        for (var insn : insnList) {
            newList.add(insn);
        }
        return newList;
    }

    public static @NotNull InsnList covertArrayToList(@NotNull AbstractInsnNode[] insns) {
        InsnList insnList = new InsnList();
        Arrays.stream(insns).forEach(insnList::add);
        return insnList;
    }

    /**
     * 拷贝InsnList
     *
     * @param insnList 要拷贝的指令列表
     * @return 拷贝后的指令列表
     */
    public static @NotNull InsnList copyRawInsnList(@NotNull InsnList insnList) {
        /*
            因为ow2 asm的InsnList设计为不可变，所以无法直接深拷贝
            深层原因是Label为了是同一个，就需要多个地方用同一个LabelNode
            所以这里需要用map来记录LabelNode，这样子拷贝的时候，相同的Label地方也是一样的引用
         */
        InsnList copy = new InsnList();
        Map<LabelNode, LabelNode> labelMap = new HashMap<>();

        for (var insn : insnList) {
            AbstractInsnNode insnCopy = copyInsnNodeFull(insn, labelMap);
            copy.add(insnCopy);
        }

        return copy;
    }

    /**
     * 为了适配InsnBuilder里面的存储方式，提供另外一个接口拷贝
     *
     * @param insnList 要拷贝的指令列表
     * @return 拷贝后的指令列表
     */
    public static @NotNull InsnList copyInsnList(@NotNull List<AbstractInsnNode> insnList) {
        InsnList copy = new InsnList();
        Map<LabelNode, LabelNode> labelMap = new HashMap<>();

        for (var insn : insnList) {
            AbstractInsnNode insnCopy = copyInsnNodeFull(insn, labelMap);
            copy.add(insnCopy);
        }

        return copy;
    }

    /**
     * 拷贝一个指令节点，不能拷贝那些需要标签的指令，如果用这个拷贝那些需要标签的指令会导致两个指令共享一个LabelNode
     * 如果产生这个共享，不会做什么，如果在同一个InsnList里面，这允许这么做，但是如果要拷贝一整个InsnList，要使用copyInsnNodeFull
     *
     * @param insn 要拷贝的指令
     * @return 拷贝的指令
     */
    public static @NotNull AbstractInsnNode copyInsnNode(@NotNull AbstractInsnNode insn) {
        switch (insn) {
            case InsnNode insnNode -> {
                return new InsnNode(insn.getOpcode());
            }
            case IntInsnNode iin -> {
                return new IntInsnNode(iin.getOpcode(), iin.operand);
            }
            case VarInsnNode vin -> {
                return new VarInsnNode(vin.getOpcode(), vin.var);
            }
            case TypeInsnNode tin -> {
                return new TypeInsnNode(tin.getOpcode(), tin.desc);
            }
            case FieldInsnNode fin -> {
                return new FieldInsnNode(fin.getOpcode(), fin.owner, fin.name, fin.desc);
            }
            case MethodInsnNode min -> {
                return new MethodInsnNode(min.getOpcode(), min.owner, min.name, min.desc, min.itf);
            }
            case JumpInsnNode jin -> {
                return new JumpInsnNode(jin.getOpcode(), jin.label);
            }
            case LabelNode ln -> {
                return ln;
            }
            case LdcInsnNode ldc -> {
                return new LdcInsnNode(ldc.cst);
            }
            case IincInsnNode iinc -> {
                return new IincInsnNode(iinc.var, iinc.incr);
            }
            case TableSwitchInsnNode tsin -> {
                LabelNode[] labels = new LabelNode[tsin.labels.size()];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = tsin.labels.get(i);
                }
                LabelNode dflt = tsin.dflt;
                return new TableSwitchInsnNode(tsin.min, tsin.max, dflt, labels);
            }
            case LookupSwitchInsnNode lsin -> {
                LabelNode dflt = lsin.dflt;
                LabelNode[] labels = new LabelNode[lsin.labels.size()];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = lsin.labels.get(i);
                }
                int[] keys = new int[lsin.keys.size()];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = lsin.keys.get(i);
                }
                return new LookupSwitchInsnNode(dflt, keys, labels);
            }
            case MultiANewArrayInsnNode manain -> {
                return new MultiANewArrayInsnNode(manain.desc, manain.dims);
            }
            case LineNumberNode lineNumberNode -> {
                return new LabelNode();
            }
            case FrameNode frameNode -> {
                if (frameNode.local == null) {
                    return new FrameNode(frameNode.type, 0, new Object[1], 0, new Object[1]);
                }
                Object[] local = new Object[frameNode.local.size()];
                for (int i = 0; i < local.length; i++) {
                    local[i] = frameNode.local.get(i);
                }
                int stackNum = frameNode.stack == null ? 0 : frameNode.stack.size();
                if (stackNum == 0) {
                    return new FrameNode(frameNode.type, frameNode.local.size(), local, stackNum, null);
                }
                Object[] stack = new Object[stackNum];
                for (int i = 0; i < stack.length; i++) {
                    stack[i] = frameNode.stack.get(i);
                }
                return new FrameNode(frameNode.type, frameNode.local.size(), local, stackNum, stack);
            }
            case InvokeDynamicInsnNode invokeDynamicInsnNode -> {
                // 到时候再修这个copy
                var bsm = invokeDynamicInsnNode.bsm;
                var bsmArgs = invokeDynamicInsnNode.bsmArgs.clone();
                return new InvokeDynamicInsnNode(invokeDynamicInsnNode.name,
                        invokeDynamicInsnNode.desc,
                        new Handle(bsm.getTag(), bsm.getOwner(), bsm.getName(), bsm.getDesc(), bsm.isInterface()), bsmArgs);
            }
            default -> {
                logger.info("no copy, may be it is a error, insn opcode: {}", insn.getOpcode());
                return insn;
            }
        }
    }

    public static @NotNull AbstractInsnNode copyInsnNodeFull(@NotNull AbstractInsnNode insn,
                                                             @NotNull Map<LabelNode, LabelNode> labelMap) {
        switch (insn) {
            case InsnNode insnNode -> {
                return new InsnNode(insn.getOpcode());
            }
            case IntInsnNode iin -> {
                return new IntInsnNode(iin.getOpcode(), iin.operand);
            }
            case VarInsnNode vin -> {
                return new VarInsnNode(vin.getOpcode(), vin.var);
            }
            case TypeInsnNode tin -> {
                return new TypeInsnNode(tin.getOpcode(), tin.desc);
            }
            case FieldInsnNode fin -> {
                return new FieldInsnNode(fin.getOpcode(), fin.owner, fin.name, fin.desc);
            }
            case MethodInsnNode min -> {
                return new MethodInsnNode(min.getOpcode(), min.owner, min.name, min.desc, min.itf);
            }
            case JumpInsnNode jin -> {
                LabelNode newLabel = labelMap.computeIfAbsent(jin.label, k -> new LabelNode());
                return new JumpInsnNode(jin.getOpcode(), newLabel);
            }
            case LabelNode ln -> {
                return labelMap.computeIfAbsent(ln, k -> new LabelNode());
            }
            case LdcInsnNode ldc -> {
                return new LdcInsnNode(ldc.cst);
            }
            case IincInsnNode iinc -> {
                return new IincInsnNode(iinc.var, iinc.incr);
            }
            case TableSwitchInsnNode tsin -> {
                LabelNode[] labels = new LabelNode[tsin.labels.size()];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = labelMap.computeIfAbsent(tsin.labels.get(i), k -> new LabelNode());
                }
                LabelNode dflt = labelMap.computeIfAbsent(tsin.dflt, k -> new LabelNode());
                return new TableSwitchInsnNode(tsin.min, tsin.max, dflt, labels);
            }
            case LookupSwitchInsnNode lsin -> {
                LabelNode dflt = labelMap.computeIfAbsent(lsin.dflt, k -> new LabelNode());
                LabelNode[] labels = new LabelNode[lsin.labels.size()];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = labelMap.computeIfAbsent(lsin.labels.get(i), k -> new LabelNode());
                }
                int[] keys = new int[lsin.keys.size()];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = lsin.keys.get(i);
                }
                return new LookupSwitchInsnNode(dflt, keys, labels);
            }
            case MultiANewArrayInsnNode manain -> {
                return new MultiANewArrayInsnNode(manain.desc, manain.dims);
            }
            case LineNumberNode lineNumberNode -> {
                return new LabelNode();
            }
            case FrameNode frameNode -> {
                if (frameNode.local == null) {
                    return new FrameNode(frameNode.type, 0, new Object[1], 0, new Object[1]);
                }
                Object[] local = new Object[frameNode.local.size()];
                for (int i = 0; i < local.length; i++) {
                    local[i] = frameNode.local.get(i);
                }
                int stackNum = frameNode.stack == null ? 0 : frameNode.stack.size();
                if (stackNum == 0) {
                    return new FrameNode(frameNode.type, frameNode.local.size(), local, stackNum, null);
                }
                Object[] stack = new Object[stackNum];
                for (int i = 0; i < stack.length; i++) {
                    stack[i] = frameNode.stack.get(i);
                }
                return new FrameNode(frameNode.type, frameNode.local.size(), local, stackNum, stack);
            }
            case InvokeDynamicInsnNode invokeDynamicInsnNode -> {
                // 到时候再修这个copy
                var bsm = invokeDynamicInsnNode.bsm;
                var bsmArgs = invokeDynamicInsnNode.bsmArgs.clone();
                return new InvokeDynamicInsnNode(invokeDynamicInsnNode.name,
                        invokeDynamicInsnNode.desc,
                        new Handle(bsm.getTag(), bsm.getOwner(), bsm.getName(), bsm.getDesc(), bsm.isInterface()), bsmArgs);
            }
            default -> {
                throw new IllegalArgumentException("Unknown instruction type: " + insn.getClass().getSimpleName());
            }
        }
    }


    public static @NotNull InsnList insnToInsnList(@NotNull AbstractInsnNode ain) {
        InsnList list = new InsnList();
        list.add(ain);
        return list;
    }

    public static @NotNull InsnList arrayToList(AbstractInsnNode @NotNull [] insns) {
        final InsnList insnList = new InsnList();
        Arrays.stream(insns).forEach(insnList::add);
        return insnList;
    }


    public static boolean methodInsnEquals(@NotNull MethodInsnNode methodInsnNode, int opcode,
                                           String owner, String name, String desc, boolean isInterface) {
        return methodInsnNode.getOpcode() == opcode
                && methodInsnNode.owner.equals(owner)
                && methodInsnNode.name.equals(name)
                && methodInsnNode.desc.equals(desc)
                && methodInsnNode.itf == isInterface;
    }


    public static boolean methodInsnEquals(@NotNull MethodInsnNode methodInsnNode, String owner, String name, String desc) {
        return methodInsnNode.owner.equals(owner)
                && methodInsnNode.name.equals(name)
                && methodInsnNode.desc.equals(desc);
    }

    /**
     * 根据字节码指令，判断这个指令需要多少个stack的slot
     */
    public static int getRequiredStackValuesCount(@NotNull AbstractInsnNode insnNode) {
        if (insnNode instanceof LdcInsnNode ldcInsnNode) {
            if (ldcInsnNode.cst instanceof Long) {
                return 2;
            } else if (ldcInsnNode.cst instanceof Double) {
                return 2;
            } else {
                return 1;
            }
        }

        return switch (insnNode.getOpcode()) {
            // Unary operations (one value)
            case ISTORE, LSTORE, FSTORE, DSTORE, ASTORE, POP, POP2, DUP, INEG,
                 LNEG, FNEG, DNEG, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S, IFEQ,
                 IFNE, IFLT, IFGE, IFGT, IFLE,
                 TABLESWITCH, LOOKUPSWITCH, IRETURN, LRETURN, FRETURN, DRETURN, ARETURN,
                 PUTSTATIC, GETFIELD,
                 NEWARRAY, ANEWARRAY, ARRAYLENGTH, ATHROW, CHECKCAST, INSTANCEOF, MONITORENTER, MONITOREXIT,
                 IFNULL, IFNONNULL -> 1;
            // Binary operations (two values)
            case IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IADD, LADD, FADD, DADD, ISUB, LSUB,
                 FSUB, DUP2, DUP2_X1, DUP2_X2, SWAP, DUP_X1,
                 DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, ISHL, LSHL, ISHR, LSHR,
                 IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, LCMP, FCMPL, FCMPG, DCMPL, DCMPG,
                 IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT,
                 IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, PUTFIELD -> 2;
            // Ternary operations (three values)
            case IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, DUP_X2 -> 3;

            // Method invocation
            case INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, INVOKEDYNAMIC ->
                    TypeDescUtil.getRequiredStackSlotsForMethodInvocation(insnNode);
            // Multi-dimensional array creation
            case MULTIANEWARRAY -> ((MultiANewArrayInsnNode) insnNode).dims;

            // No values required
            default -> 0;
        };
    }

    /**
     * 看这个指令消耗多少个slot，就是消耗，不算增加的
     *
     * @param insnNode 指令
     * @return 消耗的slot数量
     */
    public static int getConsumeStackValuesCount(@NotNull AbstractInsnNode insnNode) {
        return switch (insnNode.getOpcode()) {
            // Unary operations (one value)
            case ISTORE, LSTORE, FSTORE, DSTORE, ASTORE, POP, POP2, DUP, INEG,
                 LNEG, FNEG, DNEG, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S, IFEQ,
                 IFNE, IFLT, IFGE, IFGT, IFLE,
                 TABLESWITCH, LOOKUPSWITCH, IRETURN, LRETURN, FRETURN, DRETURN, ARETURN,
                 PUTSTATIC, GETFIELD,
                 NEWARRAY, ANEWARRAY, ARRAYLENGTH, ATHROW, CHECKCAST, INSTANCEOF, MONITORENTER, MONITOREXIT,
                 IFNULL, IFNONNULL -> 1;
            // Binary operations (two values)
            case IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IADD, LADD, FADD, DADD, ISUB, LSUB,
                 FSUB, DUP2, DUP2_X1, DUP2_X2, SWAP, DUP_X1,
                 DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, ISHL, LSHL, ISHR, LSHR,
                 IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, LCMP, FCMPL, FCMPG, DCMPL, DCMPG,
                 IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT,
                 IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, PUTFIELD -> 2;
            // Ternary operations (three values)
            case IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, DUP_X2 -> 3;

            // Method invocation
            case INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, INVOKEDYNAMIC ->
                    TypeDescUtil.getRequiredStackSlotsForMethodInvocation(insnNode);
            // Multi-dimensional array creation
            case MULTIANEWARRAY -> ((MultiANewArrayInsnNode) insnNode).dims;

            // No values required
            default -> 0;
        };
    }

    /**
     * Calculates the number of stack values required for a method invocation by descriptor.
     */
    private static int getRequiredStackValuesCountForMethodInvocation(@NotNull AbstractInsnNode insnNode) {
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

    /**
     * Returns the number of stack values consumed by this instruction
     */
    public static int getConsumedStackValuesCount(@NotNull AbstractInsnNode insnNode, Frame<? extends Value> frame) {
        return switch (insnNode.getOpcode()) {
            // Unary operations (one value)
            case ISTORE, LSTORE, FSTORE, DSTORE, ASTORE, POP, DUP, INEG,
                 LNEG, FNEG, DNEG, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S, IFEQ,
                 IFNE,
                 IFLT, IFGE, IFGT, IFLE, TABLESWITCH, LOOKUPSWITCH, IRETURN, LRETURN, FRETURN, DRETURN, ARETURN,
                 PUTSTATIC,
                 GETFIELD, NEWARRAY, ANEWARRAY, ARRAYLENGTH, ATHROW, CHECKCAST, INSTANCEOF, MONITORENTER, MONITOREXIT,
                 IFNULL,
                 IFNONNULL -> 1;
            // Binary operations (two values)
            case IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IADD, LADD, FADD, DADD, ISUB, LSUB,
                 FSUB,
                 DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, ISHL, LSHL, ISHR, LSHR,
                 IUSHR,
                 LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IF_ICMPEQ, IF_ICMPNE,
                 IF_ICMPLT,
                 IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, PUTFIELD, SWAP, DUP_X1 -> 2;
            // Ternary operations (three values)
            case IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE -> 3;

            // Method invocation
            case INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, INVOKEDYNAMIC ->
                    getRequiredStackValuesCountForMethodInvocation(insnNode);
            // Multi-dimensional array creation
            case MULTIANEWARRAY -> ((MultiANewArrayInsnNode) insnNode).dims;

            // Dynamic forms (uses frame)
            case POP2, DUP2 -> {
                Value sourceValue = frame.getStack(frame.getStackSize() - 1);
                yield sourceValue.getSize() == 2 ? 1 : 2;
            }
            case DUP_X2 -> {
                Value sourceValue2 = frame.getStack(frame.getStackSize() - 2);
                yield sourceValue2.getSize() == 2 ? 2 : 3;
            }
            case DUP2_X1 -> {
                Value sourceValue1 = frame.getStack(frame.getStackSize() - 1);
                yield sourceValue1.getSize() == 2 ? 2 : 3;
            }
            case DUP2_X2 -> {
                Value sourceValue1 = frame.getStack(frame.getStackSize() - 1);
                Value sourceValue2 = frame.getStack(frame.getStackSize() - 2);
                if (sourceValue1.getSize() == 2 && sourceValue2.getSize() == 2) {
                    yield 2;
                }
                Value sourceValue3 = frame.getStack(frame.getStackSize() - 2);
                if (sourceValue1.getSize() == 2 || sourceValue3.getSize() == 2) {
                    yield 3;
                }
                yield 4;
            }

            // No values required
            default -> 0;
        };
    }

    public static @NotNull AbstractInsnNode toPop(@NotNull Value value) {
        return value.getSize() == 1 ? new InsnNode(POP) : new InsnNode(POP2);
    }

    // 从一个槽的跳转指令到谓词
    public static final Map<Integer, Predicate<Integer>> OneSlotIntConditionJumpToPredicate = Map.of(
            // IFEQ：如果整数等于0，则跳转。
            IFEQ, (v) -> v == 0,
            // IFNE：如果整数不等于0，则跳转。
            IFNE, value -> value != 0,
            // IFLT：如果整数小于0，则跳转。
            IFLT, value -> value < 0,
            // IFGE：如果整数大于或等于0，则跳转。
            IFGE, value -> value >= 0,
            // IFGT：如果整数大于0，则跳转。
            IFGT, value -> value > 0,
            // IFLE：如果整数小于或等于0，则跳转。
            IFLE, value -> value <= 0
    );
    public static final Map<Integer, Predicate<Object>> OneSlotObjectConditionJumpToPredicate = Map.of(
            // 非整数
            IFNULL, Objects::isNull,
            IFNONNULL, Objects::nonNull
    );

    // 从两个槽的跳转指令到谓词
    public static final Map<Integer, BiPredicate<Integer, Integer>> TwoSlotIntConditionJumpToPredicate = Map.of(
            // IF_ICMPEQ：如果两个整数相等，则跳转。
            IF_ICMPEQ, (first, second) -> first.intValue() == second.intValue(),
            // IF_ICMPNE：如果两个整数不相等，则跳转。
            IF_ICMPNE, (first, second) -> first.intValue() != second.intValue(),
            // IF_ICMPLT：如果第一个整数小于第二个整数，则跳转。
            IF_ICMPLT, (first, second) -> first < second,
            // IF_ICMPGE：如果第一个整数大于等于第二个整数，则跳转。
            IF_ICMPGE, (first, second) -> first >= second,
            // IF_ICMPGT：如果第一个整数大于第二个整数，则跳转。
            IF_ICMPGT, (first, second) -> first > second,
            // IF_ICMPLE：如果第一个整数小于等于第二个整数，则跳转。
            IF_ICMPLE, (first, second) -> first <= second
    );

    public static final Map<Integer, BiPredicate<Object, Object>> TwoSlotObjectConditionJumpToPredicate = Map.of(
            // IF_ACMPEQ：如果两个引用相等，则跳转。
            IF_ACMPEQ, (first, second) -> first.equals(second),
            // IF_ACMPNE：如果两个引用不相等，则跳转。
            IF_ACMPNE, (first, second) -> !first.equals(second)
    );
}
