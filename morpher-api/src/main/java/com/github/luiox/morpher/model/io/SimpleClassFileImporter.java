package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 简单的class文件导入器。
 * <p>
 * 实现IResourceImporter接口，将本地class文件导入到资源容器。
 */
public class SimpleClassFileImporter implements IResourceImporter {
    /** class文件路径 */
    String path;

    /**
     * 构造方法。
     * @param path class文件路径
     */
    public SimpleClassFileImporter(String path) {
        this.path = path;
    }

    /**
     * 从本地class文件导入资源到容器。
     * @param container 资源容器
     * @throws IOException 读取文件异常
     */
    @Override
    public void importResource(@NotNull IResourceContainer container) throws IOException {
        var bytes = Files.readAllBytes(Path.of(path));
        container.addResource(path, new ClassResource(path, bytes));
    }
}
