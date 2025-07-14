package com.github.luiox.passes.optimize;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PassInfo(name = "NopRemover", description = "移除nop指令")
public class NopRemover extends MethodPass {

    private static final Logger logger = LoggerFactory.getLogger(NopRemover.class);
    int count = 0;

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

    @Override
    public void doFinalization(@NotNull IPassContext context) {
        logger.info("[NopRemover] remove {} nop instructions", count);
    }
}
