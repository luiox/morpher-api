package com.github.luiox.passes.optimize;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@PassInfo(name = "UnusedLabelRemover", description = "清除没有使用到的LabelNode")
public class UnusedLabelRemover extends MethodPass {

    private static final Logger logger = LoggerFactory.getLogger(UnusedLabelRemover.class);
    int count = 0;

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        Map<LabelNode, Boolean> labelToUsed = new HashMap<>();
        // 扫描指令，搜集表，和确定引用情况
        for (var insn : methodNode.instructions) {
            if (insn instanceof JumpInsnNode jumpInsnNode) {
                labelToUsed.put(jumpInsnNode.label, true);
            } else if (insn instanceof LineNumberNode lineNumberNode) {
                labelToUsed.put(lineNumberNode.start, true);
            } else if (insn instanceof LookupSwitchInsnNode lookupSwitchInsnNode) {
                for (LabelNode labelNode : lookupSwitchInsnNode.labels) {
                    labelToUsed.put(labelNode, true);
                }
                labelToUsed.put(lookupSwitchInsnNode.dflt, true);
            } else if (insn instanceof TableSwitchInsnNode tableSwitchInsnNode) {
                for (LabelNode labelNode : tableSwitchInsnNode.labels) {
                    labelToUsed.put(labelNode, true);
                }
                labelToUsed.put(tableSwitchInsnNode.dflt, true);
            } else if (insn instanceof LabelNode labelNode) {
                // 先检查一下有没有
                if (labelToUsed.containsKey(labelNode)) return;
                // 没有就标记为未使用
                labelToUsed.put(labelNode, false);
            }
        }
        // 扫描try catch的label使用情况
        for (var tryCatchBlockNode : methodNode.tryCatchBlocks) {
            labelToUsed.put(tryCatchBlockNode.start, true);
            labelToUsed.put(tryCatchBlockNode.end, true);
            labelToUsed.put(tryCatchBlockNode.handler, true);
        }

        // 拿指令的起始和结束
        var start = methodNode.instructions.getFirst();
        var end = methodNode.instructions.getLast();
        // 从前往后和从后往前，第一个label需要保留
        while (start != null) {
            if (start instanceof LabelNode labelNode) {
                labelToUsed.put(labelNode, true);
                break;
            }
            start = start.getNext();
        }
        while (end != null) {
            if (end instanceof LabelNode labelNode) {
                labelToUsed.put(labelNode, true);
            }
            end = end.getPrevious();
        }

        // 移除所有为false的label
        for (var entry : labelToUsed.entrySet()) {
            if (!entry.getValue()) {
                methodNode.instructions.remove(entry.getKey());
            }
        }
        // 记录移除的数量
        count += labelToUsed.size();
    }

    @Override
    public void doFinalization(@NotNull IPassContext context) {
        logger.info("[UnusedLabelRemover] Removed {} unused labels", count);
    }
}
