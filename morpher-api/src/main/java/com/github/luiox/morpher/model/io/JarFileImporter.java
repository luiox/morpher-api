package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.IJarReader;
import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.IResourceContainer;
import com.github.luiox.morpher.model.ManifestResource;
import com.github.luiox.morpher.model.UnknownResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Jar包导入器。
 * <p>
 * 实现IResourceImporter接口，将Jar包中的内容导入到资源容器。
 */
public class JarFileImporter implements IResourceImporter {
    /**
     * Jar读取器
     */
    IJarReader reader;

    /**
     * 构造方法。
     *
     * @param reader Jar读取器
     */
    public JarFileImporter(@NotNull IJarReader reader) {
        this.reader = reader;
    }

    /**
     * 从Jar包导入资源到容器。
     *
     * @param container 资源容器
     * @throws Exception 导入异常
     */
    @Override
    public void importResource(@NotNull IResourceContainer container) throws Exception {
        reader.read((entryName, is) -> {
            // 根据读入的内容加到container里面
            if (entryName.equals(JarUtil.ManifestFileName)) {
                try {
                    container.addResource(JarUtil.ManifestFileName, ManifestResource.from(is.readAllBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (JarUtil.isClassFile(entryName) && JarUtil.isClassFile(is)) {
                try {
                    container.addResource(entryName, new ClassResource(entryName, is.readAllBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    container.addResource(entryName, new UnknownResource(entryName, is.readAllBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
