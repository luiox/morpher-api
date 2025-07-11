package com.github.luiox.passes.deobfuscate;

import com.github.luiox.morpher.plugin.IPassPlugin;
import com.github.luiox.morpher.plugin.PluginInfo;
import com.github.luiox.morpher.transformer.AbstractPass;
import com.github.luiox.passes.UnusedLabelRemover;
import com.github.luiox.passes.obfuscate.BasicXorStrObf;
import com.github.luiox.passes.optimize.DeadCodeRemover;

import java.util.List;

@PluginInfo(name = "GruntDeobfPlugin")
public class GruntDeobfPlugin implements IPassPlugin {

    @Override
    public void onInitialize() {

    }

    @Override
    public List<AbstractPass> getAvailablePasses() {
        return List.of(
//                new GruntConstantFolder(),
                new DeadCodeRemover(),
                new BasicXorStrObf(),
                new UnusedLabelRemover()
        );
    }
}
