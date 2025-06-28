package com.github.luiox.morpher.model;

/**
 * 表示一个未知类型的资源。
 * <p>
 * 用于存储无法识别类型的二进制资源。
 */
public class UnknownResource implements IResource {
    /** 资源路径 */
    String path;
    /** 资源内容 */
    byte[] content;

    /**
     * 构造方法。
     * @param path 资源路径
     * @param content 资源内容
     */
    public UnknownResource(String path, byte[] content) {
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
     * 获取资源内容。
     * @return 字节数组
     */
    public byte[] get() {
        return content;
    }

    /**
     * 设置资源内容。
     * @param bytes 新内容
     */
    public void set(byte[] bytes) {
        content = bytes;
    }
}
