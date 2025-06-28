package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.JarReader;
import com.github.luiox.morpher.jar.JarWriter;
import com.github.luiox.morpher.model.IResource;
import com.github.luiox.morpher.model.ResourceContainer;
import com.github.luiox.morpher.util.type.Result;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源操作辅助工具类。
 * <p>
 * 提供常用的资源导入导出静态方法，简化资源文件与容器的交互。
 */
public class ResourceHelper {
    private static final Logger logger = LoggerFactory.getLogger(ResourceHelper.class);

    /**
     * 私有构造方法，防止实例化
     */
    private ResourceHelper() {
    }

    /**
     * 从class文件导入资源到容器。
     *
     * @param container 资源容器
     * @param filePath  class文件路径
     */
    public static void importFromClassFile(@NotNull ResourceContainer container, String filePath) {
        try {
            container.importResource(new SimpleClassFileImporter(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从jar包导入资源到容器。
     *
     * @param container 资源容器
     * @param filePath  jar包路径
     */
    public static void importFromJar(@NotNull ResourceContainer container, String filePath) {
        try {
            container.importResource(new JarFileImporter(new JarReader(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将资源容器导出为jar包。
     *
     * @param container 资源容器
     * @param filePath  jar包路径
     */
    public static void exportToJar(@NotNull ResourceContainer container, String filePath) {
        try {
            container.exportResource(new JarFileExporter(new JarWriter(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定资源导出为class文件。
     *
     * @param container    资源容器
     * @param filePath     class文件输出路径
     * @param resourcePath 资源在容器中的路径
     */
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
