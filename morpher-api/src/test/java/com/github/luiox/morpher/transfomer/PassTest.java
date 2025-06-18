package com.github.luiox.morpher.transfomer;

import com.github.luiox.morpher.jar.FullJarCaches;
import com.github.luiox.morpher.jar.SimpleJarReader;
import com.github.luiox.morpher.transformer.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class DemoClassPass extends ClassPass {

    @Override
    public void run(@NotNull ClassNode classNode, @NotNull IPassContext context) {
        System.out.println("class name : " + classNode.name);
    }
}

class DemoMethodPass extends MethodPass {

    @Override
    public void run(@NotNull MethodNode methodNode, @NotNull IPassContext context) {
        System.out.println("class name : " + context.currentClass().name + ", method name : " + methodNode.name);
    }
}

public class PassTest {
    @Test
    public void test() {
        PassContext context = new PassContext();
        context.jarCaches = new FullJarCaches();
        context.jarCaches.read(new SimpleJarReader("D:\\WorkSpace\\mc\\morpher\\morpher-testdemo\\build\\libs\\snake.jar"));

        SimplePassRunner runner = new SimplePassRunner();
        runner.addPass(new DemoClassPass());
        runner.addPass(new DemoMethodPass());
        runner.transform(context);

//        context.jarCaches.write(new SimpleJarWriter("target/classes"));
    }
}
