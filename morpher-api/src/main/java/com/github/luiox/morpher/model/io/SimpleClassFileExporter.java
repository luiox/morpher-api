package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 简单的class文件导出器。
 * <p>
 * 实现IResourceExporter接口，将资源容器中的ClassResource导出为class文件。
 */
public class SimpleClassFileExporter implements IResourceExporter {
    /**
     * 资源在容器中的路径
     */
    String uri;
    /**
     * class文件输出路径
     */
    String path;

    /**
     * 仅指定输出路径，自动导出唯一的ClassResource。
     *
     * @param path class文件输出路径
     */
    public SimpleClassFileExporter(String path) {
        this(null, path);
    }

    /**
     * 指定资源路径和输出路径。
     *
     * @param uri  资源在容器中的路径
     * @param path class文件输出路径
     */
    public SimpleClassFileExporter(String uri, String path) {
        this.uri = uri;
        this.path = path;
    }

    /**
     * 导出ClassResource为class文件。
     *
     * @param container 资源容器
     * @throws Exception 导出异常
     */
    @Override
    public void exportResource(@NotNull IResourceContainer container) throws Exception {
        if (uri == null) {
            // 如果是null，看看是不是就一个文件
            if (container.size() != 1) {
                throw new Exception("uri is null and container size is not 1");
            }
            // 拿到唯一的entry
            var entry = container.entrySet().iterator().next();
            // 检查是否是ClassResource
            if (entry.getValue() instanceof ClassResource classResource) {
                // 写出
                Files.write(Path.of(path), classResource.get());
            } else {
                throw new Exception("export class, but the only resource is not ClassResource");
            }
        } else {
            // 如果不是null，那么就检查uri是否在container中
            if (!container.hasResource(uri)) {
                throw new Exception("uri is not in container");
            }
            // 拿到对应的resource
            var resource = container.get(uri);
            // 检查是否是ClassResource
            if (resource instanceof ClassResource classResource) {
                // 写出
                Files.write(Path.of(path), classResource.get());
            } else {
                throw new Exception("export class, but the resource is not ClassResource");
            }
        }
    }
}
