package com.github.luiox.morpher.asm.insn;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 原始的 InsnBuilder，用于生成 InsnList，比较基础
 * 实现从方法到字节码指令一一映射
 */

public class InsnBuilder {
    private static final Logger logger = LoggerFactory.getLogger(InsnBuilder.class);
    // 内部换List<AbstractInsnNode>存储的原因是，InsnList不方便直接操作
    // 为了重用AbstractInsnNode的时候，不会因为原先的InsnList导致其prev和next错乱
    // 所有的都需要从原先的InsnList中移除，创建新的InsnList的时候，重新一个一个加进去
    private final List<AbstractInsnNode> addList = new ArrayList<>();

    public List<AbstractInsnNode> getAddList() {
        return addList;
    }

    public void addInsnNode(AbstractInsnNode... insnNodes) {
        Collections.addAll(addList, insnNodes);
    }

    public void addInsnNode(List<AbstractInsnNode> insnNodes) {
        addList.addAll(insnNodes);
    }

    public void addInsnList(InsnList @NotNull ... insnLists) {
        for (InsnList insnList : insnLists) {
            for (AbstractInsnNode insnNode : insnList) {
                addList.add(insnNode);
            }
        }
    }

    public void addInsnList(@NotNull InsnList list) {
        for (var ain : list.toArray()) {
            addInsnNode(ain);
        }
    }

    public void addInsnList(@NotNull List<AbstractInsnNode> list) {
        for (var ain : list) {
            addInsnNode(ain);
        }
    }

    public void addInsnNode(AbstractInsnNode node) {
        addList.add(node);
    }

    public void deleteLastInsnNode() {
        if (!addList.isEmpty()) {
            addList.remove(addList.getLast());
        }
    }

    private static @NotNull InsnList toInsnList(@NotNull InsnBuilder builder, @NotNull Predicate<AbstractInsnNode> skipPredicate) {
        InsnList insnList = new InsnList();
        for (var ain : builder.getAddList()) {
            if (skipPredicate.test(ain)) {
                continue;
            }
            insnList.add(ain);
        }
        return insnList;
    }

    private static @NotNull InsnList toInsnList(@NotNull InsnBuilder builder) {
        InsnList insnList = new InsnList();
        for (var ain : builder.getAddList()) {
            insnList.add(ain);
        }
        return insnList;
    }


    public static @NotNull InsnList build(@NotNull Consumer<InsnBuilder> builderAction) {
        InsnBuilder builder = new InsnBuilder();
        builderAction.accept(builder);
        return toInsnList(builder);
    }


    public @NotNull InsnList getInsnList() {
        return toInsnList(this);
    }


    public @NotNull InsnList getInsnList(@NotNull Predicate<AbstractInsnNode> skipPredicate) {
        return toInsnList(this, skipPredicate);
    }

    private @NotNull InsnNode createInsn(int opcode) {
        return new InsnNode(opcode);
    }


    public void insn(int opcode) {
        addInsnNode(createInsn(opcode));
    }

    public void line(int num, LabelNode labelNode) {
        addInsnNode(new LineNumberNode(num, labelNode));
    }

    // nop

    public void nop() {
        insn(Opcodes.NOP);
    }

    // aconst_null

    public void aconst_null() {
        insn(Opcodes.ACONST_NULL);
    }


    public void iconst_m1() {
        insn(Opcodes.ICONST_M1);
    }


    public void iconst_0() {
        insn(Opcodes.ICONST_0);
    }


    public void iconst_1() {
        insn(Opcodes.ICONST_1);
    }


    public void iconst_2() {
        insn(Opcodes.ICONST_2);
    }


    public void iconst_3() {
        insn(Opcodes.ICONST_3);
    }


    public void iconst_4() {
        insn(Opcodes.ICONST_4);
    }


    public void iconst_5() {
        insn(Opcodes.ICONST_5);
    }


    public void lconst_0() {
        insn(Opcodes.LCONST_0);
    }


    public void lconst_1() {
        insn(Opcodes.LCONST_1);
    }


    public void fconst_0() {
        insn(Opcodes.FCONST_0);
    }


    public void fconst_1() {
        insn(Opcodes.FCONST_1);
    }


    public void fconst_2() {
        insn(Opcodes.FCONST_2);
    }


