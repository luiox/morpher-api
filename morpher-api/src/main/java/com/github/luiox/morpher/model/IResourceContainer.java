package com.github.luiox.morpher.model;

import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.io.IResourceExporter;
import com.github.luiox.morpher.model.io.IResourceImporter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IResourceContainer extends Map<String, IResource> {
    void addResource(String uri, @NotNull IResource resource);

    boolean hasResource(String uri);

    void importResource(@NotNull IResourceImporter importer) throws Exception;

    void exportResource(@NotNull IResourceExporter exporter) throws Exception;

    /**
     * 把所有的ClassResource组成一个流
     *
     * @return 流
     */
    default Stream<ClassResource> classes() {
        return this.values().stream().filter(r -> r instanceof ClassResource).map(r -> (ClassResource) r);
    }

    /**
     * 把所有满足条件的ClassResource组成一个流
     *
     * @param keep 条件，返回true的保留进stream
     * @return 流
     */
    default Stream<ClassResource> classes(Predicate<ClassResource> keep) {
        return this.classes().filter(keep);
    }

    /**
     * 获取Manifest，如果找到，返回null
     *
     * @return Manifest
     */
    default ManifestResource manifest() {
        return (ManifestResource) this.get(JarUtil.ManifestFileName);
    }
}
