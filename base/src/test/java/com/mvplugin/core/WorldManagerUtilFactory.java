package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class WorldManagerUtilFactory {

    public static WorldManagerUtil getMockedWorldManagerUtil() throws WorldCreationException {
        WorldManagerUtil worldManagerUtil = PowerMockito.mock(WorldManagerUtil.class);

        // Mock getInitialWorlds
        Map<String, MultiverseWorld> initialWorlds = new HashMap<String, MultiverseWorld>(3);
        MultiverseWorld mockWorld = PowerMockito.mock(MultiverseWorld.class);
        when(mockWorld.getName()).thenReturn("world");
        initialWorlds.put(mockWorld.getName(), mockWorld);
        mockWorld = PowerMockito.mock(MultiverseWorld.class);
        when(mockWorld.getName()).thenReturn("world_nether");
        initialWorlds.put(mockWorld.getName(), mockWorld);
        mockWorld = PowerMockito.mock(MultiverseWorld.class);
        when(mockWorld.getName()).thenReturn("world_the_end");
        initialWorlds.put(mockWorld.getName(), mockWorld);
        when(worldManagerUtil.getInitialWorlds()).thenReturn(initialWorlds);

        // Mock getSafeWorldName
        when(worldManagerUtil.getSafeWorldName()).thenReturn("world");

        // Mock createWorld
        doAnswer(new Answer<MultiverseWorld>() {
            @Override
            public MultiverseWorld answer(final InvocationOnMock invocation) throws Throwable {
                WorldCreationSettings s = (WorldCreationSettings) invocation.getArguments()[0];
                MultiverseWorld world = PowerMockito.mock(MultiverseWorld.class);
                when(world.getName()).thenReturn(s.name());
                when(world.getEnvironment()).thenReturn(s.env());
                when(world.getSeed()).thenReturn(s.seed());
                when(world.getWorldType()).thenReturn(s.type());
                when(world.getGenerator()).thenReturn(s.generator());
                when(world.getAdjustSpawn()).thenReturn(s.adjustSpawn());
                return world;
            }
        }).when(worldManagerUtil).createWorld(any(WorldCreationSettings.class));

        return worldManagerUtil;
    }


}
