package com.github.luiox.morpher.jar;

import com.github.luiox.morpher.util.LogUtil;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SimpleJarReader implements IJarCachesReader{

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
                    }
                    else{
                        cachesEntry.type = JarCachesEntryType.Resource;
                    }
                    caches.addEntry(cachesEntry);
                }
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }
}
