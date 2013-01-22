package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorldManagerTest {

    private WorldManager worldManager;
    private MultiverseCoreAPI coreApi;
    private WorldManagerUtil worldManagerUtil;

    // Set up some test values...
    final String testName = "test";
    final WorldEnvironment testWorldEnvironment = WorldEnvironment.NETHER;
    final String testSeedString = "testseed";
    final Long testSeed = new WorldCreationSettings(testName).seed(testSeedString).seed();
    final WorldType testWorldType = WorldType.FLAT;
    final boolean testGenerateStructures = false;
    final String testGenerator = "testgenerator";
    final boolean testAdjustSpawn = false;

    @Before
    public void setUp() throws Exception {
        coreApi = PowerMockito.mock(MultiverseCoreAPI.class);
        worldManagerUtil = WorldManagerUtilFactory.getMockedWorldManagerUtil();
        worldManager = new WorldManager(coreApi, worldManagerUtil);
        if (testSeed == null) {
            throw new NullPointerException();
        }
    }

    @After
    public void tearDown() throws Exception {
        worldManager = null;
    }

    @Test
    public void testAddWorld() throws Exception {
        // Create a mock WorldManager to test the addWorld methods that take several parameters and ensure
        // that they are creating a proper WorldCreationSettings object.
        WorldManager mockWorldManager = PowerMockito.spy(new WorldManager(coreApi, WorldManagerUtilFactory.getMockedWorldManagerUtil()));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                WorldCreationSettings s = (WorldCreationSettings) invocation.getArguments()[0];
                assertEquals(testName, s.name());
                assertEquals(testWorldEnvironment, s.env());
                assertEquals(testSeed, s.seed());
                assertEquals(testWorldType, s.type());
                assertEquals(testGenerateStructures, s.generateStructures());
                assertEquals(testGenerator, s.generator());
                assertEquals(testAdjustSpawn, s.adjustSpawn());
                return worldManagerUtil.createWorld(s);
            }
        }).when(mockWorldManager).addWorld(any(WorldCreationSettings.class));
        mockWorldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);

        // Now test the real WorldManager to ensure the returned mock world contains the correct values
        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertEquals(testName, w.getName());
        assertNotEquals(testName.toUpperCase(), w.getName());
        assertEquals(testWorldEnvironment, w.getEnvironment());
        assertEquals(testSeed.longValue(), w.getSeed());
        assertEquals(testWorldType, w.getWorldType());
        assertEquals(testGenerator, w.getGenerator());
        assertEquals(testAdjustSpawn, w.getAdjustSpawn());

        boolean thrown = false;
        try {
            worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                    testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        } catch (WorldCreationException e) {
            thrown = true;
            assertEquals(Language.WORLD_ALREADY_EXISTS, e.getBundledMessage().getMessage());
            assertEquals(testName, e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
        thrown = false;
        try {
            worldManager.addWorld(testName.toUpperCase(), testWorldEnvironment, testSeedString,
                    testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        } catch (WorldCreationException e) {
            thrown = true;
            assertEquals(Language.WORLD_ALREADY_EXISTS, e.getBundledMessage().getMessage());
            assertEquals(testName.toUpperCase(), e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
    }

    @Test
    public void testIsLoaded() throws Exception {
        // Not a whole lot testable with this method.
        assertTrue(worldManager.isLoaded("world"));
        assertFalse(worldManager.isLoaded("world1"));
        assertTrue(worldManager.isLoaded("WORLD"));
        assertTrue(worldManager.isLoaded("WOrLd"));
        assertTrue(worldManager.isLoaded("world_nether"));
        assertTrue(worldManager.isLoaded("world_the_end"));
        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertTrue(worldManager.isLoaded(w.getName()));
        worldManager.unloadWorld(w);
        assertFalse(worldManager.isLoaded(w.getName()));
        assertFalse(worldManager.isLoaded(w.getName().toUpperCase()));
    }

    @Test
    public void testIsManaged() throws Exception {
        assertTrue(worldManager.isManaged("world"));
        assertFalse(worldManager.isManaged("world1"));
        assertTrue(worldManager.isManaged("WORLD"));
        assertTrue(worldManager.isManaged("WOrLd"));
        assertTrue(worldManager.isManaged("world_nether"));
        assertTrue(worldManager.isManaged("world_the_end"));
        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertTrue(worldManager.isManaged(w.getName()));
        worldManager.unloadWorld(w);
        assertTrue(worldManager.isManaged(w.getName()));
        worldManager.removeWorld(w.getName());
        assertFalse(worldManager.isManaged(w.getName()));
        w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertTrue(worldManager.isManaged(w.getName()));
    }

    @Test
    public void testGetWorld() throws Exception {
        assertNotNull(worldManager.getWorld("world"));
        assertNotNull(worldManager.getWorld("WORLD"));
        assertNotNull(worldManager.getWorld("woRlD"));
        assertNull(worldManager.getWorld("world1"));
        assertNull(worldManager.getWorld(testName));
        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertNotNull(worldManager.getWorld(testName));
        assertEquals(w, worldManager.getWorld(testName));
    }

    @Test
    public void testGetWorlds() throws Exception {
        assertEquals(3, worldManager.getWorlds().size());
        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        assertTrue(worldManager.getWorlds().contains(w));
        worldManager.unloadWorld(w);
        assertFalse(worldManager.getWorlds().contains(w));
        worldManager.loadWorld(w.getName());
        assertTrue(worldManager.getWorlds().contains(w));
        assertEquals(4, worldManager.getWorlds().size());
    }

    @Test
    public void testLoadWorld() throws Exception {
        boolean thrown = false;
        try {
            worldManager.loadWorld("world");
        } catch (WorldManagementException e) {
            thrown = true;
            assertEquals(Language.WORLD_ALREADY_LOADED, e.getBundledMessage().getMessage());
            assertEquals("world", e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
        thrown = false;
        try {
            worldManager.loadWorld("WoRlD");
        } catch (WorldManagementException e) {
            thrown = true;
            assertEquals(Language.WORLD_ALREADY_LOADED, e.getBundledMessage().getMessage());
            assertEquals("WoRlD", e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
        thrown = false;
        try {
            worldManager.loadWorld(testName);
        } catch (WorldManagementException e) {
            thrown = true;
            assertEquals(Language.WORLD_NOT_MANAGED, e.getBundledMessage().getMessage());
            assertEquals(testName, e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
        thrown = false;

        MultiverseWorld w = worldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        try {
            worldManager.loadWorld(testName);
        } catch (WorldManagementException e) {
            thrown = true;
            assertEquals(Language.WORLD_ALREADY_LOADED, e.getBundledMessage().getMessage());
            assertEquals(testName, e.getBundledMessage().getArgs()[0]);
        }
        assertTrue(thrown);
        thrown = false;
        worldManager.unloadWorld(testName);
        assertFalse(worldManager.isLoaded(testName));
        worldManager.loadWorld(testName);
        MultiverseWorld w1 = worldManager.getWorld(testName);
        assertNotNull(w1);
        assertNotSame(w1, w);
        assertEquals(testName, w1.getName());

        /*
        // Create a mock WorldManager to test the addWorld methods that take several parameters and ensure
        // that they are creating a proper WorldCreationSettings object.
        WorldManager mockWorldManager = PowerMockito.spy(new WorldManager(coreApi, WorldManagerUtilFactory.getMockedWorldManagerUtil()));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                WorldCreationSettings s = (WorldCreationSettings) invocation.getArguments()[0];
                assertEquals(testName, s.name());
                assertEquals(testWorldEnvironment, s.env());
                assertEquals(testSeed, s.seed());
                assertEquals(testWorldType, s.type());
                assertEquals(testGenerateStructures, s.generateStructures());
                assertEquals(testGenerator, s.generator());
                assertEquals(testAdjustSpawn, s.adjustSpawn());
                return null;
            }
        }).when(mockWorldManager).addWorld(any(WorldCreationSettings.class));
        mockWorldManager.addWorld(testName, testWorldEnvironment, testSeedString,
                testWorldType, testGenerateStructures, testGenerator, testAdjustSpawn);
        mockWorldManager.unloadWorld(testName);
        mockWorldManager.loadWorld(testName);
        */
    }

    @Test
    public void testUnloadWorld() throws Exception {

    }

    @Test
    public void testRemovePlayersFromWorld() throws Exception {

    }

    @Test
    public void testGetUnloadedWorlds() throws Exception {

    }

    @Test
    public void testRemoveWorld() throws Exception {

    }

    @Test
    public void testDeleteWorld() throws Exception {

    }
}
