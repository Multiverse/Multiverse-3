package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.MultiverseTest;
import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.WorldManager;
import com.mvplugin.core.WorldManagerFactory;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.exceptions.WorldManagementException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest(WorldDestination.class)
public class WorldDestinationTest extends MultiverseTest {
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
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        worldManager = WorldManagerFactory.getMockedWorldManager();
        //worldManager = mock(WorldManager.class);
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
