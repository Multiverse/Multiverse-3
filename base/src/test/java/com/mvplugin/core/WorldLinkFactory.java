package com.mvplugin.core;

import org.jetbrains.annotations.NotNull;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.when;

public class WorldLinkFactory {

    public static WorldLink getWorldLink(@NotNull final String name) {
        final WorldLink worldLink = PowerMockito.mock(WorldLink.class);
        when(worldLink.getName()).thenReturn(name);
        return worldLink;
    }
}
