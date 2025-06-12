package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class StackOpStep implements IMatchStep {

    private final Boolean isLoad;
    private final boolean twoSlotSize;

    /**
     * @param isLoad true: load, false: store, null: both
     */
    public StackOpStep(Boolean isLoad, boolean twoSlotSize) {
        this.isLoad = isLoad;
        this.twoSlotSize = twoSlotSize;
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
        boolean isStackInsn = now instanceof VarInsnNode;
        if (!isStackInsn) {
            return false;
        }
        int opcode = now.getOpcode();
        if (isLoad == null) {
            switch (opcode) {
                case DLOAD:
                case LLOAD:
                case DSTORE:
                case LSTORE:
                    return twoSlotSize;
                default:
                    return !twoSlotSize;
            }
        }
        if (isLoad) {
            if (opcode == DLOAD || opcode == LLOAD) {
                return twoSlotSize;
            } else if (opcode <= ALOAD) {
                return !twoSlotSize;
            }
        } else {
            if (opcode == DSTORE || opcode == LSTORE) {
                return twoSlotSize;
            } else if (opcode > ALOAD) {
                return !twoSlotSize;
            }
        }
        return false;
    }

//    @Override
//    public String toString() {
//        return "StackStep{" +
//                "captured=" + Utils.prettyprint(this.getCaptured()) +
//                ", isLoad=" + this.isLoad +
//                ", twoSlotSize=" + this.twoSlotSize +
//                '}';
//    }

}
