package com.github.luiox.morpher.info;

import java.util.List;

public class ClassInfo {
    // class splash name
    public String name;
    public int access;
    // super class splash name
    public String superName;
    // interfaces splash names，如果没有接口可能为null
    public List<String> interfaces;
    // 字节码版本号
    public int version;

    public ClassInfo(String name, int access, String superName, List<String> interfaces, int version) {
        this.name = name;
        this.access = access;
        this.superName = superName;
        this.interfaces = interfaces;
        this.version = version;
    }
}
