package com.github.luiox.morpher.jar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface IJarWriter {

    /**
     * 写出完整jar
     *
     * @param handler 需要一个handler这个handler会被反复调用，直到返回true表示写入结束，handler的参数是写入一个条目使用的方法
     */
    void write(@NotNull Consumer<IEntryWriter> handler);

    interface IEntryWriter {
        /**
         * 写入一个条目
         *
         * @param entryName 条目的路径
         * @param content   条目的内容，如果为null，则写入一个空条目
         */
        void writeEntry(@NotNull String entryName, byte @Nullable [] content);
    }
}
