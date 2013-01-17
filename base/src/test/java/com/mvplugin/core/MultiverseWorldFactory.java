package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MultiverseWorldFactory {

    public static List<MultiverseWorld> getDefaultWorlds(@NotNull final WorldManagerUtil worldManagerUtil) throws IOException {
        return Arrays.asList(newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world")),
                newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world_nether").env(WorldEnvironment.NETHER)),
                newMultiverseWorld(worldManagerUtil, new WorldCreationSettings("world_the_end").env(WorldEnvironment.THE_END)));
    }

    public static MultiverseWorld newMultiverseWorld(@NotNull final WorldManagerUtil worldManagerUtil,
                                                     @NotNull final WorldCreationSettings s) throws IOException {
        final WorldProperties properties = worldManagerUtil.getWorldProperties(s.name());
        WorldEnvironment env = s.env();
        Long seed = s.seed();
        WorldType type = s.type();
        properties.set(WorldProperties.SEED, seed == null ? 0L : seed);
        properties.set(WorldProperties.GENERATOR, s.generator());
        final WorldLink worldLink = WorldLinkFactory.getWorldLink(s.name(), env == null ? WorldEnvironment.NORMAL : env, type == null ? WorldType.NORMAL : type);
        return new DefaultMultiverseWorld(properties, worldLink);
    }
}
