package com.github.luiox.morpher.asm.writer;

import com.github.luiox.morpher.jar.JarReader;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class CommonSuperClassProvider implements ICommonSuperClassProvider {
    private static final Logger logger = LoggerFactory.getLogger(CommonSuperClassProvider.class);

    Map<String, String> superClassNameMap = new HashMap<>();

    public Map<String, String> getSuperClassNameMap() {
        return superClassNameMap;
    }

    public void scan(String folderPath) {
        List<Path> zips = new ArrayList<>();
        try {
            Files.walkFileTree(Path.of(folderPath), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (!Files.isDirectory(file)) {
                        String fileName = file.toString().toLowerCase();
                        if (fileName.endsWith(".zip") || fileName.endsWith(".jar") || fileName.endsWith(".jmod")) {
                            zips.add(file);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error walking file tree: " + e.getMessage());
            return;
        }
        for (var zip : zips) {
            // 读取每个jar
            // 读取每个class
            try {
                JarReader jarReader = new JarReader(zip.toString());
                jarReader.read((filePath, is) -> {
                    try {
                        if (filePath.contains("module-info.class")) {
                            return;
                        }
                        if (!filePath.endsWith(".class")) {
                            return;
                        }
                        byte[] data = is.readAllBytes();
                        ClassReader reader = new ClassReader(data);
                        var className = reader.getClassName();
                        var superName = reader.getSuperName();
                        superClassNameMap.put(className, superName);
                    } catch (Exception e) {
                        logger.error("Error reading class file: {}", filePath, e);
                    }
                });
            } catch (Exception e) {
                logger.error("Error reading jar file: {}", zip, e);
            }
        }
    }

    public List<String> getSuperClasses(String type) {
        List<String> superClasses = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        // 初始检查
        if (!superClassNameMap.containsKey(type)) {
            // 如果不包含，那么输出信息到串口
            logger.error("type not found: {}", type);
            return List.of("java/lang/Object");
        }

        String currentType = type;
        while (true) {
            // 检查是否已经访问过，避免循环
            if (visited.contains(currentType)) {
                logger.error("circular dependency found: {}", currentType);
                break;
            }
            visited.add(currentType);

            // 先检查是否包含
            if (!superClassNameMap.containsKey(currentType)) {
                // 不包含就得先输出
                logger.error("type not found: {}", currentType);
                break;
            }

            // 添加当前的类到列表
            superClasses.add(currentType);

            // 获取当前类型的直接父类
            String superClass = superClassNameMap.get(currentType);

            // 如果superClass为null，那就是java/lang/Object
            // 如果当前是java/lang/Object了，那么应该结束了
            if (superClass == null) {
                superClass = "java/lang/Object";
                break;
            }

            // 更新当前类型为父类，继续循环
            currentType = superClass;
        }

        return superClasses.reversed();
    }

    @Override
    public String getCommonSuperClasses(String type1, String type2) {
        // 获取type1的父类列表
        List<String> superClasses1 = getSuperClasses(type1);
        // 获取type2的父类列表
        List<String> superClasses2 = getSuperClasses(type2);
        // 获取两个父类列表的长度
        int size = Math.min(superClasses1.size(), superClasses2.size());
        // 初始化索引
        int i = 0;
        // 遍历两个父类列表，找到第一个不相等的父类
        while (i < size && superClasses1.get(i).equals(superClasses2.get(i))) {
            i++;
        }
        // 如果没有找到不相等的父类，返回Object
        if (i == 0) {
            return "java/lang/Object";
        } else {
            // 返回第一个不相等的父类
            return superClasses1.get(i - 1);
        }
    }
}
