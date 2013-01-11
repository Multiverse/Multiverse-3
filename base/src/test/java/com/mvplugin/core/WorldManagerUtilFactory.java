package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class WorldManagerUtilFactory {

    public static WorldManagerUtil getMockedWorldManagerUtil() throws WorldCreationException, IOException {
        WorldManagerUtil worldManagerUtil = PowerMockito.mock(WorldManagerUtil.class);
        final List<String> managedWorlds = new ArrayList<String>();

        // Mock getInitialWorlds
        List<MultiverseWorld> defaultWorlds = MultiverseWorldFactory.getDefaultWorlds();
        Map<String, MultiverseWorld> initialWorlds = new HashMap<String, MultiverseWorld>(defaultWorlds.size());
        for (final MultiverseWorld world : defaultWorlds) {
            initialWorlds.put(world.getName().toLowerCase(), world);
            managedWorlds.add(world.getName().toLowerCase());
        }
        when(worldManagerUtil.getInitialWorlds()).thenReturn(initialWorlds);

        // Mock getSafeWorldName
        when(worldManagerUtil.getSafeWorldName()).thenReturn("world");

        // Mock unloadWorldFromServer
        doReturn(true).when(worldManagerUtil).unloadWorldFromServer(any(MultiverseWorld.class));

        // Mock getManagedWorldNames()
        doReturn(managedWorlds).when(worldManagerUtil).getManagedWorldNames();

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                String name = invocation.getArguments()[0].toString();
                for (final String managedWorld : managedWorlds) {
                    if (name.equalsIgnoreCase(managedWorld)) {
                        name = managedWorld;
                        break;
                    }
                }
                managedWorlds.remove(name);
                return null;
            }
        }).when(worldManagerUtil).removeWorldProperties(anyString());

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
                managedWorlds.add(s.name());
                return world;
            }
        }).when(worldManagerUtil).createWorld(any(WorldCreationSettings.class));

        return worldManagerUtil;
    }


}
