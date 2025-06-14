package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldStep implements IMatchStep {
    private static final Logger logger = LoggerFactory.getLogger(FieldStep.class);

    private final int opcode;
    private final String owner;
    private final String name;
    private final String desc;

    public FieldStep(int opcode, String owner, String name, String desc) {
        if (opcode > Opcodes.PUTFIELD || opcode < Opcodes.GETSTATIC) {
            logger.error("Invalid FieldInsn opcode {}", opcode);
        }
        this.opcode = opcode;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
        if (now.getOpcode() != opcode) {
            return false;
        }
        if (!(now instanceof FieldInsnNode)) {
            return false;
        }
        FieldInsnNode fin = (FieldInsnNode) now;
        if (owner != null && !fin.owner.equals(owner)) {
            return false;
        }
        if (name != null && !fin.name.equals(name)) {
            return false;
        }
        if (desc != null && !fin.desc.equals(desc)) {
            return false;
        }
        return true;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

//    @Override
//    public String toString() {
//        return "FieldInsnStep{" +
//                "captured=" + Utils.prettyprint(this.getCaptured()) +
//                ", opcode=" + this.opcode +
//                ", owner='" + this.owner + '\'' +
//                ", name='" + this.name + '\'' +
//                ", desc='" + this.desc + '\'' +
//                '}';
//    }

}
