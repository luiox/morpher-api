package com.github.luiox.morpher.jar;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class SimpleJarWriter implements IJarCachesWriter {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJarReader.class);

    private final String path;

    public SimpleJarWriter(@NotNull String path) {
        this.path = path;
    }

    @Override
    public void write(@NotNull IJarCaches caches) {
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(path))) {
            // 添加content中的内容
            caches.entries(JarCachesEntryType.All).forEach(entry -> {
                JarEntry jarEntry = new JarEntry(entry.path);
                try {
                    jos.putNextEntry(jarEntry);
                    byte[] content = entry.content;
                    // 如果是null或者是byte[0]
                    if (content == null || content.length == 0) {
                        // 创建一个空的
                        jos.closeEntry();
                    } else {
                        // 写入
                        jos.write(content);
                        jos.closeEntry();
                    }

                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            });

            // 写出manifest
            var ret = caches.getManifest();
            if (!ret.isErr()) {
                try {
                    Manifest manifest = ret.unwrap();
                    var byteArrayOutputStream = new ByteArrayOutputStream();
                    manifest.write(byteArrayOutputStream);
                    jos.putNextEntry(new JarEntry(JarUtil.ManifestFileName));
                    jos.write(byteArrayOutputStream.toByteArray());
                    jos.closeEntry();
                } catch (Exception e) {
                    logger.info("setManifest fail! e: {}", e.getMessage());
                    logger.error(e.getMessage());
                }
            }

        } catch (IOException e) {
            logger.info("Failed to write jar file, e: {}", e.getMessage());
            logger.error(e.getMessage());
        }
    }
}
