package com.github.luiox.passes.optimize;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

@PassInfo(name = "NopRemover", description = "移除无用指令")
public class NopRemover extends MethodPass {
    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        if(methodNode.instructions ==null || methodNode.instructions.size()==0){
            return;
        }
        // 如果是nop则移除
        for (var it = methodNode.instructions.iterator(); it.hasNext(); ) {
            var insn = it.next();
            if (insn.getOpcode() == Opcodes.NOP) {
                it.remove();
            }
        }
    }
}
