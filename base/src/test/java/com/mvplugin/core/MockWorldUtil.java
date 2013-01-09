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

public class MockWorldUtil {

    public static WorldUtil getMockedWorldUtil() throws WorldCreationException {
        WorldUtil worldUtil = PowerMockito.mock(WorldUtil.class);

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
        when(worldUtil.getInitialWorlds()).thenReturn(initialWorlds);

        // Mock getSafeWorldName
        when(worldUtil.getSafeWorldName()).thenReturn("world");

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
        }).when(worldUtil).createWorld(any(WorldCreationSettings.class));

        return worldUtil;
    }


}
