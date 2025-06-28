package com.github.luiox.morpher.util;

import org.jetbrains.annotations.NotNull;

public class TimeUtil {

    /**
     * 运行一个同步任务，并返回其耗时（单位：毫秒）。
     *
     * @param task 要执行的任务
     * @return 任务执行耗时（毫秒）
     */
    public static long runBlocking(@NotNull Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        return (System.nanoTime() - startTime) / 1_000_000;
    }

}
