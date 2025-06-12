package com.github.luiox.morpher.asm.matcher;

import com.github.luiox.morpher.asm.matcher.step.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.function.Predicate;

/**
 * 帮助快速创建的step
 */
public class StepUtil {

    private static final SimpleStep[] NormalSimpleSteps = new SimpleStep[Opcodes.IFNONNULL + 1];

    public static @NotNull SimpleStep step(int opcode) {
        if (NormalSimpleSteps[opcode] != null) {
            return NormalSimpleSteps[opcode];
        }
        NormalSimpleSteps[opcode] = new SimpleStep(opcode);
        return NormalSimpleSteps[opcode];
    }

    public static @NotNull SimpleStep step(int opcode, @NotNull Predicate<AbstractInsnNode> condition) {
        return new SimpleStep(opcode, condition);
    }

    // InsnNode
    public static @NotNull IMatchStep nop() {
        return step(Opcodes.NOP);
    }

    public static @NotNull IMatchStep aconst_null() {
        return step(Opcodes.ACONST_NULL);
    }

    public static @NotNull IMatchStep iconst_m1() {
        return step(Opcodes.ICONST_M1);
    }

    public static @NotNull IMatchStep iconst_0() {
        return step(Opcodes.ICONST_0);
    }

    public static @NotNull IMatchStep iconst_1() {
        return step(Opcodes.ICONST_1);
    }

    public static @NotNull IMatchStep iconst_2() {
        return step(Opcodes.ICONST_2);
    }

    public static @NotNull IMatchStep iconst_3() {
        return step(Opcodes.ICONST_3);
    }

    public static @NotNull IMatchStep iconst_4() {
        return step(Opcodes.ICONST_4);
    }

    public static @NotNull IMatchStep iconst_5() {
        return step(Opcodes.ICONST_5);
    }

    public static @NotNull IMatchStep lconst_0() {
        return step(Opcodes.LCONST_0);
    }

    public static @NotNull IMatchStep lconst_1() {
        return step(Opcodes.LCONST_1);
    }

    public static @NotNull IMatchStep fconst_0() {
        return step(Opcodes.FCONST_0);
    }

    public static @NotNull IMatchStep fconst_1() {
        return step(Opcodes.FCONST_1);
    }

    public static @NotNull IMatchStep fconst_2() {
        return step(Opcodes.FCONST_2);
    }

    public static @NotNull IMatchStep dconst_0() {
        return step(Opcodes.DCONST_0);
    }

    public static @NotNull IMatchStep dconst_1() {
        return step(Opcodes.DCONST_1);
    }

    public static @NotNull IMatchStep pop() {
        return step(Opcodes.POP);
    }

    public static @NotNull IMatchStep pop2() {
        return step(Opcodes.POP2);
    }

    public static @NotNull IMatchStep dup() {
        return step(Opcodes.DUP);
    }

    public static @NotNull IMatchStep dup_x1() {
        return step(Opcodes.DUP_X1);
    }

    public static @NotNull IMatchStep dup_x2() {
        return step(Opcodes.DUP_X2);
    }

    public static @NotNull IMatchStep dup2() {
        return step(Opcodes.DUP2);
    }

    public static @NotNull IMatchStep dup2_x1() {
        return step(Opcodes.DUP2_X1);
    }

    public static @NotNull IMatchStep dup2_x2() {
        return step(Opcodes.DUP2_X2);
    }

    public static @NotNull IMatchStep swap() {
        return step(Opcodes.SWAP);
    }

    public static @NotNull IMatchStep iadd() {
        return step(Opcodes.IADD);
    }

    public static @NotNull IMatchStep ladd() {
        return step(Opcodes.LADD);
    }

    public static @NotNull IMatchStep fadd() {
        return step(Opcodes.FADD);
    }

    public static @NotNull IMatchStep dadd() {
        return step(Opcodes.DADD);
    }

    public static @NotNull IMatchStep isub() {
        return step(Opcodes.ISUB);
    }

