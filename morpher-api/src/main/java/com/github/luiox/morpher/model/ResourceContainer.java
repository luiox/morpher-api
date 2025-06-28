package com.github.luiox.morpher.model;

import com.github.luiox.morpher.model.io.IResourceExporter;
import com.github.luiox.morpher.model.io.IResourceImporter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResourceContainer extends HashMap<String, IResource> implements IResourceContainer {

    @Override
    public void addResource(String uri, @NotNull IResource resource) {
        put(uri, resource);
    }

    @Override
    public boolean hasResource(String uri) {
        return containsKey(uri);
    }

    @Override
    public void importResource(@NotNull IResourceImporter importer) throws Exception {
        importer.importResource(this);
    }

    @Override
    public void exportResource(@NotNull IResourceExporter exporter) throws Exception {
        exporter.exportResource(this);
    }
}
