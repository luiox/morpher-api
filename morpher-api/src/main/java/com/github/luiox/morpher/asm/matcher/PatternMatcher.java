package com.github.luiox.morpher.asm.matcher;

import com.github.luiox.morpher.asm.insn.InsnBuilder;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指令模式匹配器
 * 优化点：采用Trie树进行模式匹配
 */
public class PatternMatcher {

    private final List<MatchRule> rules = new ArrayList<>();

    public @NotNull PatternMatcher addRule(@NotNull MatchRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * 根据规则匹配指令，返回匹配结果
     *
     * @param original 原始指令
     * @return 匹配结果，以规则实例->匹配成功的列表
     */
    public Map<MatchRule, List<MatchResult>> match(@NotNull InsnList original) {
        Map<MatchRule, List<MatchResult>> result = new HashMap<>();
        MatchContext context = new MatchContext(original, 0, 0);
        // 匹配
        for (int i = 0; i < original.size(); ) {
            boolean matched = false;
            for (MatchRule rule : rules) {
                // 设置起始位置
                context.startIdx = i;
                // 尝试不同的rule
                if (rule.tryMatch(original, i, context)) {
                    // 设置起始和结束
                    context.setArea(i, i + rule.getMatchedSize());
                    // 添加到结果中
                    result.computeIfAbsent(rule, _ -> new ArrayList<>());
                    result.get(rule).add(new MatchResult(i, i + rule.getMatchedSize()));
                    // 获取匹配的字节数，跳过这些
                    i += rule.getMatchedSize();
                    matched = true;
                    break;
                }
            }
            if (matched) {
                continue;
            }
            i++;
        }
        return result;
    }

    /**
     * 根据规则匹配指令，然后根据不同的匹配规则对应的修改策略，自动应用修改策略
     *
     * @param original 原始指令
     * @return 修改后的指令
     */
    public @NotNull InsnList apply(@NotNull InsnList original) {
        List<AbstractInsnNode> deleteList = new ArrayList<>();
        MatchContext context = new MatchContext(original, 0, 0);
        InsnBuilder builder = context.builder;
        for (int i = 0; i < original.size(); ) {
            boolean matched = false;
            for (MatchRule rule : rules) {
                // 设置起始位置
                context.startIdx = i;
                // 尝试不同的rule
                if (rule.tryMatch(original, i, context)) {
                    // 设置起始和结束
                    context.setArea(i, i + rule.getMatchedSize());
                    // 匹配以后利用strategy去做操作
                    rule.getStrategy().apply(context);
                    // 标记这些节点需要被移除，以免重用的时候影响新的InsnList
                    for (int j = 0; j < rule.getMatchedSize(); j++) {
                        deleteList.add(original.get(i + j));
                    }

                    i += rule.getMatchedSize();
                    matched = true;

                    break;
                }
            }
            if (matched) {
                continue;
            }
            builder.addInsnNode(original.get(i));
            i++;
        }
        // 移除删除的节点，主要是为了断开连接，要不然会影响Builder
        for (AbstractInsnNode ain : deleteList) {
            original.remove(ain);
        }
        return builder.getInsnList();
    }

    /**
     * 从多个匹配器中创建一个新的PatternMatcher
     *
     * @param matchers 多个匹配器
     * @return 一个新的PatternMatcher实例
     */
    public static @NotNull PatternMatcher from(PatternMatcher @NotNull ... matchers) {
        PatternMatcher result = new PatternMatcher();

        for (PatternMatcher matcher : matchers) {
            result.rules.addAll(matcher.rules);
        }

        return result;
    }
}
