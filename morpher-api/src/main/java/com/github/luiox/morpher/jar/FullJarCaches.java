package com.github.luiox.morpher.jar;

import com.github.luiox.morpher.util.type.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.stream.Stream;

public class FullJarCaches implements IJarCaches {
    // path -> byte[]
    private Map<String, byte[]> classByteMap = new HashMap<>();
    // path -> byte[]
    private final Map<String, byte[]> resources = new HashMap<>();
    // 单独存Manifest
    private Manifest manifest = null;

    @Override
    public void addEntry(@NotNull JarCachesEntry entry) {
        if (entry.type == JarCachesEntryType.Class) {
            classByteMap.put(entry.path, entry.content);
        } else {
            resources.put(entry.path, entry.content);
        }
    }

    @Override
    public void removeEntry(@NotNull String path) {
        if (path.endsWith(".class")) {
            classByteMap.remove(path);
        } else {
            resources.remove(path);
        }
    }

    @Override
    public @NotNull Result<byte[], String> getEntry(@NotNull String path, @NotNull JarCachesEntryType type) {
        if (type == JarCachesEntryType.Class) {
            if (classByteMap.containsKey(path)) {
                return Result.Ok(classByteMap.get(path));
            } else {
                return Result.Err("Class not found: " + path);
            }
        } else {
            if (resources.containsKey(path)) {
                return Result.Ok(resources.get(path));
            } else {
                return Result.Err("Resource not found: " + path);
            }
        }
    }

    @Override
    public void addEntries(@NotNull Collection<JarCachesEntry> entries) {
        for (var entry : entries) {
            addEntry(entry);
        }
    }

    @Override
    public void removeEntries(@NotNull Collection<String> paths) {
        for (var path : paths) {
            removeEntry(path);
        }
    }

    @Override
    public Stream<JarCachesEntry> entries(@NotNull JarCachesEntryType type) {
        Stream<Map.Entry<String, byte[]>> classStream = type == JarCachesEntryType.Class || type == JarCachesEntryType.All
                ? classByteMap.entrySet().stream()
                : Stream.empty();

        Stream<Map.Entry<String, byte[]>> resourceStream = type == JarCachesEntryType.Resource || type == JarCachesEntryType.All
                ? resources.entrySet().stream()
                : Stream.empty();

        Stream<Map.Entry<String, byte[]>> combinedStream = Stream.concat(classStream, resourceStream);

        return combinedStream.map(entry -> {
            JarCachesEntry t = new JarCachesEntry();
            t.content = entry.getValue();
            t.path = entry.getKey();
            t.type = type;
            return t;
        });
    }

    @Override
    public @NotNull Result<Manifest, String> getManifest() {
        if (manifest == null) {
            return Result.Err("Manifest is null");
        }
        return Result.Ok(manifest);
    }

    @Override
    public void setManifest(@Nullable Manifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public void read(@NotNull IJarCachesReader reader) {
        reader.read(this);
    }

    @Override
    public void write(@NotNull IJarCachesWriter reader) {
        reader.write(this);
    }
}
