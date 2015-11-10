package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.MultiverseTest;
import org.junit.Before;
import org.junit.Test;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.Locations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WorldDestinationTest extends MultiverseTest {

    private WorldDestination.Factory factory;
    protected MultiverseCoreAPI api;

    private List<String> validDestinations = Arrays.asList(
            "someworld",
            "world:someworld",
            "w:someworld"
    );

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        factory = new WorldDestination.Factory();
    }

    @Test
    public void testValidDestinationStrings() throws Exception {
        for (String validDestination : validDestinations) {
            assertEquals(WorldDestination.class, factory.createDestination(api, validDestination).getClass());
        }
    }

    @Test
    public void testGetDestination() throws Exception {
        WorldDestination dest = factory.createDestination(api, "world:world");
        assertNotNull(dest.getDestination());
        // TODO: PluginBase might need some changes to allow FacingCoordinates to equal EntityCoordinates
        assertTrue(Locations.equal(api.getWorldManager().getWorld("world").getSpawnLocation(), dest.getDestination()));
    }

    @Test
    public void testEquals() throws Exception {
        WorldDestination a = new WorldDestination(api, "world");
        WorldDestination b = new WorldDestination(api, "world");
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
        b = new WorldDestination(api, "someworld");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));

        // TODO: should case matter?
    }

    @Test
    public void testTeleportLocation() throws Exception {
        BasePlayer player = api.getServerInterface().getPlayer("Player");
        assertNotNull(player);
        // Location must be middle of block since our safe teleporter does this
        ((Entity) player).teleport(Locations.getEntityCoordinates("someworld", 50.5, 50, 50.5, 0, 0));

        WorldDestination dest = new WorldDestination(api, "world");
        assertNotEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
        dest.teleport(player, (Entity) player);
        assertEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
    }
}
