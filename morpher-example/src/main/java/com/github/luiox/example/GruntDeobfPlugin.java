package com.github.luiox.example;

import com.github.luiox.morpher.plugin.IPassPlugin;
import com.github.luiox.morpher.plugin.PluginInfo;
import com.github.luiox.morpher.transformer.AbstractPass;

import java.util.List;

@PluginInfo(name = "GruntDeobfPlugin")
public class GruntDeobfPlugin implements IPassPlugin {

    @Override
    public void onInitialize() {

    }

    @Override
    public List<AbstractPass> getAvailablePasses() {
        return List.of(new GruntConstantFolder());
    }
}
