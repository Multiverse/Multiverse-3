package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.properties.MemoryProperties;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class WorldManagerUtilFactory {

    public static WorldManagerUtil getMockedWorldManagerUtil() throws WorldCreationException, PluginBaseException {
        final WorldManagerUtil worldManagerUtil = PowerMockito.mock(WorldManagerUtil.class);
        final List<String> managedWorlds = new ArrayList<String>();

        // Mock getSafeWorldName
        when(worldManagerUtil.getSafeWorldName()).thenReturn("world");

        // Mock unloadWorldFromServer
        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(final InvocationOnMock invocation) throws Throwable {
                String name = ((MultiverseWorld) invocation.getArguments()[0]).getName();
                return !name.equalsIgnoreCase("world")
                        && !name.equalsIgnoreCase("world_nether")
                        && !name.equalsIgnoreCase("world_the_end");
            }
        }).when(worldManagerUtil).unloadWorldFromServer(any(MultiverseWorld.class));

        // Mock getManagedWorldNames
        doReturn(managedWorlds).when(worldManagerUtil).getManagedWorldNames();

        // Mock getWorldProperties
        doAnswer(new Answer<WorldProperties>() {
            @Override
            public WorldProperties answer(final InvocationOnMock invocation) throws Throwable {
                String name = invocation.getArguments()[0].toString();
                return new DefaultWorldProperties(MemoryProperties.newMemoryProperties(Logging.getLogger(), WorldProperties.class));
            }
        }).when(worldManagerUtil).getWorldProperties(anyString());

        // Mock removeWorldProperties
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
                MultiverseWorld world = MultiverseWorldFactory.newMultiverseWorld(worldManagerUtil, s);
                managedWorlds.add(s.name());
                return world;
            }
        }).when(worldManagerUtil).createWorld(any(WorldCreationSettings.class));


        // Mock getInitialWorlds
        List<MultiverseWorld> defaultWorlds = MultiverseWorldFactory.getDefaultWorlds(worldManagerUtil);
        Map<String, MultiverseWorld> initialWorlds = new HashMap<String, MultiverseWorld>(defaultWorlds.size());
        for (final MultiverseWorld world : defaultWorlds) {
            initialWorlds.put(world.getName().toLowerCase(), world);
            managedWorlds.add(world.getName().toLowerCase());
        }
        when(worldManagerUtil.getInitialWorlds()).thenReturn(initialWorlds);

        return worldManagerUtil;
    }


}
