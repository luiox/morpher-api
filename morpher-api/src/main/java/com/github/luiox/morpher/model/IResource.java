package com.github.luiox.morpher.model;

/**
 * 资源对象的通用接口。
 * <p>
 * 所有资源类型（如Class、Manifest、未知等）都应实现该接口。
 */
public interface IResource {
    /**
     * 获取资源URI。
     * <p>
     * 如果是jar包中的资源，则返回jar包路径；如果是磁盘文件，则返回磁盘路径。
     *
     * @return 资源URI
     */
    String getLocation();

    /**
     * 获取资源类型。
     *
     * @return 资源类型
     */
    default ResourceType getType() {
        return ResourceType.Unknown;
    }
}