    public void dconst_0() {
        insn(Opcodes.DCONST_0);
    }


    public void dconst_1() {
        insn(Opcodes.DCONST_1);
    }


    public void sipush(int value) {
        addInsnNode(new IntInsnNode(Opcodes.SIPUSH, value));
    }


    public void bipush(int value) {
        addInsnNode(new IntInsnNode(Opcodes.BIPUSH, value));
    }

    // ldc

    public void ldc(int value) {
        addInsnNode(InsnUtil.getIntInsn(value));
    }


    public void ldc(long value) {
        addInsnNode(InsnUtil.getLongInsn(value));
    }


    public void ldc(float value) {
        addInsnNode(InsnUtil.getFloatInsn(value));
    }


    public void ldc(double value) {
        addInsnNode(InsnUtil.getDoubleInsn(value));
    }


    public void ldc(String value) {
        addInsnNode(new LdcInsnNode(value));
    }


    public void ldc(Type type) {
        addInsnNode(new LdcInsnNode(type));
    }


    public void ldc(Handle handle) {
        addInsnNode(new LdcInsnNode(handle));
    }

    // load

    public void iload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ILOAD, slot));
    }


    public void lload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.LLOAD, slot));
    }


    public void fload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.FLOAD, slot));
    }


    public void dload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.DLOAD, slot));
    }


    public void aload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ALOAD, slot));
    }


    public void iaload() {
        insn(Opcodes.IALOAD);
    }


    public void laload() {
        insn(Opcodes.LALOAD);
    }


    public void faload() {
        insn(Opcodes.FALOAD);
    }


    public void daload() {
        insn(Opcodes.DALOAD);
    }


    public void aaload() {
        insn(Opcodes.AALOAD);
    }


    public void baload() {
        insn(Opcodes.BALOAD);
    }


    public void caload() {
        insn(Opcodes.CALOAD);
    }


    public void saload() {
        insn(Opcodes.SALOAD);
    }

    // store

    public void istore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ISTORE, slot));
    }


    public void lstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.LSTORE, slot));
    }


    public void fstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.FSTORE, slot));
    }


    public void dstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.DSTORE, slot));
    }


    public void astore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ASTORE, slot));
    }


    public void iastore() {
        insn(Opcodes.IASTORE);
    }


    public void lastore() {
        insn(Opcodes.LASTORE);
    }


    public void fastore() {
        insn(Opcodes.FASTORE);
    }


    public void dastore() {
        insn(Opcodes.DASTORE);
    }


    public void aastore() {
        insn(Opcodes.AASTORE);
    }


    public void bastore() {
        insn(Opcodes.BASTORE);
    }


    public void castore() {
        insn(Opcodes.CASTORE);
    }


    public void sastore() {
        insn(Opcodes.SASTORE);
    }

    // pop

    public void pop() {
        insn(Opcodes.POP);
    }


    public void pop2() {
        insn(Opcodes.POP2);
    }


    // dup
    public void dup() {
        insn(Opcodes.DUP);
    }


    public void dup_x1() {
        insn(Opcodes.DUP_X1);
    }


    public void dup_x2() {
        insn(Opcodes.DUP_X2);
    }


    public void dup2() {
        insn(Opcodes.DUP2);
    }


    public void dup2_x1() {
        insn(Opcodes.DUP2_X1);
    }


    public void dup2_x2() {
        insn(Opcodes.DUP2_X2);
    }

    // calculate

    public void swap() {
        insn(Opcodes.SWAP);
    }


    public void iadd() {
        insn(Opcodes.IADD);
    }


    public void ladd() {
        insn(Opcodes.LADD);
    }


    public void fadd() {
        insn(Opcodes.FADD);
    }


    public void dadd() {
        insn(Opcodes.DADD);
    }


    public void isub() {
        insn(Opcodes.ISUB);
    }


    public void lsub() {
        insn(Opcodes.LSUB);
    }


    public void fsub() {
        insn(Opcodes.FSUB);
    }


    public void dsub() {
        insn(Opcodes.DSUB);
    }


    public void imul() {
        insn(Opcodes.IMUL);
    }


    public void lmul() {
        insn(Opcodes.LMUL);
    }


    public void fmul() {
        insn(Opcodes.FMUL);
    }


    public void dmul() {
        insn(Opcodes.DMUL);
    }


    public void idiv() {
        insn(Opcodes.IDIV);
    }


    public void ldiv() {
        insn(Opcodes.LDIV);
    }


    public void fdiv() {
        insn(Opcodes.FDIV);
    }


    public void ddiv() {
        insn(Opcodes.DDIV);
    }


    public void irem() {
        insn(Opcodes.IREM);
    }


    public void lrem() {
        insn(Opcodes.LREM);
    }


    public void frem() {
        insn(Opcodes.FREM);
    }


    public void drem() {
        insn(Opcodes.DREM);
    }


    public void ineg() {
        insn(Opcodes.INEG);
    }


    public void lneg() {
        insn(Opcodes.LNEG);
    }


    public void fneg() {
        insn(Opcodes.FNEG);
    }


    public void dneg() {
        insn(Opcodes.DNEG);
    }


    public void ishl() {
        insn(Opcodes.ISHL);
    }


    public void lshl() {
        insn(Opcodes.LSHL);
    }


    public void ishr() {
        insn(Opcodes.ISHR);
    }


    public void lshr() {
        insn(Opcodes.LSHR);
    }


    public void iushr() {
        insn(Opcodes.IUSHR);
    }


    public void lushr() {
        insn(Opcodes.LUSHR);
    }


    public void iand() {
        insn(Opcodes.IAND);
    }


    public void land() {
        insn(Opcodes.LAND);
    }


    public void ior() {
        insn(Opcodes.IOR);
    }


    public void lor() {
        insn(Opcodes.LOR);
    }


    public void ixor() {
        insn(Opcodes.IXOR);
    }


    public void lxor() {
        insn(Opcodes.LXOR);
    }


    public void iinc(int slot, int incr) {
        addInsnNode(new IincInsnNode(slot, incr));
    }

    // covert

    public void i2l() {
        insn(Opcodes.I2L);
    }


    public void i2f() {
        insn(Opcodes.I2F);
    }


    public void i2d() {
        insn(Opcodes.I2D);
    }


    public void l2i() {
        insn(Opcodes.L2I);
    }


    public void l2f() {
        insn(Opcodes.L2F);
    }


    public void l2d() {
        insn(Opcodes.L2D);
    }


    public void f2i() {
        insn(Opcodes.F2I);
    }


    public void f2l() {
        insn(Opcodes.F2L);
    }


    public void f2d() {
        insn(Opcodes.F2D);
    }


    public void d2i() {
        insn(Opcodes.D2I);
    }


    public void d2l() {
        insn(Opcodes.D2L);
    }


    public void d2f() {
        insn(Opcodes.D2F);
    }


    public void i2b() {
        insn(Opcodes.I2B);
    }


    public void i2c() {
        insn(Opcodes.I2C);
    }


    public void i2s() {
        insn(Opcodes.I2S);
    }

    // compare

    public void lcmp() {
        insn(Opcodes.LCMP);
    }


    public void fcmpl() {
        insn(Opcodes.FCMPL);
    }


    public void fcmpg() {
        insn(Opcodes.FCMPG);
    }


    public void dcmpl() {
        insn(Opcodes.DCMPL);
    }


    public void dcmpg() {
        insn(Opcodes.DCMPG);
    }

    // label

    public void label(LabelNode label) {
        addInsnNode(label);
    }

    // control flow
    // just goto instruction

    public void ifeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFEQ, label));
    }


    public void ifne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNE, label));
    }


    public void iflt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLT, label));
    }


    public void ifge(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGE, label));
    }


    public void ifgt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGT, label));
    }


    public void ifle(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLE, label));
    }


    public void if_icmpeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
    }


    public void if_icmpne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPNE, label));
    }


    public void if_icmplt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLT, label));
    }


    public void if_icmpge(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGE, label));
    }


    public void if_icmpgt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGT, label));
    }


    public void if_icmple(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLE, label));
    }


    public void if_acmpeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPEQ, label));
    }


    public void if_acmpne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPNE, label));
    }


    public void gotoo(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.GOTO, label));
    }


    public void jsr(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.JSR, label));
    }


    public void ret(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.RET, slot));
    }


    public void tableswitch(LabelNode defaultLabel, int min, int max, LabelNode[] labels) {
        addInsnNode(new TableSwitchInsnNode(min, max, defaultLabel, labels));
    }


    public void lookupswitch(LabelNode defaultLabel, int[] keys, LabelNode[] labels) {
        addInsnNode(new LookupSwitchInsnNode(defaultLabel, keys, labels));
    }

    // return

    public void ireturn() {
        addInsnNode(new InsnNode(Opcodes.IRETURN));
    }


    public void lreturn() {
        addInsnNode(new InsnNode(Opcodes.LRETURN));
    }


    public void freturn() {
        addInsnNode(new InsnNode(Opcodes.FRETURN));
    }


    public void dreturn() {
        addInsnNode(new InsnNode(Opcodes.DRETURN));
    }


    public void areturn() {
        addInsnNode(new InsnNode(Opcodes.ARETURN));
    }


    public void returnn() {
        addInsnNode(new InsnNode(Opcodes.RETURN));
    }

    // field and method

    public void getstatic(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.GETSTATIC, owner, name, desc));
    }


    public void putstatic(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.PUTSTATIC, owner, name, desc));
    }


    public void getfield(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.GETFIELD, owner, name, desc));
    }


    public void putfield(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.PUTFIELD, owner, name, desc));
    }


    public void invokevirtual(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, desc, false));
    }


    public void invokevirtual(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, desc, Interface));
    }


    public void invokespecial(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, desc, false));
    }


    public void invokespecial(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, desc, Interface));
    }


    public void invokestatic(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false));
    }


    public void invokestatic(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, Interface));
    }


    public void invokeinterface(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEINTERFACE, owner, name, desc, true));
    }


    public void invokeinterface(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEINTERFACE, owner, name, desc, Interface));
    }


    public void invokedynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        addInsnNode(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    // new
    // new instruction

    public void neww(String type) {
        addInsnNode(new TypeInsnNode(Opcodes.NEW, type));
    }


    public void newarray(int type) {
        addInsnNode(new IntInsnNode(Opcodes.NEWARRAY, type));
    }

    // new array

    public void newboolarray() {
        newarray(Opcodes.T_BOOLEAN);
    }


    public void newchararray() {
        newarray(Opcodes.T_CHAR);
    }


    public void newbytearray() {
        newarray(Opcodes.T_BYTE);
    }


    public void newshortarray() {
        newarray(Opcodes.T_SHORT);
    }


    public void newintarray() {
        newarray(Opcodes.T_INT);
    }


    public void newlongarray() {
        newarray(Opcodes.T_LONG);
    }


    public void newfloatarray() {
        newarray(Opcodes.T_FLOAT);
    }


    public void newdoublearray() {
        newarray(Opcodes.T_DOUBLE);
    }


    public void anewarray(String type) {
        addInsnNode(new TypeInsnNode(Opcodes.ANEWARRAY, type));
    }


    public void arraylength() {
        addInsnNode(new InsnNode(Opcodes.ARRAYLENGTH));
    }

    // exception

    public void athrow() {
        addInsnNode(new InsnNode(Opcodes.ATHROW));
    }


    public void checkcast(String descriptor) {
        addInsnNode(new TypeInsnNode(Opcodes.CHECKCAST, descriptor));
    }


    public void instanceoff(String descriptor) {
        addInsnNode(new TypeInsnNode(Opcodes.INSTANCEOF, descriptor));
    }


    public void monitorenter() {
        addInsnNode(new InsnNode(Opcodes.MONITORENTER));
    }


    public void monitorexit() {
        addInsnNode(new InsnNode(Opcodes.MONITOREXIT));
    }


    public void multiarray(String descriptor, int dims) {
        addInsnNode(new MultiANewArrayInsnNode(descriptor, dims));
    }


    public void ifnull(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNULL, label));
    }


    public void ifnonnull(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNONNULL, label));
    }

    // frame

    public void frame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        addInsnNode(new FrameNode(type, numLocal, local, numStack, stack));
    }

    /*
     * 更先进的指令生成器
     * 方便手写指令
     */
    // 为了方便写标签而不需要new LabelNode
    // 所有标签直接用字符串表示
    private final Map<String, LabelNode> labelMap = new HashMap<>();

    public void label(String labelName) {
        if (!labelMap.containsKey(labelName)) {
            // 如果不存在就先创建一个
            LabelNode labelNode = new LabelNode();
            labelMap.put(labelName, labelNode);
            label(labelNode);
        } else {
            label(labelMap.get(labelName));
        }
    }

    private LabelNode getLabelByName(String labelName) {
        if (!labelMap.containsKey(labelName)) {
            // 如果不存在就先创建一个
            LabelNode labelNode = new LabelNode();
            labelMap.put(labelName, labelNode);
            return labelNode;
        } else {
            return labelMap.get(labelName);
        }
    }

    public void ifeq(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFEQ, getLabelByName(labelName)));
    }

    public void ifne(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNE, getLabelByName(labelName)));
    }

    public void iflt(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLT, getLabelByName(labelName)));
    }

    public void ifge(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGE, getLabelByName(labelName)));
    }

    public void ifgt(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGT, getLabelByName(labelName)));
    }

    public void ifle(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLE, getLabelByName(labelName)));
    }

    public void if_icmpeq(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPEQ, getLabelByName(labelName)));
    }

    public void if_icmpne(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPNE, getLabelByName(labelName)));
    }

    public void if_icmplt(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLT, getLabelByName(labelName)));
    }

    public void if_icmpge(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGE, getLabelByName(labelName)));
    }

    public void if_icmpgt(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGT, getLabelByName(labelName)));
    }

    public void if_icmple(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLE, getLabelByName(labelName)));
    }

    public void if_acmpeq(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPEQ, getLabelByName(labelName)));
    }

    public void if_acmpne(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPNE, getLabelByName(labelName)));
    }

    public void gotoo(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.GOTO, getLabelByName(labelName)));
    }

    public void jsr(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.JSR, getLabelByName(labelName)));
    }

    public void ifnull(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNULL, getLabelByName(labelName)));
    }

    public void ifnonnull(String labelName) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNONNULL, getLabelByName(labelName)));
    }

    public void line(int num, String labelName) {
        addInsnNode(new LineNumberNode(num, getLabelByName(labelName)));
    }

    /**
     * 局部变量表，name -> slot
     */
    private final Map<String, Integer> localVars = new HashMap<>();

    private int getSlot(String varName) {
        // 如果不存在，则创建一个
        if (!localVars.containsKey(varName)) {
            localVars.put(varName, localVars.size());
        }
        return localVars.get(varName);
    }

    public void nameArgs(String desc, String @NotNull ... names) {
        int cnt = Type.getArgumentCount(desc);
        if (cnt != names.length) {
            logger.error("When naming parameters, the number of parameters does not match, desc is {}, but your args is {}", cnt, names.length);
            return;
        }
        for (int i = 0; i < cnt; i++) {
            getSlot(names[i]);
        }
    }

    public void adjustArgs(String desc) {
        int cnt = Type.getArgumentCount(desc);
        for (int i = 1; i <= cnt; i++) {
            getSlot("arg" + i);
        }
    }

    public void iload(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.ILOAD, getSlot(varName)));
    }

    public void lload(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.LLOAD, getSlot(varName)));
    }

    public void fload(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.FLOAD, getSlot(varName)));
    }

    public void dload(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.DLOAD, getSlot(varName)));
    }

    public void aload(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.ALOAD, getSlot(varName)));
    }

    public void istore(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.ISTORE, getSlot(varName)));
    }

    public void lstore(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.LSTORE, getSlot(varName)));
    }

    public void fstore(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.FSTORE, getSlot(varName)));
    }

    public void dstore(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.DSTORE, getSlot(varName)));
    }

    public void astore(String varName) {
        addInsnNode(new VarInsnNode(Opcodes.ASTORE, getSlot(varName)));
    }

    public void iinc(String varName, int incr) {
        addInsnNode(new IincInsnNode(getSlot(varName), incr));
    }
// 没有加jasm，以后再加这个
//    /**
//     * 以recaf的jasm风格字符串作为输入，一行一条指令，返回包含指令的指令构建器
//     * @param str 指令字符串
//     * @return 指令构建器
//     */
//    public static @NotNull InsnBuilder fromString(String str) {
//        InsnBuilder builder = new InsnBuilder();
//
//        // 开始解析字符串
//        // 直接\n拆解
//        for (String line : str.split("\n")) {
//
//        }
//
//        return builder;
//    }
}

