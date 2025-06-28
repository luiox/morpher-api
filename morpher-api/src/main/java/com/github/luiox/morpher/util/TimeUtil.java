package com.github.luiox.morpher.util;

import org.jetbrains.annotations.NotNull;

public class TimeUtil {

    public static long runBlocking(@NotNull Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        return (System.nanoTime() - startTime) / 1_000_000;
    }

}
