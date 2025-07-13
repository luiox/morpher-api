package com.github.luiox.morpher.asm.writer;

public interface ICommonSuperClassProvider {
    /**
     * 获取两个类的公共父类
     * @param type1 类1的InternalName，例如geny/Generator$Action
     * @param type2 类2的InternalName，例如geny/Generator$Action
     * @return 返回一个字符串，表示两个类的公共父类
     */
    String getCommonSuperClasses(String type1, String type2);
}
