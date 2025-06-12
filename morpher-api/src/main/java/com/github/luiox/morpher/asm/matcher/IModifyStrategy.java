package com.github.luiox.morpher.asm.matcher;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IModifyStrategy {
    void apply(@NotNull MatchContext ctx);
}