package com.github.luiox.morpher.model;

import com.github.luiox.morpher.jar.JarUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.jar.Manifest;

/**
 * 表示一个Manifest文件资源。
 * <p>
 * 封装了Manifest对象的读写与类型标识。
 */
public class ManifestResource implements IResource {
    /**
     * Manifest对象
     */
    private Manifest manifest;

    /**
     * 构造方法。
     *
     * @param manifest Manifest对象
     */
    public ManifestResource(Manifest manifest) {
        this.manifest = manifest;
    }

    /**
     * 通过字节数组创建ManifestResource。
     *
     * @param bytes Manifest字节内容
     * @return ManifestResource实例
     * @throws IOException 解析失败时抛出
     */
    public static ManifestResource from(byte[] bytes) throws IOException {
        Manifest manifest = new Manifest(new ByteArrayInputStream(bytes));
        ManifestResource resource = new ManifestResource(manifest);
        return resource;
    }

    /**
     * 获取资源路径。
     *
     * @return Manifest文件名
     */
    @Override
    public String getLocation() {
        return JarUtil.ManifestFileName;
    }

    /**
     * 获取Manifest对象。
     *
     * @return Manifest对象
     */
    public Manifest get() {
        return manifest;
    }

    /**
     * 设置Manifest对象。
     *
     * @param manifest 新Manifest对象
     */
    public void set(Manifest manifest) {
        this.manifest = manifest;
    }

    /**
     * 获取资源类型。
     *
     * @return ResourceType.Manifest
     */
    @Override
    public ResourceType getType() {
        return ResourceType.Manifest;
    }
}