    public static @NotNull IMatchStep lsub() {
        return step(Opcodes.LSUB);
    }

    public static @NotNull IMatchStep fsub() {
        return step(Opcodes.FSUB);
    }

    public static @NotNull IMatchStep dsub() {
        return step(Opcodes.DSUB);
    }

    public static @NotNull IMatchStep imul() {
        return step(Opcodes.IMUL);
    }

    public static @NotNull IMatchStep lmul() {
        return step(Opcodes.LMUL);
    }

    public static @NotNull IMatchStep fmul() {
        return step(Opcodes.FMUL);
    }

    public static @NotNull IMatchStep dmul() {
        return step(Opcodes.DMUL);
    }

    public static @NotNull IMatchStep idiv() {
        return step(Opcodes.IDIV);
    }

    public static @NotNull IMatchStep ldiv() {
        return step(Opcodes.LDIV);
    }

    public static @NotNull IMatchStep fdiv() {
        return step(Opcodes.FDIV);
    }

    public static @NotNull IMatchStep ddiv() {
        return step(Opcodes.DDIV);
    }

    public static @NotNull IMatchStep irem() {
        return step(Opcodes.IREM);
    }

    public static @NotNull IMatchStep lrem() {
        return step(Opcodes.LREM);
    }

    public static @NotNull IMatchStep frem() {
        return step(Opcodes.FREM);
    }

    public static @NotNull IMatchStep drem() {
        return step(Opcodes.DREM);
    }

    public static @NotNull IMatchStep ineg() {
        return step(Opcodes.INEG);
    }

    public static @NotNull IMatchStep lneg() {
        return step(Opcodes.LNEG);
    }

    public static @NotNull IMatchStep fneg() {
        return step(Opcodes.FNEG);
    }

    public static @NotNull IMatchStep dneg() {
        return step(Opcodes.DNEG);
    }

    public static @NotNull IMatchStep ishl() {
        return step(Opcodes.ISHL);
    }

    public static @NotNull IMatchStep lshl() {
        return step(Opcodes.LSHL);
    }

    public static @NotNull IMatchStep ishr() {
        return step(Opcodes.ISHR);
    }

    public static @NotNull IMatchStep lshr() {
        return step(Opcodes.LSHR);
    }

    public static @NotNull IMatchStep iushr() {
        return step(Opcodes.IUSHR);
    }

    public static @NotNull IMatchStep lushr() {
        return step(Opcodes.LUSHR);
    }

    public static @NotNull IMatchStep iand() {
        return step(Opcodes.IAND);
    }

    public static @NotNull IMatchStep land() {
        return step(Opcodes.LAND);
    }

    public static @NotNull IMatchStep ior() {
        return step(Opcodes.IOR);
    }

    public static @NotNull IMatchStep lor() {
        return step(Opcodes.LOR);
    }

    public static @NotNull IMatchStep ixor() {
        return step(Opcodes.IXOR);
    }

    public static @NotNull IMatchStep lxor() {
        return step(Opcodes.LXOR);
    }

    public static @NotNull IMatchStep i2l() {
        return step(Opcodes.I2L);
    }

    public static @NotNull IMatchStep i2f() {
        return step(Opcodes.I2F);
    }

    public static @NotNull IMatchStep i2d() {
        return step(Opcodes.I2D);
    }

    public static @NotNull IMatchStep l2i() {
        return step(Opcodes.L2I);
    }

    public static @NotNull IMatchStep l2f() {
        return step(Opcodes.L2F);
    }

    public static @NotNull IMatchStep l2d() {
        return step(Opcodes.L2D);
    }

    public static @NotNull IMatchStep f2i() {
        return step(Opcodes.F2I);
    }

    public static @NotNull IMatchStep f2l() {
        return step(Opcodes.F2L);
    }

    public static @NotNull IMatchStep f2d() {
        return step(Opcodes.F2D);
    }

    public static @NotNull IMatchStep d2i() {
        return step(Opcodes.D2I);
    }

