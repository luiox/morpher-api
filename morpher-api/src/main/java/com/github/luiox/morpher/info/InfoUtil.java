package com.github.luiox.morpher.info;

import com.github.luiox.morpher.model.ResourceContainer;
import com.github.luiox.morpher.util.type.Pair;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类信息工具类，提供从资源容器中批量提取ClassInfo的静态方法。
 * 该类不可实例化。
 */
public class InfoUtil {
    /**
     * 私有构造方法，防止实例化
     */
    private InfoUtil() {
    }

    /**
     * 构建类名到（资源位置, ClassInfo）映射表。
     *
     * @param container 资源容器
     * @return 类名到Pair(资源位置, ClassInfo)的映射
     */
    public static @NotNull Map<String, Pair<String, ClassInfo>> buildClassName2Info(@NotNull ResourceContainer container) {
        return buildClassName2Info(container, false);
    }

    /**
     * 构建类名到（资源位置, ClassInfo）映射表，可选择是否跳过接口。
     *
     * @param container      资源容器
     * @param skipInterfaces 是否跳过接口
     * @return 类名到Pair(资源位置, ClassInfo)的映射
     */
    public static @NotNull Map<String, Pair<String, ClassInfo>> buildClassName2Info(@NotNull ResourceContainer container,
                                                                                    boolean skipInterfaces) {
        Map<String, Pair<String, ClassInfo>> classInfos = new HashMap<>();

        container.classes().forEach(classResource -> {
            var bytes = classResource.get();
            ClassReader classReader = new ClassReader(bytes);
            classReader.getClassName();
            var interfaces = skipInterfaces ? null : List.of(classReader.getInterfaces());
            classInfos.put(classReader.getClassName(),
                    Pair.of(classResource.getLocation(),
                            new ClassInfo(classReader.getClassName(),
                                    classReader.getAccess(),
                                    classReader.getSuperName(),
                                    interfaces,
                                    classReader.readShort(6)
                            )
                    )
            );
        });

        return classInfos;
    }

    /**
     * 构建资源位置到ClassInfo的映射表（默认不包含接口信息）。
     *
     * @param container 资源容器
     * @return 资源位置到ClassInfo的映射
     */
    public static @NotNull Map<String, ClassInfo> buildClassInfo(@NotNull ResourceContainer container) {
        return buildClassInfo(container, false);
    }

    /**
     * 构建资源位置到ClassInfo的映射表。
     *
     * @param container      资源容器
     * @param skipInterfaces 是否跳过接口
     * @return 资源位置到ClassInfo的映射
     */
    public static @NotNull Map<String, ClassInfo> buildClassInfo(@NotNull ResourceContainer container,
                                                                 boolean skipInterfaces) {
        Map<String, ClassInfo> classInfos = new HashMap<>();

        container.classes().forEach(classResource -> {
            var bytes = classResource.get();
            ClassReader classReader = new ClassReader(bytes);
            classReader.getClassName();
            var interfaces = skipInterfaces ? null : List.of(classReader.getInterfaces());
            classInfos.put(classResource.getLocation(),
                    new ClassInfo(classReader.getClassName(),
                            classReader.getAccess(),
                            classReader.getSuperName(),
                            interfaces,
                            classReader.readShort(6)
                    )
            );
        });

        return classInfos;
    }
}
