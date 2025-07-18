package com.github.luiox.passes;

import com.github.luiox.morpher.model.io.ResourceHelper;
import com.github.luiox.morpher.transformer.PassContext;
import com.github.luiox.morpher.transformer.PassRunner;
import com.github.luiox.morpher.transformer.Phase;
import com.github.luiox.morpher.transformer.Pipeline;
import com.github.luiox.passes.deobfuscate.Sample001Pass1;
import com.github.luiox.passes.deobfuscate.Sample001Pass2;
import com.github.luiox.passes.deobfuscate.Sample001Pass3;
import com.github.luiox.passes.optimize.DeadCodeRemover;
import com.github.luiox.passes.optimize.UnusedLabelRemover;
import org.objectweb.asm.ClassWriter;

public class Main {
    public static void main(String[] args) {
        PassContext context = new PassContext();
        ResourceHelper.importFromJar(context.getContainer(), "sample-001.jar");

        // 添加依赖库路径，如果不加的话，可能会导致找不到类
//        context.getPassHelper().addLibPath("dep");

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
