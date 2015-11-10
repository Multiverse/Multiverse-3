package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.exceptions.TeleportException;
import org.junit.Before;
import org.junit.Test;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CannonDestinationTest {

    private CannonDestination.Factory factory;
    protected MultiverseCoreAPI api;

    private List<String> validDestinations = Arrays.asList(
            "cannon:someworld:5:5.3:5.4:3:3:4",
            "cannon:5",
            "ca:someworld:5:5.3:5.4:3:3:4",
            "ca:5"
    );

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        factory = new CannonDestination.Factory();
    }

    @Test
    public void testValidDestinationStrings() throws Exception {
        for (String validDestination : validDestinations) {
            assertEquals(CannonDestination.class, factory.createDestination(api, validDestination).getClass());
        }
    }

    @Test
    public void testGetSpecificDestination() throws Exception {
        assertNotNull(factory.createDestination(api, "cannon:someworld:5:5.3:5.4:3:3:4").getDestination());
    }

    @Test(expected = TeleportException.class)
    public void testGetLaunchDestination() throws Exception {
        CannonDestination dest = factory.createDestination(api, "cannon:5");
        dest.getDestination();
    }

    @Test
    public void testEquals() throws Exception {
        CannonDestination a = new CannonDestination(api, null, 5);
        CannonDestination b = new CannonDestination(api, null, 5);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
        b = new CannonDestination(api, null, 4);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
        b = new CannonDestination(api, Locations.getEntityCoordinates("someworld", 0, 0, 0, 0, 0), 5);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
        a = new CannonDestination(api, Locations.getEntityCoordinates("someworld", 0, 0, 0, 0, 0), 5);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
    }

    @Test
    public void testTeleportLocation() throws Exception {
        BasePlayer player = api.getServerInterface().getPlayer("Player");
        assertNotNull(player);
        // Location must be middle of block since our safe teleporter does this
        CannonDestination dest = new CannonDestination(api, Locations.getEntityCoordinates("someworld", 50.5, 50, 50.5, 0, 0), 5);
        assertNotEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
        dest.teleport(player, (Entity) player);
        assertEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(dest.getLaunchSpeed(), ((Entity) player).getVelocity().length(), 0.00001);
    }

    @Test
    public void testTeleportLaunch() throws Exception {
        BasePlayer player = api.getServerInterface().getPlayer("Player");
        assertNotNull(player);
        CannonDestination dest = new CannonDestination(api, null, 5);
        EntityCoordinates originalCoords = ((Entity) player).getLocation();
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
        dest.teleport(player, (Entity) player);
        assertEquals(originalCoords, ((Entity) player).getLocation());
        assertEquals(dest.getLaunchSpeed(), ((Entity) player).getVelocity().length(), 0.00001);
    }
}
