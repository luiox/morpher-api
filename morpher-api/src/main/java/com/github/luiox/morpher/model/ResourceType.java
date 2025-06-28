package com.github.luiox.morpher.model;

/**
 * 资源类型枚举。
 * <p>
 * 用于标识不同类型的资源，如Class文件、Manifest文件、Jar包或未知类型。
 */
public enum ResourceType {
    /**
     * class文件
     */
    Class,
    /**
     * manifest文件
     */
    Manifest,
    /**
     * jar文件
     */
    Jar,
    /**
     * 其他未知类型
     */
    Unknown,
}
