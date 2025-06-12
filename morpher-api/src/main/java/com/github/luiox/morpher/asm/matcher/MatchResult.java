package com.github.luiox.morpher.asm.matcher;

/**
 * 表示匹配的结果
 * start和end表示匹配的起始和结束位置，相当于区间[start, end)被匹配上了
 */
public class MatchResult {
    public int start;
    public int end;

    public MatchResult(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
