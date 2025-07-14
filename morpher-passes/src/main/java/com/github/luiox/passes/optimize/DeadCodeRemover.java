package com.github.luiox.passes.optimize;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PassInfo(name = "DeadCodeRemover", description = "移除无用代码")
public class DeadCodeRemover extends MethodPass {

    private static final Logger logger = LoggerFactory.getLogger(DeadCodeRemover.class);
    int count = 0;

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        if (methodNode.instructions == null || methodNode.instructions.size() == 0) {
            return;
        }

        // 栈帧分析
        try {
            Analyzer<BasicValue> analyzer = new Analyzer<>(new BasicInterpreter());
            Frame<BasicValue>[] frames = analyzer.analyze(context.currentClass().name, methodNode);

            // 删除无论如何不可能执行到的死代码
            AbstractInsnNode[] insnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < frames.length; i++) {
                if (frames[i] == null
                        && !(insnNodes[i] instanceof LabelNode)
                        && !(insnNodes[i] instanceof LineNumberNode)
                        && !(insnNodes[i] instanceof FrameNode)) {
                    methodNode.instructions.remove(insnNodes[i]);
                    count++;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to analyze method: {} in class: {}", methodNode.name, context.currentClass().name, e);
        }
    }

    @Override
    public void doFinalization(@NotNull IPassContext context) {
        logger.info("[DeadCodeRemover] remove {} dead code instructions", count);
    }
}
