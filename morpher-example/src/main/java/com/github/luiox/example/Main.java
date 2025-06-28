package com.github.luiox.example;

import com.github.luiox.morpher.model.io.ResourceHelper;
import com.github.luiox.morpher.transformer.PassContext;
import com.github.luiox.morpher.transformer.PassRunner;
import com.github.luiox.morpher.transformer.Phase;
import com.github.luiox.morpher.transformer.Pipeline;
import org.objectweb.asm.ClassWriter;

public class Main {
    public static void main(String[] args) {
        PassContext context = new PassContext();
        ResourceHelper.importFromJar(context.getContainer(), "sample-001.jar");

        PassRunner runner = new PassRunner();
        runner.add(Pipeline.of("test")
                .add(Phase.of("test1", 0, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)
                        .add(new Sample001Pass1())
                        .add(new DeadCodeRemover())
                        .add(new Sample001Pass2())
                        .add(new UnusedLabelRemover())
                        .add(new Sample001Pass3())
                )
        ).transform(context);

        ResourceHelper.exportToJar(context.getContainer(), "output.jar");
    }
}
