package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.model.ClassResource;
import com.github.luiox.morpher.model.ResourceContainer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassContext extends BasicPassContext {
    private final ResourceContainer container = new ResourceContainer();

    public ResourceContainer getContainer() {
        return container;
    }

    // class name
    private final List<String> classDeleteList = new ArrayList<>();
    // class name -> byte[]
    private final Map<String, byte[]> classAddList = new HashMap<>();

    public void deleteClassNode(String className) {
        classDeleteList.add(className);
    }

    public void deleteClassNode(@NotNull ClassNode classNode) {
        classDeleteList.add(classNode.name);
    }

    public void addClassNode(String className, byte[] classBytes) {
        classAddList.put(className, classBytes);
    }

    public void applyClassModify() {
        for (var className : classDeleteList) {
            container.remove(className + ".class");
        }
        for (var entry : classAddList.entrySet()) {
            var key = entry.getKey() + ".class";
            container.addResource(key, new ClassResource(key, entry.getValue()));
        }
    }

}
