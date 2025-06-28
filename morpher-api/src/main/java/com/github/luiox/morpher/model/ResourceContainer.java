package com.github.luiox.morpher.model;

import com.github.luiox.morpher.model.io.IResourceExporter;
import com.github.luiox.morpher.model.io.IResourceImporter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * 资源容器的默认实现。
 * <p>
 * 基于HashMap实现，支持资源的添加、查找、导入、导出等操作。
 */
public class ResourceContainer extends HashMap<String, IResource> implements IResourceContainer {
    /**
     * 添加资源到容器。
     * @param uri 资源URI
     * @param resource 资源对象
     */
    @Override
    public void addResource(String uri, @NotNull IResource resource) {
        put(uri, resource);
    }

    /**
     * 判断容器中是否存在指定URI的资源。
     * @param uri 资源URI
     * @return 是否存在
     */
    @Override
    public boolean hasResource(String uri) {
        return containsKey(uri);
    }

    /**
     * 使用指定导入器导入资源。
     * @param importer 资源导入器
     * @throws Exception 导入异常
     */
    @Override
    public void importResource(@NotNull IResourceImporter importer) throws Exception {
        importer.importResource(this);
    }

    /**
     * 使用指定导出器导出资源。
     * @param exporter 资源导出器
     * @throws Exception 导出异常
     */
    @Override
    public void exportResource(@NotNull IResourceExporter exporter) throws Exception {
        exporter.exportResource(this);
    }
}
