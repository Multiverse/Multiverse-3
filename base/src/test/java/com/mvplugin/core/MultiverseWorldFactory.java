package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.world.WorldCreationSettings;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.PluginBaseException;

import java.util.Arrays;
import java.util.List;

public class MultiverseWorldFactory {

    public static List<MultiverseWorld> getDefaultWorlds(@NotNull final WorldManagerUtil worldManagerUtil) throws PluginBaseException {
        return Arrays.asList(newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world")),
                newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world_nether").env(WorldEnvironment.NETHER)),
                newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world_the_end").env(WorldEnvironment.THE_END)));
    }

    public static MultiverseWorld newMultiverseWorld(@NotNull final WorldManagerUtil worldManagerUtil,
                                                     @NotNull final WorldCreationSettings s) throws PluginBaseException {
        final WorldProperties properties = worldManagerUtil.getWorldProperties(s.name());
        WorldEnvironment env = s.env();
        Long seed = s.seed();
        seed = seed == null ? 0L : seed;
        WorldType type = s.type();
        properties.setSeed(seed);
        properties.setGenerator(s.generator());
        final WorldLink worldLink = WorldLinkFactory.getMockedWorldLink(s.name(), env == null ? WorldEnvironment.NORMAL : env, type == null ? WorldType.NORMAL : type, seed);
        return new MultiverseWorld(properties, worldLink);
    }
}
