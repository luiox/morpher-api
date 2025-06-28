package com.github.luiox.morpher.model;

public class ClassResource implements IResource {

    String path;
    byte[] content;

    public ClassResource(String path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    @Override
    public String getLocation() {
        return path;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.Class;
    }

    public byte[] get() {
        return content;
    }

    public void set(byte[] bytes) {
        content = bytes;
    }
}
