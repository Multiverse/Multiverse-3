package com.mvplugin.core;

import org.junit.Test;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import static org.junit.Assert.*;

public class WorldPropertiesTest extends BukkitMultiverseTest {

    @Test
    public void testSpawnLocationChange() throws Exception {
        MultiverseWorld world = plugin.getWorldManager().getWorld("world");
        assertNotNull(world);
        FacingCoordinates originalLoc = world.getSpawnLocation();
        FacingCoordinates newLocation = Locations.getFacingCoordinates(5, 5, 5, 0, 0);
        assertFalse(originalLoc.equals(newLocation));
        world.setSpawnLocation(newLocation);
        assertEquals(newLocation, world.getSpawnLocation());
        plugin.getWorldManager().saveWorld(world);
        reloadServer();
        world = plugin.getWorldManager().getWorld("world");
        assertNotNull(world);
        assertEquals(newLocation, world.getSpawnLocation());
    }
}
