package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个Pass处理流程（Pipeline），由多个Phase阶段组成。
 * 用于组织和执行一系列代码转换或处理阶段。
 */
public class Pipeline {
    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    /** 流程名称 */
    public final String name;

    /** 阶段列表 */
    private final List<Phase> phases = new ArrayList<>();

    /**
     * 构造一个Pipeline实例。
     * @param name 流程名称
     */
    public Pipeline(String name) {
        this.name = name;
    }

    /**
     * 添加一个阶段到流程中。
     * @param phase 阶段对象
     * @return 当前Pipeline实例，便于链式调用
     */
    public @NotNull Pipeline add(@NotNull Phase phase) {
        phases.add(phase);
        return this;
    }

    /**
     * 获取所有阶段列表。
     * @return 阶段列表
     */
    public @NotNull List<Phase> getPhases() {
        return phases;
    }

    /**
     * 静态工厂方法，创建一个Pipeline实例。
     * @param name 流程名称
     * @return Pipeline实例
     */
    public static @NotNull Pipeline of(String name) {
        return new Pipeline(name);
    }

    /**
     * 依次执行所有阶段。
     * @param context Pass上下文
     */
    public void runPipeLine(IPassContext context) {
        for (var phase : phases) {
            System.out.println("-".repeat(120));
            var time = TimeUtil.runBlocking(() -> phase.runPhase(context));
            logger.info("{} took {} ms.", phase.name, time);
        }
    }
}
