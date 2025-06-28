package com.github.luiox.morpher.transformer;

import com.github.luiox.morpher.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    public final String name;

    private final List<Phase> phases = new ArrayList<>();

    public Pipeline(String name) {
        this.name = name;
    }


    public @NotNull Pipeline add(@NotNull Phase phase) {
        phases.add(phase);
        return this;
    }

    public @NotNull List<Phase> getPhases() {
        return phases;
    }

    public static @NotNull Pipeline of(String name) {
        return new Pipeline(name);
    }

    public void runPipeLine(IPassContext context) {
        for (var phase : phases) {
            System.out.println("-".repeat(120));
            var time = TimeUtil.runBlocking(() -> phase.runPhase(context));
            logger.info("{} took {} ms.", phase.name, time);
        }
    }
}
