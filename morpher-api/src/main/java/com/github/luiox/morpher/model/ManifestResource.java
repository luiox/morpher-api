package com.github.luiox.morpher.model;

import com.github.luiox.morpher.jar.JarUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.jar.Manifest;

public class ManifestResource implements IResource {

    private Manifest manifest;

    public ManifestResource(Manifest manifest) {
        this.manifest = manifest;
    }

    public static ManifestResource from(byte[] bytes) throws IOException {
        Manifest manifest = new Manifest(new ByteArrayInputStream(bytes));
        ManifestResource resource = new ManifestResource(manifest);
        return resource;
    }

    @Override
    public String getLocation() {
        return JarUtil.ManifestFileName;
    }

    public Manifest get() {
        return manifest;
    }

    public void set(Manifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.Manifest;
    }
}
