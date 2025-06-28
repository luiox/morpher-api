package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.IJarWriter;
import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.jar.Manifest;

/**
 * Jar包导出器。
 * <p>
 * 实现IResourceExporter接口，将资源容器中的资源写入到Jar包。
 */
public class JarFileExporter implements IResourceExporter {
    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(JarFileExporter.class);
    /** Jar写入器 */
    IJarWriter writer;

    /**
     * 构造方法。
     * @param writer Jar写入器
     */
    public JarFileExporter(@NotNull IJarWriter writer) {
        this.writer = writer;
    }

    /**
     * 将单个资源写入Jar包。
     * @param writer Jar条目写入器
     * @param resource 资源对象
     */
    public void writeResource(IJarWriter.IEntryWriter writer, IResource resource) {
        if (resource instanceof ClassResource classResource) {
            writer.writeEntry(classResource.getLocation(), classResource.get());
        } else if (resource instanceof ManifestResource manifestResource) {
            try {
                Manifest manifest = manifestResource.get();
                var byteArrayOutputStream = new ByteArrayOutputStream();
                manifest.write(byteArrayOutputStream);
                writer.writeEntry(JarUtil.ManifestFileName, byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                logger.error("Failed to write manifest, {}", e.getMessage());
            }
        } else if (resource instanceof UnknownResource unknownResource) {
            writer.writeEntry(unknownResource.getLocation(), unknownResource.get());
        } else {
            throw new IllegalArgumentException("Unknown resource type: " + resource.getClass().getName());
        }
    }

    /**
     * 导出资源容器中的所有资源到Jar包。
     * @param container 资源容器
     * @throws Exception 导出异常
     */
    @Override
    public void exportResource(@NotNull IResourceContainer container) throws Exception {
        writer.write(writer -> {
            for (var entry : container.entrySet()) {
                writeResource(writer, entry.getValue());
            }
        });
    }
}