    public static @NotNull IMatchStep d2l() {
        return step(Opcodes.D2L);
    }

    public static @NotNull IMatchStep d2f() {
        return step(Opcodes.D2F);
    }

    public static @NotNull IMatchStep i2b() {
        return step(Opcodes.I2B);
    }

    public static @NotNull IMatchStep i2c() {
        return step(Opcodes.I2C);
    }

    public static @NotNull IMatchStep i2s() {
        return step(Opcodes.I2S);
    }

    public static @NotNull IMatchStep lcmp() {
        return step(Opcodes.LCMP);
    }

    public static @NotNull IMatchStep fcmpl() {
        return step(Opcodes.FCMPL);
    }

    public static @NotNull IMatchStep fcmpg() {
        return step(Opcodes.FCMPG);
    }

    public static @NotNull IMatchStep dcmpl() {
        return step(Opcodes.DCMPL);
    }

    public static @NotNull IMatchStep dcmpg() {
        return step(Opcodes.DCMPG);
    }

    public static @NotNull IMatchStep ireturn() {
        return step(Opcodes.IRETURN);
    }

    public static @NotNull IMatchStep lreturn() {
        return step(Opcodes.LRETURN);
    }

    public static @NotNull IMatchStep freturn() {
        return step(Opcodes.FRETURN);
    }

    public static @NotNull IMatchStep dreturn() {
        return step(Opcodes.DRETURN);
    }

    public static @NotNull IMatchStep areturn() {
        return step(Opcodes.ARETURN);
    }

    public static @NotNull IMatchStep returnn() {
        return step(Opcodes.RETURN);
    }

    public static @NotNull IMatchStep arraylength() {
        return step(Opcodes.ARRAYLENGTH);
    }

    public static @NotNull IMatchStep athrow() {
        return step(Opcodes.ATHROW);
    }

    // IntInsnNode
    public static @NotNull IMatchStep bipush() {
        return step(Opcodes.BIPUSH);
    }

    public static @NotNull IMatchStep sipush() {
        return step(Opcodes.SIPUSH);
    }

    // VarInsnNode
    public static @NotNull IMatchStep iload() {
        return step(Opcodes.ILOAD);
    }

    public static @NotNull IMatchStep lload() {
        return step(Opcodes.LLOAD);
    }

    public static @NotNull IMatchStep fload() {
        return step(Opcodes.FLOAD);
    }

    public static @NotNull IMatchStep dload() {
        return step(Opcodes.DLOAD);
    }

    public static @NotNull IMatchStep aload() {
        return step(Opcodes.ALOAD);
    }

    public static @NotNull IMatchStep iaload() {
        return step(Opcodes.IALOAD);
    }

    public static @NotNull IMatchStep laload() {
        return step(Opcodes.LALOAD);
    }

    public static @NotNull IMatchStep faload() {
        return step(Opcodes.FALOAD);
    }

    public static @NotNull IMatchStep daload() {
        return step(Opcodes.DALOAD);
    }

    public static @NotNull IMatchStep aaload() {
        return step(Opcodes.AALOAD);
    }

    public static @NotNull IMatchStep baload() {
        return step(Opcodes.BALOAD);
    }

    public static @NotNull IMatchStep caload() {
        return step(Opcodes.CALOAD);
    }

    public static @NotNull IMatchStep saload() {
        return step(Opcodes.SALOAD);
    }

    public static @NotNull IMatchStep istore() {
        return step(Opcodes.ISTORE);
    }

    public static @NotNull IMatchStep lstore() {
        return step(Opcodes.LSTORE);
    }

    public static @NotNull IMatchStep fstore() {
        return step(Opcodes.FSTORE);
    }

    public static @NotNull IMatchStep dstore() {
        return step(Opcodes.DSTORE);
    }

    public static @NotNull IMatchStep astore() {
        return step(Opcodes.ASTORE);
    }

    public static @NotNull IMatchStep iastore() {
        return step(Opcodes.IASTORE);
    }

