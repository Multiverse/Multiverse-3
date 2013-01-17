package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import org.jetbrains.annotations.NotNull;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.when;

public class WorldLinkFactory {

    public static WorldLink getWorldLink(@NotNull final String name, @NotNull final WorldEnvironment env) {
        final WorldLink worldLink = PowerMockito.mock(WorldLink.class);
        when(worldLink.getName()).thenReturn(name);
        when(worldLink.getEnvironment()).thenReturn(env);
        return worldLink;
    }
}
