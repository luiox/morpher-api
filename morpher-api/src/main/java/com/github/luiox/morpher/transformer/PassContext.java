package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.jar.IJarCaches;
import com.github.luiox.morpher.jar.JarCachesEntry;
import com.github.luiox.morpher.jar.JarCachesEntryType;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassContext extends BasicPassContext{

    public IJarCaches jarCaches;

    public PassContext() {
        this(false);
    }

    public PassContext(boolean fullLoad) {
    }

    // class name
    private List<String> classDeleteList = new ArrayList<>();
    // class name -> byte[]
    private Map<String, byte[]> classAddList = new HashMap<>();

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
            jarCaches.removeEntry(className + ".class");
        }
        for (var entry : classAddList.entrySet()) {
            var e = new JarCachesEntry();
            e.content = entry.getValue();
            e.type = JarCachesEntryType.Class;
            e.path = entry.getKey() + ".class";
            jarCaches.addEntry(e);
        }
    }
}
