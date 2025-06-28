package com.github.luiox.morpher.model;

/**
 * 标记资源
 */
public interface IResource {

    /**
     * 获取资源URI，如果是另外一个jar包中的资源，则返回jar包的路径，
     * 如果是磁盘上的就返回磁盘路径
     *
     * @return 资源URI
     */
    String getLocation();

    /**
     * 获取资源类型
     *
     * @return 资源类型
     */
    default ResourceType getType() {
        return ResourceType.Unknown;
    }
}
