package com.github.luiox.morpher.info;

import com.github.luiox.morpher.model.ResourceContainer;
import com.github.luiox.morpher.util.type.Pair;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoUtil {
    private InfoUtil() {
    }

    public static @NotNull Map<String, Pair<String, ClassInfo>> buildClassName2Info(@NotNull ResourceContainer container) {
        return buildClassName2Info(container, false);
    }

    // 构建一个类名到location,ClassInfo的映射表
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

    // 默认不要接口，反正也没什么用
    public static @NotNull Map<String, ClassInfo> buildClassInfo(@NotNull ResourceContainer container) {
        return buildClassInfo(container, false);
    }

    // 构建一个info表
    // location -> ClassInfo
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
