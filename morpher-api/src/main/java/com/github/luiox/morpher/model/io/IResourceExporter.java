package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

public interface IResourceExporter {
    void exportResource(@NotNull IResourceContainer container) throws Exception;
}