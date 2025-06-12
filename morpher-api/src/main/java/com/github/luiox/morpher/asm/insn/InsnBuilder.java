package com.github.luiox.morpher.asm.insn;

import com.github.luiox.morpher.annotation.API;
import com.github.luiox.morpher.annotation.APIStatus;
import com.github.luiox.morpher.util.LogUtil;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 原始的 InsnBuilder，用于生成 InsnList，比较基础
 * 实现从方法到字节码指令一一映射
 */
@API(status = APIStatus.Stable)
public class InsnBuilder {
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

    @API(status = APIStatus.Stable)
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

    @API(status = APIStatus.Stable)
    public static @NotNull InsnList build(@NotNull Consumer<InsnBuilder> builderAction) {
        InsnBuilder builder = new InsnBuilder();
        builderAction.accept(builder);
        return toInsnList(builder);
    }

    @API(status = APIStatus.Stable)
    public @NotNull InsnList getInsnList() {
        return toInsnList(this);
    }

    @API(status = APIStatus.Stable)
    public @NotNull InsnList getInsnList(@NotNull Predicate<AbstractInsnNode> skipPredicate) {
        return toInsnList(this, skipPredicate);
    }

    private @NotNull InsnNode createInsn(int opcode) {
        return new InsnNode(opcode);
    }

    @API(status = APIStatus.Stable)
    public void insn(int opcode) {
        addInsnNode(createInsn(opcode));
    }

    public void line(int num, LabelNode labelNode) {
        addInsnNode(new LineNumberNode(num, labelNode));
    }

    // nop
    @API(status = APIStatus.Stable)
    public void nop() {
        insn(Opcodes.NOP);
    }

    // aconst_null
    @API(status = APIStatus.Stable)
    public void aconst_null() {
        insn(Opcodes.ACONST_NULL);
    }

    @API(status = APIStatus.Stable)
    public void iconst_m1() {
        insn(Opcodes.ICONST_M1);
    }

    @API(status = APIStatus.Stable)
    public void iconst_0() {
        insn(Opcodes.ICONST_0);
    }

    @API(status = APIStatus.Stable)
    public void iconst_1() {
        insn(Opcodes.ICONST_1);
    }

    @API(status = APIStatus.Stable)
    public void iconst_2() {
        insn(Opcodes.ICONST_2);
    }

    @API(status = APIStatus.Stable)
    public void iconst_3() {
        insn(Opcodes.ICONST_3);
    }

    @API(status = APIStatus.Stable)
    public void iconst_4() {
        insn(Opcodes.ICONST_4);
    }

    @API(status = APIStatus.Stable)
    public void iconst_5() {
        insn(Opcodes.ICONST_5);
    }

    @API(status = APIStatus.Stable)
    public void lconst_0() {
        insn(Opcodes.LCONST_0);
    }

    @API(status = APIStatus.Stable)
    public void lconst_1() {
        insn(Opcodes.LCONST_1);
    }

    @API(status = APIStatus.Stable)
    public void fconst_0() {
        insn(Opcodes.FCONST_0);
    }

    @API(status = APIStatus.Stable)
    public void fconst_1() {
        insn(Opcodes.FCONST_1);
    }

    @API(status = APIStatus.Stable)
    public void fconst_2() {
        insn(Opcodes.FCONST_2);
    }

    @API(status = APIStatus.Stable)
    public void dconst_0() {
        insn(Opcodes.DCONST_0);
    }

    @API(status = APIStatus.Stable)
    public void dconst_1() {
        insn(Opcodes.DCONST_1);
    }

    @API(status = APIStatus.Stable)
    public void sipush(int value) {
        addInsnNode(new IntInsnNode(Opcodes.SIPUSH, value));
    }

    @API(status = APIStatus.Stable)
    public void bipush(int value) {
        addInsnNode(new IntInsnNode(Opcodes.BIPUSH, value));
    }

    // ldc
    @API(status = APIStatus.Stable)
    public void ldc(int value) {
        addInsnNode(InsnUtil.getIntInsn(value));
    }

    @API(status = APIStatus.Stable)
    public void ldc(long value) {
        addInsnNode(InsnUtil.getLongInsn(value));
    }

    @API(status = APIStatus.Stable)
    public void ldc(float value) {
        addInsnNode(InsnUtil.getFloatInsn(value));
    }

    @API(status = APIStatus.Stable)
    public void ldc(double value) {
        addInsnNode(InsnUtil.getDoubleInsn(value));
    }

    @API(status = APIStatus.Stable)
    public void ldc(String value) {
        addInsnNode(new LdcInsnNode(value));
    }

