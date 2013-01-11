package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.properties.MemoryProperties;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MultiverseWorldFactory {

    public static List<MultiverseWorld> getDefaultWorlds() {
        return Arrays.asList(newMultiverseWorld("world"),
                newMultiverseWorld("world_nether"),
                newMultiverseWorld("world_the_end"));
    }

    public static MultiverseWorld newMultiverseWorld(@NotNull final String name) {
        final WorldProperties properties = new DefaultworldProperties(new MemoryProperties(true, WorldProperties.class));
        final WorldLink worldLink = WorldLinkFactory.getWorldLink(name);
        return new DefaultMultiverseWorld(properties, worldLink);
    }
}