    public static @NotNull IMatchStep lastore() {
        return step(Opcodes.LASTORE);
    }

    public static @NotNull IMatchStep fastore() {
        return step(Opcodes.FASTORE);
    }

    public static @NotNull IMatchStep dastore() {
        return step(Opcodes.DASTORE);
    }

    public static @NotNull IMatchStep aastore() {
        return step(Opcodes.AASTORE);
    }

    public static @NotNull IMatchStep bastore() {
        return step(Opcodes.BASTORE);
    }

    public static @NotNull IMatchStep castore() {
        return step(Opcodes.CASTORE);
    }

    public static @NotNull IMatchStep sastore() {
        return step(Opcodes.SASTORE);
    }

    // control flow
    public static @NotNull IMatchStep gotoo() {
        return step(Opcodes.GOTO);
    }

    public static @NotNull IMatchStep ifeq() {
        return step(Opcodes.IFEQ);
    }

    public static @NotNull IMatchStep ifne() {
        return step(Opcodes.IFNE);
    }

    public static @NotNull IMatchStep ldc() {
        return step(Opcodes.LDC);
    }

    private static LoadStep.LoadIntStep LoadIntStep;

    public static @NotNull IMatchStep loadInt() {
        if (LoadIntStep == null) {
            LoadIntStep = new LoadStep.LoadIntStep();
        }
        return LoadIntStep;
    }

    private static LoadStep.LoadFloatStep LoadFloatStep;

    public static @NotNull IMatchStep loadFloat() {
        if (LoadFloatStep == null) {
            LoadFloatStep = new LoadStep.LoadFloatStep();
        }
        return LoadFloatStep;
    }

    private static LoadStep.LoadDoubleStep LoadDoubleStep;

    public static @NotNull IMatchStep loadDouble() {
        if (LoadDoubleStep == null) {
            LoadDoubleStep = new LoadStep.LoadDoubleStep();
        }
        return LoadDoubleStep;
    }

    private static LoadStep.LoadLongStep LoadLongStep;

    public static @NotNull IMatchStep loadLong() {
        if (LoadLongStep == null) {
            LoadLongStep = new LoadStep.LoadLongStep();
        }
        return LoadLongStep;
    }

    private static LoadStep.LoadStrStep LoadStrStep;

    public static @NotNull IMatchStep loadStr() {
        if (LoadStrStep == null) {
            LoadStrStep = new LoadStep.LoadStrStep();
        }
        return LoadStrStep;
    }

    public static @NotNull IMatchStep aload(int slot) {
        return new SimpleStep(Opcodes.ALOAD, insnNode -> {
            if (insnNode instanceof VarInsnNode varInsnNode) {
                return varInsnNode.var == slot;
            }
            return false;
        });
    }

    // 两个条件的if判断
    public static @NotNull IMatchStep if2slot() {
        return new AnyStep((insn) -> {
            return insn.getOpcode() == Opcodes.IF_ICMPGE
                    || insn.getOpcode() == Opcodes.IF_ICMPGT
                    || insn.getOpcode() == Opcodes.IF_ICMPLE
                    || insn.getOpcode() == Opcodes.IF_ICMPLT
                    || insn.getOpcode() == Opcodes.IF_ICMPNE
                    || insn.getOpcode() == Opcodes.IF_ICMPEQ;
        });
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull IMatchStep invokestatic(@NotNull String owner, @NotNull String name, @NotNull String desc) {
        return new MethodStep.SimpleMethodStep(Opcodes.INVOKESTATIC, owner, name, desc);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull IMatchStep invokespecial(@NotNull String owner, @NotNull String name, @NotNull String desc) {
        return new MethodStep.SimpleMethodStep(Opcodes.INVOKESPECIAL, owner, name, desc);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull IMatchStep invokevirtual(@NotNull String owner, @NotNull String name, @NotNull String desc) {
        return new MethodStep.SimpleMethodStep(Opcodes.INVOKEVIRTUAL, owner, name, desc);
    }
}

