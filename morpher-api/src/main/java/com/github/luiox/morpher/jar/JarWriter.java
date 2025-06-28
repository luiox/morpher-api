package com.github.luiox.morpher.jar;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarWriter implements IJarWriter {
    private static final Logger logger = LoggerFactory.getLogger(JarWriter.class);

    String path;
    JarOutputStream jos;

    public JarWriter(String path) {
        this.path = path;
    }

    public void writeEntry(String entryName, byte[] content) {
        JarEntry jarEntry = new JarEntry(entryName);
        try {
            jos.putNextEntry(jarEntry);
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
            e.printStackTrace();
        }
    }

    @Override
    public void write(@NotNull Consumer<IEntryWriter> handler) {
        try (JarOutputStream t = new JarOutputStream(new FileOutputStream(path))) {
            jos = t;

            handler.accept(this::writeEntry);

        } catch (IOException e) {
            logger.info("Failed to write jar file, e: {}", e.getMessage());
            logger.error(e.getMessage());
        }
    }

}
