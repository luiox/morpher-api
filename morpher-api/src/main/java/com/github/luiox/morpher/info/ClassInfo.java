package com.github.luiox.morpher.info;

import java.util.List;

/**
 * 用于描述类的基本信息的数据结构。
 * 包含类名、访问标志、父类名、接口列表和字节码版本号等。
 */
public class ClassInfo {
    /** 类的斜杠分隔名称（如 java/lang/Object） */
    public String name;
    /** 访问标志（如 public、final 等） */
    public int access;
    /** 父类的斜杠分隔名称 */
    public String superName;
    /** 实现的接口名称列表，可能为null */
    public List<String> interfaces;
    /** 字节码版本号 */
    public int version;

    /**
     * 构造方法。
     * @param name 类名
     * @param access 访问标志
     * @param superName 父类名
     * @param interfaces 接口列表
     * @param version 字节码版本号
     */
    public ClassInfo(String name, int access, String superName, List<String> interfaces, int version) {
        this.name = name;
        this.access = access;
        this.superName = superName;
        this.interfaces = interfaces;
        this.version = version;
    }
}
