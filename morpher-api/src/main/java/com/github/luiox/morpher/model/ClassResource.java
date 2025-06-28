package com.github.luiox.morpher.model;

/**
 * 表示一个class文件资源。
 * <p>
 * 封装了class文件的路径和字节内容。
 */
public class ClassResource implements IResource {
    /** 资源路径 */
    String path;
    /** class文件内容 */
    byte[] content;

    /**
     * 构造方法。
     * @param path 资源路径
     * @param content class文件内容
     */
    public ClassResource(String path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    /**
     * 获取资源路径。
     * @return 路径
     */
    @Override
    public String getLocation() {
        return path;
    }

    /**
     * 获取资源类型。
     * @return ResourceType.Class
     */
    @Override
    public ResourceType getType() {
        return ResourceType.Class;
    }

    /**
     * 获取class文件内容。
     * @return 字节数组
     */
    public byte[] get() {
        return content;
    }

    /**
     * 设置class文件内容。
     * @param bytes 新内容
     */
    public void set(byte[] bytes) {
        content = bytes;
    }
}
