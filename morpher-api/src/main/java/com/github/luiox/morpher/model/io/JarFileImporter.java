package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.IJarReader;
import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.IResourceContainer;
import com.github.luiox.morpher.model.ManifestResource;
import com.github.luiox.morpher.model.UnknownResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JarFileImporter implements IResourceImporter {
    IJarReader reader;

    public JarFileImporter(@NotNull IJarReader reader) {
        this.reader = reader;
    }

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
