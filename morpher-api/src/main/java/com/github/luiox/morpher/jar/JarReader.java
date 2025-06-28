package com.github.luiox.morpher.jar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarReader implements IJarReader {
    public final String path;

    public JarReader(String path) {
        this.path = path;
    }

    @Override
    public void read(BiConsumer<String, InputStream> consumer) throws IOException {
        try (var jarFile = new JarFile(path)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                InputStream entryInputStream = jarFile.getInputStream(entry);
                String entryName = entry.getName();
                consumer.accept(entryName, new ByteArrayInputStream(entryInputStream.readAllBytes()));
            }
        } catch (Exception e) {
            throw new IOException("jar read error, " + e.getMessage());
        }
    }
}
