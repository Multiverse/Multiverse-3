package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import org.junit.Before;
import org.junit.Test;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.Locations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ExactDestinationTest {

    private ExactDestination.Factory factory;
    protected MultiverseCoreAPI api;

    private List<String> validDestinations = Arrays.asList(
            "exact:someworld:5:5.3:5.4:3:3",
            "e:someworld:5:5.3:5.4:3:3",
            "location:someworld:5:5.3:5.4:3:3",
            "loc:someworld:5:5.3:5.4:3:3",
            "l:someworld:5:5.3:5.4:3:3",
            "coordinates:someworld:5:5.3:5.4:3:3",
            "coords:someworld:5:5.3:5.4:3:3",
            "exact:someworld:5:5.3:5.4",
            "e:someworld:5:5.3:5.4",
            "location:someworld:5:5.3:5.4",
            "loc:someworld:5:5.3:5.4",
            "l:someworld:5:5.3:5.4",
            "coordinates:someworld:5:5.3:5.4",
            "coords:someworld:5:5.3:5.4"
    );

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        factory = new ExactDestination.Factory();
    }

    @Test
    public void testValidDestinationStrings() throws Exception {
        for (String validDestination : validDestinations) {
            ExactDestination dest = factory.createDestination(api, validDestination);
            assertEquals(ExactDestination.class, dest.getClass());
            assertNotNull(dest.getDestination());
        }
    }

    @Test
    public void testEquals() throws Exception {
        ExactDestination a = new ExactDestination(api, Locations.getEntityCoordinates("someworld", 50.5, 50, 50.5, 0, 0));
        ExactDestination b = new ExactDestination(api, Locations.getEntityCoordinates("someworld", 50.5, 50, 50.5, 0, 0));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
        b = new ExactDestination(api, Locations.getEntityCoordinates("someworld", 50, 50, 50.5, 0, 0));
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
        a = new ExactDestination(api, Locations.getEntityCoordinates("someworld", 50, 50, 50.5, 0, 0));
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
        ExactDestination dest = new ExactDestination(api, Locations.getEntityCoordinates("someworld", 50.5, 50, 50.5, 0, 0));
        assertNotEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
        dest.teleport(player, (Entity) player);
        assertEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
    }
}
