package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.info.InfoUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个Pass阶段（Phase），可包含多个MethodPass、ClassPass或一个GlobalPass。
 * 用于组织和执行一组相关的代码转换任务。
 */
public class Phase {
    /** 阶段名称 */
    public final String name;
    /** 阶段包含的Pass列表 */
    private final List<AbstractPass> passes = new ArrayList<>();
    /** 读取标志位 */
    private int readFlag;
    /** 写入标志位 */
    private int writeFlag;

    /**
     * 构造一个Phase实例，默认读写标志为0。
     * @param name 阶段名称
     */
    public Phase(String name) {
        this(name, 0, 0);
    }

    /**
     * 构造一个Phase实例。
     * @param name 阶段名称
     * @param readFlag 读取标志
     * @param writeFlag 写入标志
     */
    public Phase(String name, int readFlag, int writeFlag) {
        this.name = name;
        this.readFlag = readFlag;
        this.writeFlag = writeFlag;
    }

    /**
     * 添加一个MethodPass到阶段。
     * @param methodPass 方法级Pass
     * @return 当前Phase实例
     */
    public Phase add(MethodPass methodPass) {
        passes.add(methodPass);
        return this;
    }

    /**
     * 添加一个ClassPass到阶段。
     * @param classPass 类级Pass
     * @return 当前Phase实例
     */
    public Phase add(ClassPass classPass) {
        passes.add(classPass);
        return this;
    }

    /**
     * 设置读取标志位。
     * @param readFlag 读取标志
     * @return 当前Phase实例
     */
    public Phase withReadFlag(int readFlag) {
        this.readFlag = readFlag;
        return this;
    }

    /**
     * 设置写入标志位。
     * @param writeFlag 写入标志
     * @return 当前Phase实例
     */
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

    /**
     * 运行当前阶段，依次初始化、执行、收尾所有Pass。
     * @param ctx Pass上下文
     */
    public void runPhase(IPassContext ctx) {
        if (!(ctx instanceof PassContext context)) {
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

    /**
     * 设置自定义的类节点遍历函数。
     * @param func 遍历函数实现
     */
    public static void setIterateClassNodeFunc(IterateClassNodeFunc func) {
        iterateClassNodeFunc = func;
    }

    /**
     * 以GlobalPass创建阶段。
     * @param name 阶段名称
     * @param globalPass 全局Pass
     * @return Phase实例
     */
    public static @NotNull Phase of(String name, GlobalPass globalPass) {
        var phase = new Phase(name);
        phase.passes.add(globalPass);
        return phase;
    }

    /**
     * 以GlobalPass和标志位创建阶段。
     * @param name 阶段名称
     * @param readFlag 读取标志
     * @param writeFlag 写入标志
     * @param globalPass 全局Pass
     * @return Phase实例
     */
    public static @NotNull Phase of(String name, int readFlag, int writeFlag, GlobalPass globalPass) {
        var phase = new Phase(name, readFlag, writeFlag);
        phase.passes.add(globalPass);
        return phase;
    }

    /**
     * 以多个MethodPass创建阶段。
     * @param name 阶段名称
     * @param methodPasses 方法级Pass
     * @return Phase实例
     */
    public static @NotNull Phase of(String name, MethodPass... methodPasses) {
        var phase = new Phase(name);
        phase.passes.addAll(List.of(methodPasses));
        return phase;
    }

    /**
     * 以多个ClassPass创建阶段。
     * @param name 阶段名称
     * @param classPasses 类级Pass
     * @return Phase实例
     */
    public static @NotNull Phase of(String name, ClassPass... classPasses) {
        var phase = new Phase(name);
        phase.passes.addAll(List.of(classPasses));
        return phase;
    }

    /**
     * 以名称创建空阶段。
     * @param name 阶段名称
     * @return Phase实例
     */
    public static @NotNull Phase of(String name) {
        return new Phase(name);
    }

    /**
     * 以名称和标志位创建空阶段。
     * @param name 阶段名称
     * @param readFlag 读取标志
     * @param writeFlag 写入标志
     * @return Phase实例
     */
    public static @NotNull Phase of(String name, int readFlag, int writeFlag) {
        return new Phase(name, readFlag, writeFlag);
    }

    /**
     * 类节点遍历函数接口。
     */
    public interface IterateClassNodeFunc {
        /**
         * 遍历所有类节点并执行Pass。
         * @param context Pass上下文
         * @param rflag 读取标志
         * @param wflag 写入标志
         * @param passes Pass列表
         */
        void iterate(@NotNull PassContext context,
                     int rflag,
                     int wflag, List<AbstractPass> passes);
    }
}
