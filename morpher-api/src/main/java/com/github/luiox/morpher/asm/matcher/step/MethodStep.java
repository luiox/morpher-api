package com.github.luiox.morpher.asm.matcher.step;

import com.github.luiox.morpher.asm.matcher.MatchContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.function.Predicate;

public class MethodStep {
    public static class SimpleMethodStep implements IMatchStep {
        private final int opcode;
        private final String owner;
        private final String name;
        private final String desc;
        private final Predicate<AbstractInsnNode> additional;

        public SimpleMethodStep(@NotNull String owner, @NotNull String name, @NotNull String desc) {
            this(-1, owner, name, desc, _ -> true);
        }

        public SimpleMethodStep(int opcode, @NotNull String owner, @NotNull String name, @NotNull String desc) {
            this(opcode, owner, name, desc, _ -> true);
        }

        public SimpleMethodStep(int opcode, @NotNull String owner, @NotNull String name, @NotNull String desc, @NotNull Predicate<AbstractInsnNode> additional) {
            this.opcode = opcode;
            this.owner = owner;
            this.name = name;
            this.desc = desc;
            this.additional = additional;
        }

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            if (now instanceof MethodInsnNode insnNode) {
                if (opcode != -1) {
                    return insnNode.getOpcode() == opcode && insnNode.owner.equals(owner) && insnNode.name.equals(name) && insnNode.desc.equals(desc) && additional.test(now);
                }
                return insnNode.owner.equals(owner) && insnNode.name.equals(name) && insnNode.desc.equals(desc) && additional.test(now);
            }
            return false;
        }
    }

    public static class IndyMethodStep implements IMatchStep {

        @Override
        public boolean tryMatch(@NotNull AbstractInsnNode now, @NotNull MatchContext ctx) {
            return false;
        }
    }
}
