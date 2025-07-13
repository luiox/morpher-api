package com.github.luiox.morpher.asm.writer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * 定制化分析公共父类的ClassWriter
 */
public class DelegatingSuperClassWriter extends ClassWriter {

    ICommonSuperClassProvider provider;

    public DelegatingSuperClassWriter(ICommonSuperClassProvider provider, int flags) {
        super(flags);
        this.provider = provider;
    }

    public DelegatingSuperClassWriter(ICommonSuperClassProvider provider, ClassReader classReader, int flags) {
        super(classReader, flags);
        this.provider = provider;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return provider.getCommonSuperClasses(type1, type2);
    }
}
