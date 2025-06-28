package com.github.luiox.example;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

@PassInfo(name = "Sample001Pass2", description = "处理冗余跳转的pass")
public class Sample001Pass2 extends MethodPass {

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        if (methodNode.instructions == null || methodNode.instructions.size() == 0) {
            return;
        }

        AbstractInsnNode[] insns = methodNode.instructions.toArray();
        for (int i = 0; i < insns.length; i++) {
            var insn  = insns[i];
            if (insn instanceof JumpInsnNode jumpInsnNode && insn.getOpcode() == Opcodes.GOTO) {
                // 检查一下i+1是否在范围内
                if (i + 1 >= insns.length) {
                    break;
                }
                var next = insns[i + 1];
                // 检查一下跳转的目标是否是下一个指令
                if(next instanceof LabelNode labelNode && labelNode == jumpInsnNode.label){
                    // 移除这个跳转
                    methodNode.instructions.remove(insn);
                }
            }
        }
    }
}