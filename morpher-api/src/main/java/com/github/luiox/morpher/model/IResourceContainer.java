package com.github.luiox.morpher.model;

import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.io.IResourceExporter;
import com.github.luiox.morpher.model.io.IResourceImporter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 资源容器接口。
 * <p>
 * 用于统一管理和操作多种类型的资源，支持导入、导出、查找、流式处理等功能。
 */
public interface IResourceContainer extends Map<String, IResource> {
    /**
     * 添加资源到容器。
     *
     * @param uri      资源URI
     * @param resource 资源对象
     */
    void addResource(String uri, @NotNull IResource resource);

    /**
     * 判断容器中是否存在指定URI的资源。
     *
     * @param uri 资源URI
     * @return 是否存在
     */
    boolean hasResource(String uri);

    /**
     * 使用指定导入器导入资源。
     *
     * @param importer 资源导入器
     * @throws Exception 导入异常
     */
    void importResource(@NotNull IResourceImporter importer) throws Exception;

    /**
     * 使用指定导出器导出资源。
     *
     * @param exporter 资源导出器
     * @throws Exception 导出异常
     */
    void exportResource(@NotNull IResourceExporter exporter) throws Exception;

    /**
     * 获取所有ClassResource的流。
     *
     * @return ClassResource流
     */
    default Stream<ClassResource> classes() {
        return this.values().stream().filter(r -> r instanceof ClassResource).map(r -> (ClassResource) r);
    }

    /**
     * 获取所有满足条件的ClassResource流。
     *
     * @param keep 条件谓词，保留为条件为true的ClassResource
     * @return ClassResource流
     */
    default Stream<ClassResource> classes(Predicate<ClassResource> keep) {
        return this.classes().filter(keep);
    }

    /**
     * 获取Manifest资源。
     *
     * @return ManifestResource对象，未找到返回null
     */
    default ManifestResource manifest() {
        return (ManifestResource) this.get(JarUtil.ManifestFileName);
    }
}
