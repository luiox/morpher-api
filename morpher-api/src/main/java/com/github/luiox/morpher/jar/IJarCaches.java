package com.github.luiox.morpher.jar;

import com.github.luiox.morpher.util.type.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.jar.Manifest;
import java.util.stream.Stream;

/**
 * Jar缓存接口
 */
public interface IJarCaches {
    /**
     * 添加一个Entry，这个Entry对象在添加的时候，只会存里面的内容，不会存这个对象的引用，所以对象可以复用
     *
     * @param entry entry
     */
    void addEntry(@NotNull JarCachesEntry entry);

    /**
     * 移除一个Entry，只需要指定路径即可
     *
     * @param path path
     */
    void removeEntry(@NotNull String path);

    /**
     * 获取一个Entry，如果不存在，或者产生错误，返回错误消息字符串
     *
     * @param path path
     */
    @NotNull Result<byte[], String> getEntry(@NotNull String path, @NotNull JarCachesEntryType type);

    /**
     * 批量添加Entry
     *
     * @param entries entries
     */
    void addEntries(@NotNull Collection<JarCachesEntry> entries);

    /**
     * 批量移除Entry
     *
     * @param paths paths
     */
    void removeEntries(@NotNull Collection<String> paths);

    /**
     * 获取某个类型的的Entry，进行操作
     *
     * @param type 类型
     * @return entries
     */
    Stream<JarCachesEntry> entries(@NotNull JarCachesEntryType type);

    /**
     * 获取当前的Manifest
     *
     * @return manifest
     */
    @NotNull Result<Manifest, String> getManifest();

    /**
     * 设置Manifest，如果为null，那么后面不会写出
     *
     * @param manifest manifest
     */
    void setManifest(@Nullable Manifest manifest);

    /**
     * 读取jar到JarCaches
     *
     * @param reader reader
     */
    void read(@NotNull IJarCachesReader reader);

    /**
     * 写出JarCaches到文件
     *
     * @param writer writer
     */
    void write(@NotNull IJarCachesWriter writer);
}
