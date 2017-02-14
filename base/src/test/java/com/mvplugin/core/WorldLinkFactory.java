package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class WorldLinkFactory {

    public static WorldLink getMockedWorldLink(@NotNull final String name, @NotNull final WorldEnvironment env, @NotNull final WorldType type, long seed) {
        final WorldLink worldLink = PowerMockito.mock(WorldLink.class);
        when(worldLink.getName()).thenReturn(name);
        when(worldLink.getEnvironment()).thenReturn(env);
        when(worldLink.getType()).thenReturn(type);
        when(worldLink.getSpawnLocation()).thenReturn(Locations.NULL_FACING);
        when(worldLink.getSeed()).thenReturn(seed);
        when(worldLink.getUID()).thenReturn(UUID.nameUUIDFromBytes(name.getBytes()));

        WorldLinkData data = new WorldLinkData();

        when(worldLink.getSpawnLocation()).thenAnswer(new Answer<FacingCoordinates>() {
            @Override
            public FacingCoordinates answer(InvocationOnMock invocationOnMock) throws Throwable {
                return data.spawnLocation;
            }
        });

        return worldLink;
    }

    private static class WorldLinkData {
        private FacingCoordinates spawnLocation = Locations.getFacingCoordinates(0.5, 0, 0.5, 0, 0);
    }
}
