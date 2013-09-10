package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.WorldManager;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.world.MultiverseWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorldDestination.class)
public class WorldDestinationTest {
    private static final FacingCoordinates SPAWNPOINT = Locations.getFacingCoordinates(0, 0, 0, 0F, 0F);

    private MultiverseCoreAPI api;
    private WorldManager worldManager;
    private boolean hasLoaded;

    private WorldDestination createWorldDestination(String destString) throws Exception {
        WorldDestination dest = mock(WorldDestination.class);

        when(dest.getDestination()).thenCallRealMethod();
        when(dest.tryParse(anyString())).thenCallRealMethod();
        when(dest.serialize()).thenCallRealMethod();

        when(dest.getApi()).thenReturn(api);
        assertTrue(dest.tryParse(destString));
        assertEquals(destString, dest.serialize());
        return dest;
    }
    @Before
    public void setUp() throws Exception {
        api = mock(MultiverseCoreAPI.class);
        worldManager = mock(WorldManager.class);
        when(api.getWorldManager()).thenReturn(worldManager);
        when(worldManager.getWorld("fail")).thenReturn(null);
        final MultiverseWorld mockLoaded = mock(MultiverseWorld.class);
        when(mockLoaded.getSpawnLocation()).thenReturn(SPAWNPOINT);
        final MultiverseWorld mockUnloaded = mock(MultiverseWorld.class);
        when(mockUnloaded.getSpawnLocation()).thenReturn(SPAWNPOINT);
        when(worldManager.getWorld("loaded")).thenReturn(mockLoaded);
        hasLoaded = false;
        when(worldManager.getWorld("unloaded")).thenAnswer(new Answer<MultiverseWorld>(){
            @Override
            public MultiverseWorld answer(final InvocationOnMock invocationOnMock) throws Throwable {
                return hasLoaded ? mockUnloaded : null;
            }
        });
        when(worldManager.loadWorld("unloaded")).thenAnswer(new Answer<MultiverseWorld>(){
            @Override
            public MultiverseWorld answer(final InvocationOnMock invocationOnMock) throws Throwable {
                hasLoaded = true;
                return worldManager.getWorld("unloaded");
            }
        });
        when(worldManager.loadWorld("fail")).thenThrow(WorldManagementException.class);
    }

    @Test
    public void testLoaded() throws Exception {
        WorldDestination dest = createWorldDestination("world:loaded:true");
        assertEquals(Locations.getEntityCoordinates("loaded", SPAWNPOINT), dest.getDestination());
        verify(worldManager, never()).loadWorld(anyString());
    }

    @Test(expected = TeleportException.class)
    public void testUnloaded() throws Exception {
        WorldDestination dest = createWorldDestination("world:unloaded:false");
        try {
            dest.getDestination();
        } finally {
            verify(worldManager, never()).loadWorld(anyString());
        }
    }

    @Test
    public void testLoading() throws Exception {
        WorldDestination dest = createWorldDestination("world:unloaded:true");
        assertEquals(Locations.getEntityCoordinates("unloaded", SPAWNPOINT), dest.getDestination());
        verify(worldManager).loadWorld("unloaded");
    }

    @Test(expected = TeleportException.class)
    public void testNonexistant() throws Exception {
        WorldDestination dest = createWorldDestination("world:fail:true");
        try {
            dest.getDestination();
        } finally {
            verify(worldManager).loadWorld("fail");
        }
    }
}
