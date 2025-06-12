package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.jar.JarCachesEntry;
import com.github.luiox.morpher.jar.JarCachesEntryType;
import com.github.luiox.morpher.util.LogUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimplePassRunner implements IPassRunner {
    private List<AbstractPass> passes = new ArrayList<>();

    public void addPass(AbstractPass pass) {
        passes.add(pass);
    }

    @Override
    public void transform(PassContext context) {
        for (AbstractPass pass : passes) {
            pass.doInitialization(context);
        }

        for (AbstractPass pass : passes) {
            if (pass instanceof GlobalPass globalPass) {
                globalPass.run(context);
            } else if (pass instanceof ClassPass classPass) {
                AtomicInteger index = new AtomicInteger(1);
                context.jarCaches.entries(JarCachesEntryType.Class).forEach(entry -> {
                    try {
                        ClassReader classReader = new ClassReader(entry.content);
                        ClassNode classNode = new ClassNode();
                        classReader.accept(classNode, 0);

                        System.out.println("index : " + index.getAndIncrement() + ", class path : " + entry.path);

                        classPass.run(classNode, context);
                        // 写回去
                        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        classNode.accept(classWriter);

                        context.deleteClassNode(classNode);
                        context.addClassNode(classNode.name, classWriter.toByteArray());
                    } catch (Exception e) {
                        LogUtil.printStackTrace(e);
                    }
                });
                context.applyClassModify();

            } else if (pass instanceof MethodPass methodPass) {
                AtomicInteger index = new AtomicInteger(1);
                context.jarCaches.entries(JarCachesEntryType.Class).forEach(entry -> {
                    try {
                        ClassReader classReader = new ClassReader(entry.content);
                        ClassNode classNode = new ClassNode();
                        classReader.accept(classNode, 0);

                        System.out.println("index : " + index.getAndIncrement() + ", class path : " + entry.path);

                        // 设置当前的类
                        context.currentClass = classNode;
                        for(var method : classNode.methods){
                            methodPass.run(method, context);
                        }

                        // 写回去
                        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        classNode.accept(classWriter);

                        context.deleteClassNode(classNode);
                        context.addClassNode(classNode.name, classWriter.toByteArray());
                    } catch (Exception e) {
                        LogUtil.printStackTrace(e);
                    }
                });
                context.applyClassModify();
            }
        }

        for (AbstractPass pass : passes) {
            pass.doFinalization(context);
        }
    }
}
