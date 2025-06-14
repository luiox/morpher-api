package com.github.luiox.morpher.jar;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SimpleJarReader implements IJarCachesReader {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJarReader.class);

    private final String path;

    public SimpleJarReader(@NotNull String path) {
        this.path = path;
    }

    @Override
    public void read(@NotNull IJarCaches caches) {
        try (var jarFile = new JarFile(path)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            JarCachesEntry cachesEntry = new JarCachesEntry();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                InputStream entryInputStream = jarFile.getInputStream(entry);
                byte[] entryData = entryInputStream.readAllBytes();
                String entryName = entry.getName();
                if (entryName.equals(JarUtil.ManifestFileName)) {
                    caches.setManifest(jarFile.getManifest());
                } else {
                    cachesEntry.path = entryName;
                    cachesEntry.content = entryData;
                    // 判断一下类型
                    if (JarUtil.isClassFile(entryName)) {
                        cachesEntry.type = JarCachesEntryType.Class;
                    } else {
                        cachesEntry.type = JarCachesEntryType.Resource;
                    }
                    caches.addEntry(cachesEntry);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
