package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.info.InfoUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Phase {
    public final String name;
    // 多个MethodPass和ClassPass, 或者一个GlobalPass
    private final List<AbstractPass> passes = new ArrayList<>();
    private int readFlag;
    private int writeFlag;

    public Phase(String name) {
        this(name, 0, 0);
    }

    public Phase(String name, int readFlag, int writeFlag) {
        this.name = name;
        this.readFlag = readFlag;
        this.writeFlag = writeFlag;
    }

    public Phase add(MethodPass methodPass) {
        passes.add(methodPass);
        return this;
    }

    public Phase add(ClassPass classPass) {
        passes.add(classPass);
        return this;
    }

    public Phase withReadFlag(int readFlag) {
        this.readFlag = readFlag;
        return this;
    }

    public Phase withWriteFlag(int writeFlag) {
        this.writeFlag = writeFlag;
        return this;
    }

    static IterateClassNodeFunc iterateClassNodeFunc;

    static {
        iterateClassNodeFunc = (context, rflag, wflag, passes) -> {
            // 构建一下索引
            var infos = InfoUtil.buildClassInfo(context.getContainer());
            // 以索引的方式迭代
            PassHelper.iterateClassNodeWithInfo(context, infos, rflag, wflag, classNode -> {
                for (var pass : passes) {
                    if (pass instanceof MethodPass methodPass) {
                        for (var methodNode : classNode.methods) {
                            methodPass.run(methodNode, context);
                        }
                    } else if (pass instanceof ClassPass classPass) {
                        classPass.run(classNode, context);
                    } else {
                        throw new IllegalArgumentException("Unsupport pass type");
                    }
                }
            });
        };
    }

    public static void setIterateClassNodeFunc(IterateClassNodeFunc func) {
        iterateClassNodeFunc = func;
    }

    public void runPhase(IPassContext ctx) {
        if (!(ctx instanceof NewPassContext context)) {
            throw new IllegalArgumentException("ctx must be NewPassContext");
        }

        for (var passes : passes) {
            passes.doInitialization(context);
        }

        if (passes.size() == 1 && passes.getFirst() instanceof GlobalPass globalPass) {
            globalPass.run(context);
        } else {
            iterateClassNodeFunc.iterate(context, readFlag, writeFlag, passes);
        }

        for (var passes : passes) {
            passes.doFinalization(context);
        }
    }

    public static @NotNull Phase of(String name, GlobalPass globalPass) {
        var phase = new Phase(name);
        phase.passes.add(globalPass);
        return phase;
    }

    public static @NotNull Phase of(String name, int readFlag, int writeFlag, GlobalPass globalPass) {
        var phase = new Phase(name, readFlag, writeFlag);
        phase.passes.add(globalPass);
        return phase;
    }

    public static @NotNull Phase of(String name, MethodPass... methodPasses) {
        var phase = new Phase(name);
        phase.passes.addAll(List.of(methodPasses));
        return phase;
    }

    public static @NotNull Phase of(String name, ClassPass... classPasses) {
        var phase = new Phase(name);
        phase.passes.addAll(List.of(classPasses));
        return phase;
    }

    public static @NotNull Phase of(String name) {
        return new Phase(name);
    }

    public static @NotNull Phase of(String name, int readFlag, int writeFlag) {
        return new Phase(name, readFlag, writeFlag);
    }

    public interface IterateClassNodeFunc {
        void iterate(@NotNull NewPassContext context,
                     int rflag,
                     int wflag, List<AbstractPass> passes);
    }
}
