package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.properties.MemoryProperties;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MultiverseWorldFactory {

    public static List<MultiverseWorld> getDefaultWorlds() {
        return Arrays.asList(newMultiverseWorld("world", WorldEnvironment.NORMAL),
                newMultiverseWorld("world_nether", WorldEnvironment.NETHER),
                newMultiverseWorld("world_the_end", WorldEnvironment.THE_END));
    }

    public static MultiverseWorld newMultiverseWorld(@NotNull final String name, @NotNull WorldEnvironment env) {
        final WorldProperties properties = new DefaultWorldProperties(new MemoryProperties(true, WorldProperties.class));
        final WorldLink worldLink = WorldLinkFactory.getWorldLink(name, env);
        return new DefaultMultiverseWorld(properties, worldLink);
    }
}
