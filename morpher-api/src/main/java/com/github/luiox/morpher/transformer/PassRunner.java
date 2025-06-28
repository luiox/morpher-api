package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 新的Pass运行器，以Phase或者Pipline为单位
 */
public class PassRunner {
    private static final Logger logger = LoggerFactory.getLogger(PassRunner.class);

    // Phase或者Pipline
    private List<Object> objects = new LinkedList<>();

    public PassRunner add(Phase phase) {
        objects.add(phase);
        return this;
    }

    public PassRunner add(Phase... phases) {
        objects.addAll(Arrays.asList(phases));
        return this;
    }

    public PassRunner add(Pipeline pipline) {
        objects.add(pipline);
        return this;
    }

    public PassRunner add(Pipeline... pipelines) {
        objects.addAll(Arrays.asList(pipelines));
        return this;
    }

    public void transform(NewPassContext context) {
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
