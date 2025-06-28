package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleClassFileImporter implements IResourceImporter {
    String path;

    public SimpleClassFileImporter(String path) {
        this.path = path;
    }

    @Override
    public void importResource(@NotNull IResourceContainer container) throws IOException {
        var bytes = Files.readAllBytes(Path.of(path));
        container.addResource(path, new ClassResource(path, bytes));
    }
}
