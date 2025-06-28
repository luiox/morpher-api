package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.info.ClassInfo;
import com.github.luiox.morpher.model.ClassResource;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PassHelper {
    private PassHelper(){
    }

    private static final Logger logger = LoggerFactory.getLogger(PassHelper.class);

    public static void iterateClassNodeWithInfo(@NotNull NewPassContext context,
                                                @NotNull Map<String, ClassInfo> infos,
                                                int rflag,
                                                int wflag,
                                                @NotNull Consumer<ClassNode> consumer) {
        AtomicInteger index = new AtomicInteger(1);
        AtomicInteger total = new AtomicInteger(context.getContainer().size());

        var classFilter = context.getCurrentClassFilter();

        // 运行startHook
        context.runBeforeAllClassesHooks();

        for (var entry : infos.entrySet()) {
            var info = entry.getValue();
            // 如果为false，那么就被过滤
            if (classFilter != null) {
                if (!classFilter.test(info.name)) {
                    continue;
                }
            }
            var resource = context.getContainer().get(entry.getKey());
            if (resource == null) {
                logger.error("can not find resource for location : {}", entry.getKey());
                continue;
            }
            if (resource instanceof ClassResource classResource) {
                try {
                    // 根据pass的配置读取
                    ClassReader classReader = new ClassReader(classResource.get());
                    ClassNode classNode = new ClassNode();
                    classReader.accept(classNode, rflag);

                    // 打印信息
                    printProgress(index.getAndIncrement(), total.get(), classNode.name);

                    context.setCurrentClass(classNode);

                    String oldName = classNode.name;
                    consumer.accept(classNode);
                    // 根据参数写回去
                    ClassWriter classWriter;

                    classWriter = new ClassWriter(wflag);

                    try {
                        classNode.accept(classWriter);
                    }catch (Exception e){
                        logger.info("error when transform class: {}", classNode.name);
                        e.printStackTrace();
                    }

                    if (oldName.equals(classNode.name)) {
                        // 没有改名
                        classResource.set(classWriter.toByteArray());
                    } else {
                        // 改名了
                        String newName = classNode.name;
                        // 移除之前的
                        context.getContainer().remove(oldName);
                        // 写入新的
                        context.getContainer().put(newName, new ClassResource(newName, classWriter.toByteArray()));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            } else {
                logger.error("location : {} is not ClassResource", entry.getKey());
                continue;
            }
        }
        System.out.println();

        // 运行endHook
        context.runAfterAllClassesHook();
    }

    public static void printProgress(int currentClassIdx, int totalClassSize, String className) {
        // 清除当前行
        System.out.print("\r");
        // 打印进度信息
        System.out.printf("Class: [%d/%d] %s",
                currentClassIdx, totalClassSize, className);
        // 如果进度完成，换行
        if (currentClassIdx == totalClassSize) {
            System.out.println();
        }
    }
}
