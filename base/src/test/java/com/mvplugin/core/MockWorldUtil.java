package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.*;

public class MockWorldUtil {

    public static WorldUtil<MultiverseWorld> getMockedWorldUtil() throws WorldCreationException {
        WorldUtil<MultiverseWorld> worldUtil = PowerMockito.mock(WorldUtil.class);

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
