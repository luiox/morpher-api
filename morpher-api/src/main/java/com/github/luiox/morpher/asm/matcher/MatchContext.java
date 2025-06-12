package com.github.luiox.morpher.asm.matcher;

import com.github.luiox.morpher.asm.insn.InsnBuilder;
import org.objectweb.asm.tree.InsnList;

import java.util.HashMap;
import java.util.Map;

/**
 * 匹配上下文，每个matcher的一次匹配只会同时存在一个MatchContext实例，因为上下文会复用
 */
public class MatchContext {
    public int startIdx;
    public int endIdx;
    public final Map<MatchRule, Object> extraData = new HashMap<>();

    public final InsnList original;
    public final InsnBuilder builder = new InsnBuilder();

    public MatchContext(InsnList insnList, int startIdx, int endIdx) {
        this.original = insnList;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    public void setArea(int startIdx, int endIdx) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }
}