    @API(status = APIStatus.Stable)
    public void ldc(Type type) {
        addInsnNode(new LdcInsnNode(type));
    }

    @API(status = APIStatus.Stable)
    public void ldc(Handle handle) {
        addInsnNode(new LdcInsnNode(handle));
    }

    // load
    @API(status = APIStatus.Stable)
    public void iload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ILOAD, slot));
    }

    @API(status = APIStatus.Stable)
    public void lload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.LLOAD, slot));
    }

    @API(status = APIStatus.Stable)
    public void fload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.FLOAD, slot));
    }

    @API(status = APIStatus.Stable)
    public void dload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.DLOAD, slot));
    }

    @API(status = APIStatus.Stable)
    public void aload(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ALOAD, slot));
    }

    @API(status = APIStatus.Stable)
    public void iaload() {
        insn(Opcodes.IALOAD);
    }

    @API(status = APIStatus.Stable)
    public void laload() {
        insn(Opcodes.LALOAD);
    }

    @API(status = APIStatus.Stable)
    public void faload() {
        insn(Opcodes.FALOAD);
    }

    @API(status = APIStatus.Stable)
    public void daload() {
        insn(Opcodes.DALOAD);
    }

    @API(status = APIStatus.Stable)
    public void aaload() {
        insn(Opcodes.AALOAD);
    }

    @API(status = APIStatus.Stable)
    public void baload() {
        insn(Opcodes.BALOAD);
    }

    @API(status = APIStatus.Stable)
    public void caload() {
        insn(Opcodes.CALOAD);
    }

    @API(status = APIStatus.Stable)
    public void saload() {
        insn(Opcodes.SALOAD);
    }

    // store
    @API(status = APIStatus.Stable)
    public void istore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ISTORE, slot));
    }

    @API(status = APIStatus.Stable)
    public void lstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.LSTORE, slot));
    }

    @API(status = APIStatus.Stable)
    public void fstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.FSTORE, slot));
    }

    @API(status = APIStatus.Stable)
    public void dstore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.DSTORE, slot));
    }

    @API(status = APIStatus.Stable)
    public void astore(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.ASTORE, slot));
    }

    @API(status = APIStatus.Stable)
    public void iastore() {
        insn(Opcodes.IASTORE);
    }

    @API(status = APIStatus.Stable)
    public void lastore() {
        insn(Opcodes.LASTORE);
    }

    @API(status = APIStatus.Stable)
    public void fastore() {
        insn(Opcodes.FASTORE);
    }

    @API(status = APIStatus.Stable)
    public void dastore() {
        insn(Opcodes.DASTORE);
    }

    @API(status = APIStatus.Stable)
    public void aastore() {
        insn(Opcodes.AASTORE);
    }

    @API(status = APIStatus.Stable)
    public void bastore() {
        insn(Opcodes.BASTORE);
    }

    @API(status = APIStatus.Stable)
    public void castore() {
        insn(Opcodes.CASTORE);
    }

    @API(status = APIStatus.Stable)
    public void sastore() {
        insn(Opcodes.SASTORE);
    }

    // pop
    @API(status = APIStatus.Stable)
    public void pop() {
        insn(Opcodes.POP);
    }

    @API(status = APIStatus.Stable)
    public void pop2() {
        insn(Opcodes.POP2);
    }

    @API(status = APIStatus.Stable)

    // dup
    public void dup() {
        insn(Opcodes.DUP);
    }

    @API(status = APIStatus.Stable)
    public void dup_x1() {
        insn(Opcodes.DUP_X1);
    }

    @API(status = APIStatus.Stable)
    public void dup_x2() {
        insn(Opcodes.DUP_X2);
    }

    @API(status = APIStatus.Stable)
    public void dup2() {
        insn(Opcodes.DUP2);
    }

    @API(status = APIStatus.Stable)
    public void dup2_x1() {
        insn(Opcodes.DUP2_X1);
    }

    @API(status = APIStatus.Stable)
    public void dup2_x2() {
        insn(Opcodes.DUP2_X2);
    }

    // calculate
    @API(status = APIStatus.Stable)
    public void swap() {
        insn(Opcodes.SWAP);
    }

    @API(status = APIStatus.Stable)
    public void iadd() {
        insn(Opcodes.IADD);
    }

    @API(status = APIStatus.Stable)
    public void ladd() {
        insn(Opcodes.LADD);
    }

    @API(status = APIStatus.Stable)
    public void fadd() {
        insn(Opcodes.FADD);
    }

    @API(status = APIStatus.Stable)
    public void dadd() {
        insn(Opcodes.DADD);
    }

    @API(status = APIStatus.Stable)
    public void isub() {
        insn(Opcodes.ISUB);
    }

    @API(status = APIStatus.Stable)
    public void lsub() {
        insn(Opcodes.LSUB);
    }

    @API(status = APIStatus.Stable)
    public void fsub() {
        insn(Opcodes.FSUB);
    }

    @API(status = APIStatus.Stable)
    public void dsub() {
        insn(Opcodes.DSUB);
    }

    @API(status = APIStatus.Stable)
    public void imul() {
        insn(Opcodes.IMUL);
    }

    @API(status = APIStatus.Stable)
    public void lmul() {
        insn(Opcodes.LMUL);
    }

    @API(status = APIStatus.Stable)
    public void fmul() {
        insn(Opcodes.FMUL);
    }

    @API(status = APIStatus.Stable)
    public void dmul() {
        insn(Opcodes.DMUL);
    }

    @API(status = APIStatus.Stable)
    public void idiv() {
        insn(Opcodes.IDIV);
    }

    @API(status = APIStatus.Stable)
    public void ldiv() {
        insn(Opcodes.LDIV);
    }

    @API(status = APIStatus.Stable)
    public void fdiv() {
        insn(Opcodes.FDIV);
    }

    @API(status = APIStatus.Stable)
    public void ddiv() {
        insn(Opcodes.DDIV);
    }

    @API(status = APIStatus.Stable)
    public void irem() {
        insn(Opcodes.IREM);
    }

    @API(status = APIStatus.Stable)
    public void lrem() {
        insn(Opcodes.LREM);
    }

    @API(status = APIStatus.Stable)
    public void frem() {
        insn(Opcodes.FREM);
    }

    @API(status = APIStatus.Stable)
    public void drem() {
        insn(Opcodes.DREM);
    }

    @API(status = APIStatus.Stable)
    public void ineg() {
        insn(Opcodes.INEG);
    }

    @API(status = APIStatus.Stable)
    public void lneg() {
        insn(Opcodes.LNEG);
    }

    @API(status = APIStatus.Stable)
    public void fneg() {
        insn(Opcodes.FNEG);
    }

    @API(status = APIStatus.Stable)
    public void dneg() {
        insn(Opcodes.DNEG);
    }

    @API(status = APIStatus.Stable)
    public void ishl() {
        insn(Opcodes.ISHL);
    }

    @API(status = APIStatus.Stable)
    public void lshl() {
        insn(Opcodes.LSHL);
    }

    @API(status = APIStatus.Stable)
    public void ishr() {
        insn(Opcodes.ISHR);
    }

    @API(status = APIStatus.Stable)
    public void lshr() {
        insn(Opcodes.LSHR);
    }

    @API(status = APIStatus.Stable)
    public void iushr() {
        insn(Opcodes.IUSHR);
    }

    @API(status = APIStatus.Stable)
    public void lushr() {
        insn(Opcodes.LUSHR);
    }

    @API(status = APIStatus.Stable)
    public void iand() {
        insn(Opcodes.IAND);
    }

    @API(status = APIStatus.Stable)
    public void land() {
        insn(Opcodes.LAND);
    }

    @API(status = APIStatus.Stable)
    public void ior() {
        insn(Opcodes.IOR);
    }

    @API(status = APIStatus.Stable)
    public void lor() {
        insn(Opcodes.LOR);
    }

    @API(status = APIStatus.Stable)
    public void ixor() {
        insn(Opcodes.IXOR);
    }

    @API(status = APIStatus.Stable)
    public void lxor() {
        insn(Opcodes.LXOR);
    }

    @API(status = APIStatus.Stable)
    public void iinc(int slot, int incr) {
        addInsnNode(new IincInsnNode(slot, incr));
    }

    // covert
    @API(status = APIStatus.Stable)
    public void i2l() {
        insn(Opcodes.I2L);
    }

    @API(status = APIStatus.Stable)
    public void i2f() {
        insn(Opcodes.I2F);
    }

    @API(status = APIStatus.Stable)
    public void i2d() {
        insn(Opcodes.I2D);
    }

    @API(status = APIStatus.Stable)
    public void l2i() {
        insn(Opcodes.L2I);
    }

    @API(status = APIStatus.Stable)
    public void l2f() {
        insn(Opcodes.L2F);
    }

    @API(status = APIStatus.Stable)
    public void l2d() {
        insn(Opcodes.L2D);
    }

    @API(status = APIStatus.Stable)
    public void f2i() {
        insn(Opcodes.F2I);
    }

    @API(status = APIStatus.Stable)
    public void f2l() {
        insn(Opcodes.F2L);
    }

    @API(status = APIStatus.Stable)
    public void f2d() {
        insn(Opcodes.F2D);
    }

    @API(status = APIStatus.Stable)
    public void d2i() {
        insn(Opcodes.D2I);
    }

    @API(status = APIStatus.Stable)
    public void d2l() {
        insn(Opcodes.D2L);
    }

    @API(status = APIStatus.Stable)
    public void d2f() {
        insn(Opcodes.D2F);
    }

    @API(status = APIStatus.Stable)
    public void i2b() {
        insn(Opcodes.I2B);
    }

    @API(status = APIStatus.Stable)
    public void i2c() {
        insn(Opcodes.I2C);
    }

    @API(status = APIStatus.Stable)
    public void i2s() {
        insn(Opcodes.I2S);
    }

    // compare
    @API(status = APIStatus.Stable)
    public void lcmp() {
        insn(Opcodes.LCMP);
    }

    @API(status = APIStatus.Stable)
    public void fcmpl() {
        insn(Opcodes.FCMPL);
    }

    @API(status = APIStatus.Stable)
    public void fcmpg() {
        insn(Opcodes.FCMPG);
    }

    @API(status = APIStatus.Stable)
    public void dcmpl() {
        insn(Opcodes.DCMPL);
    }

    @API(status = APIStatus.Stable)
    public void dcmpg() {
        insn(Opcodes.DCMPG);
    }

    // label
    @API(status = APIStatus.Stable)
    public void label(LabelNode label) {
        addInsnNode(label);
    }

    // control flow
    // just goto instruction
    @API(status = APIStatus.Stable)
    public void ifeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFEQ, label));
    }

    @API(status = APIStatus.Stable)
    public void ifne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNE, label));
    }

    @API(status = APIStatus.Stable)
    public void iflt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLT, label));
    }

    @API(status = APIStatus.Stable)
    public void ifge(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGE, label));
    }

    @API(status = APIStatus.Stable)
    public void ifgt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFGT, label));
    }

    @API(status = APIStatus.Stable)
    public void ifle(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFLE, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmpeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmpne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPNE, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmplt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLT, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmpge(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGE, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmpgt(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPGT, label));
    }

    @API(status = APIStatus.Stable)
    public void if_icmple(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ICMPLE, label));
    }

    @API(status = APIStatus.Stable)
    public void if_acmpeq(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPEQ, label));
    }

    @API(status = APIStatus.Stable)
    public void if_acmpne(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IF_ACMPNE, label));
    }

    @API(status = APIStatus.Stable)
    public void gotoo(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.GOTO, label));
    }

    @API(status = APIStatus.Stable)
    public void jsr(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.JSR, label));
    }

    @API(status = APIStatus.Stable)
    public void ret(int slot) {
        addInsnNode(new VarInsnNode(Opcodes.RET, slot));
    }

    @API(status = APIStatus.Stable)
    public void tableswitch(LabelNode defaultLabel, int min, int max, LabelNode[] labels) {
        addInsnNode(new TableSwitchInsnNode(min, max, defaultLabel, labels));
    }

    @API(status = APIStatus.Stable)
    public void lookupswitch(LabelNode defaultLabel, int[] keys, LabelNode[] labels) {
        addInsnNode(new LookupSwitchInsnNode(defaultLabel, keys, labels));
    }

    // return
    @API(status = APIStatus.Stable)
    public void ireturn() {
        addInsnNode(new InsnNode(Opcodes.IRETURN));
    }

    @API(status = APIStatus.Stable)
    public void lreturn() {
        addInsnNode(new InsnNode(Opcodes.LRETURN));
    }

    @API(status = APIStatus.Stable)
    public void freturn() {
        addInsnNode(new InsnNode(Opcodes.FRETURN));
    }

    @API(status = APIStatus.Stable)
    public void dreturn() {
        addInsnNode(new InsnNode(Opcodes.DRETURN));
    }

    @API(status = APIStatus.Stable)
    public void areturn() {
        addInsnNode(new InsnNode(Opcodes.ARETURN));
    }

    @API(status = APIStatus.Stable)
    public void returnn() {
        addInsnNode(new InsnNode(Opcodes.RETURN));
    }

    // field and method
    @API(status = APIStatus.Stable)
    public void getstatic(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.GETSTATIC, owner, name, desc));
    }

    @API(status = APIStatus.Stable)
    public void putstatic(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.PUTSTATIC, owner, name, desc));
    }

    @API(status = APIStatus.Stable)
    public void getfield(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.GETFIELD, owner, name, desc));
    }

    @API(status = APIStatus.Stable)
    public void putfield(String owner, String name, String desc) {
        addInsnNode(new FieldInsnNode(Opcodes.PUTFIELD, owner, name, desc));
    }

    @API(status = APIStatus.Stable)
    public void invokevirtual(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, desc, false));
    }

    @API(status = APIStatus.Stable)
    public void invokevirtual(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, name, desc, Interface));
    }

    @API(status = APIStatus.Stable)
    public void invokespecial(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, desc, false));
    }

    @API(status = APIStatus.Stable)
    public void invokespecial(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESPECIAL, owner, name, desc, Interface));
    }

    @API(status = APIStatus.Stable)
    public void invokestatic(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false));
    }

    @API(status = APIStatus.Stable)
    public void invokestatic(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, Interface));
    }

    @API(status = APIStatus.Stable)
    public void invokeinterface(String owner, String name, String desc) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEINTERFACE, owner, name, desc, true));
    }

    @API(status = APIStatus.Stable)
    public void invokeinterface(String owner, String name, String desc, boolean Interface) {
        addInsnNode(new MethodInsnNode(Opcodes.INVOKEINTERFACE, owner, name, desc, Interface));
    }

    @API(status = APIStatus.Stable)
    public void invokedynamic(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        addInsnNode(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    // new
    // new instruction
    @API(status = APIStatus.Stable)
    public void neww(String type) {
        addInsnNode(new TypeInsnNode(Opcodes.NEW, type));
    }

    @API(status = APIStatus.Stable)
    public void newarray(int type) {
        addInsnNode(new IntInsnNode(Opcodes.NEWARRAY, type));
    }

    // new array
    @API(status = APIStatus.Stable)
    public void newboolarray() {
        newarray(Opcodes.T_BOOLEAN);
    }

    @API(status = APIStatus.Stable)
    public void newchararray() {
        newarray(Opcodes.T_CHAR);
    }

    @API(status = APIStatus.Stable)
    public void newbytearray() {
        newarray(Opcodes.T_BYTE);
    }

    @API(status = APIStatus.Stable)
    public void newshortarray() {
        newarray(Opcodes.T_SHORT);
    }

    @API(status = APIStatus.Stable)
    public void newintarray() {
        newarray(Opcodes.T_INT);
    }

    @API(status = APIStatus.Stable)
    public void newlongarray() {
        newarray(Opcodes.T_LONG);
    }

    @API(status = APIStatus.Stable)
    public void newfloatarray() {
        newarray(Opcodes.T_FLOAT);
    }

    @API(status = APIStatus.Stable)
    public void newdoublearray() {
        newarray(Opcodes.T_DOUBLE);
    }

    @API(status = APIStatus.Stable)
    public void anewarray(String type) {
        addInsnNode(new TypeInsnNode(Opcodes.ANEWARRAY, type));
    }

    @API(status = APIStatus.Stable)
    public void arraylength() {
        addInsnNode(new InsnNode(Opcodes.ARRAYLENGTH));
    }

    // exception
    @API(status = APIStatus.Stable)
    public void athrow() {
        addInsnNode(new InsnNode(Opcodes.ATHROW));
    }

    @API(status = APIStatus.Stable)
    public void checkcast(String descriptor) {
        addInsnNode(new TypeInsnNode(Opcodes.CHECKCAST, descriptor));
    }

    @API(status = APIStatus.Stable)
    public void instanceoff(String descriptor) {
        addInsnNode(new TypeInsnNode(Opcodes.INSTANCEOF, descriptor));
    }

    @API(status = APIStatus.Stable)
    public void monitorenter() {
        addInsnNode(new InsnNode(Opcodes.MONITORENTER));
    }

    @API(status = APIStatus.Stable)
    public void monitorexit() {
        addInsnNode(new InsnNode(Opcodes.MONITOREXIT));
    }

    @API(status = APIStatus.Stable)
    public void multiarray(String descriptor, int dims) {
        addInsnNode(new MultiANewArrayInsnNode(descriptor, dims));
    }

    @API(status = APIStatus.Stable)
    public void ifnull(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNULL, label));
    }

    @API(status = APIStatus.Stable)
    public void ifnonnull(LabelNode label) {
        addInsnNode(new JumpInsnNode(Opcodes.IFNONNULL, label));
    }

    // frame
    @API(status = APIStatus.Stable)
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
            LogUtil.error("When naming parameters, the number of parameters does not match, desc is {}, but your args is {}", cnt, names.length);
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

