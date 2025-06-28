package com.github.luiox.morpher.model;

public class UnknownResource implements IResource {

    String path;
    byte[] content;

    public UnknownResource(String path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    @Override
    public String getLocation() {
        return path;
    }

    public byte[] get() {
        return content;
    }

    public void set(byte[] bytes) {
        content = bytes;
    }
}
