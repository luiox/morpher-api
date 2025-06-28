package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.JarReader;
import com.github.luiox.morpher.jar.JarWriter;
import com.github.luiox.morpher.model.IResource;
import com.github.luiox.morpher.model.ResourceContainer;
import com.github.luiox.morpher.util.type.Result;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);

    private ResourceUtil() {
    }

    public static void importFromClassFile(@NotNull ResourceContainer container, String filePath) {
        try {
            container.importResource(new SimpleClassFileImporter(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importFromJar(@NotNull ResourceContainer container, String filePath) {
        try {
            container.importResource(new JarFileImporter(new JarReader(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportToJar(@NotNull ResourceContainer container, String filePath) {
        try {
            container.exportResource(new JarFileExporter(new JarWriter(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportToClassFile(@NotNull ResourceContainer container, String filePath, String resourcePath) {
        try {
            container.exportResource(new SimpleClassFileExporter(resourcePath, filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Result<IResource, String> getOnlyResource(@NotNull ResourceContainer container) {
        if (container.isEmpty()) {
            return Result.Err("the container is empty");
        }
        return Result.Ok(container.values().iterator().next());
    }
}
