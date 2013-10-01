package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;
import org.powermock.api.mockito.PowerMockito;
import pluginbase.minecraft.location.Locations;

import static org.mockito.Mockito.*;

public class WorldLinkFactory {

    public static WorldLink getMockedWorldLink(@NotNull final String name, @NotNull final WorldEnvironment env, @NotNull final WorldType type, long seed) {
        final WorldLink worldLink = PowerMockito.mock(WorldLink.class);
        when(worldLink.getName()).thenReturn(name);
        when(worldLink.getEnvironment()).thenReturn(env);
        when(worldLink.getType()).thenReturn(type);
        when(worldLink.getSpawnLocation()).thenReturn(Locations.NULL_FACING);
        when(worldLink.getSeed()).thenReturn(seed);

        return worldLink;
    }
}
