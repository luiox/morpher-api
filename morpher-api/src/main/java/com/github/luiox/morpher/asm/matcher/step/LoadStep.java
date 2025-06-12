package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.insn.InsnUtil;
import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class LoadStep {
    public static class LoadIntStep implements IMatchStep {

        private final Integer expectVal;

        public LoadIntStep() {
            this(null);
        }

        public LoadIntStep(Integer expectVal) {
            this.expectVal = expectVal;
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            if (expectVal == null) {
                return InsnUtil.isInteger(now);
            }
            return InsnUtil.isInteger(now) && InsnUtil.getIntValue(now) == expectVal;
        }
    }

    public static class LoadLongStep implements IMatchStep {
        public LoadLongStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return InsnUtil.isLong(now);
        }
    }

    public static class LoadFloatStep implements IMatchStep {
        public LoadFloatStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return InsnUtil.isFloat(now);
        }
    }

    public static class LoadDoubleStep implements IMatchStep {
        public LoadDoubleStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return InsnUtil.isDouble(now);
        }
    }

    public static class LoadStrStep implements IMatchStep {
        public LoadStrStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return now instanceof LdcInsnNode && ((LdcInsnNode) now).cst instanceof String;
        }
    }

    public static class Load1SlotStep implements IMatchStep {
        public Load1SlotStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return InsnUtil.isInteger(now) || InsnUtil.isFloat(now);
        }
    }

    public static class Load2SlotStep implements IMatchStep {
        public Load2SlotStep() {
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return InsnUtil.isLong(now) || InsnUtil.isDouble(now);
        }
    }

    public static class NoSideEffectLoad1SlotStep implements IMatchStep {
        private final boolean alsoMatchStackLoad;

        public NoSideEffectLoad1SlotStep(boolean alsoMatchStackLoad) {
            this.alsoMatchStackLoad = alsoMatchStackLoad;
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return switch (now.getOpcode()) {
                case ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, FCONST_0,
                     FCONST_1,
                     FCONST_2, BIPUSH, SIPUSH -> true;
                case ILOAD, FLOAD, ALOAD -> alsoMatchStackLoad;
                case LDC -> {
                    Object obj = ((LdcInsnNode) now).cst;
                    yield !(obj instanceof Double || obj instanceof Long || obj instanceof Type);
                }
                default -> false;
            };
        }

    }

    public static class NoSideEffectLoad2SlotStep implements IMatchStep {
        private final boolean alsoMatchStackLoad;

        public NoSideEffectLoad2SlotStep(boolean alsoMatchStackLoad) {
            this.alsoMatchStackLoad = alsoMatchStackLoad;
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return switch (now.getOpcode()) {
                case LCONST_0, LCONST_1, DCONST_0, DCONST_1 -> true;
                case LLOAD, DLOAD -> alsoMatchStackLoad;
                case LDC -> {
                    Object obj = ((LdcInsnNode) now).cst;
                    yield obj instanceof Double || obj instanceof Long;
                }
                default -> false;
            };
        }
    }
}
