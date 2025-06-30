package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 新的Pass运行器，以Phase或者Pipeline为单位组织和执行转换流程。
 * 支持批量添加Phase或Pipeline，并统一执行。
 */
public class PassRunner {
    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(PassRunner.class);

    /**
     * 存储Phase或Pipeline对象的列表
     */
    private List<Object> objects = new LinkedList<>();

    /**
     * 添加一个Phase到运行器。
     *
     * @param phase 阶段对象
     * @return 当前PassRunner实例
     */
    public PassRunner add(Phase phase) {
        objects.add(phase);
        return this;
    }

    /**
     * 批量添加Phase到运行器。
     *
     * @param phases 多个阶段对象
     * @return 当前PassRunner实例
     */
    public PassRunner add(Phase... phases) {
        objects.addAll(Arrays.asList(phases));
        return this;
    }

    /**
     * 添加一个Pipeline到运行器。
     *
     * @param pipline 流程对象
     * @return 当前PassRunner实例
     */
    public PassRunner add(Pipeline pipline) {
        objects.add(pipline);
        return this;
    }

    /**
     * 批量添加Pipeline到运行器。
     *
     * @param pipelines 多个流程对象
     * @return 当前PassRunner实例
     */
    public PassRunner add(Pipeline... pipelines) {
        objects.addAll(Arrays.asList(pipelines));
        return this;
    }

    /**
     * 执行所有已添加的Phase或Pipeline。
     *
     * @param context Pass上下文
     */
    public void transform(@NotNull IPassContext context) {
        for (Object object : objects) {
            if (object instanceof Phase phase) {
                System.out.println("-".repeat(120));
                var time = TimeUtil.runBlocking(() -> phase.runPhase(context));
                logger.info("{} took {} ms.", phase.name, time);
            } else if (object instanceof Pipeline pipeline) {
                var time = TimeUtil.runBlocking(() -> pipeline.runPipeLine(context));
                logger.info("{} took {} ms.", pipeline.name, time);
            }
        }
    }
}
