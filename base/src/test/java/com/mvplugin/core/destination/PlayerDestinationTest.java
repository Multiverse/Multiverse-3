package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.exceptions.TeleportException;
import org.junit.Before;
import org.junit.Test;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.Locations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerDestinationTest {

    private PlayerDestination.Factory factory;
    protected MultiverseCoreAPI api;

    private List<String> validDestinations = Arrays.asList(
            "player:someplayer",
            "pl:someplayer"
    );

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        factory = new PlayerDestination.Factory();
    }

    @Test
    public void testValidDestinationStrings() throws Exception {
        for (String validDestination : validDestinations) {
            assertEquals(PlayerDestination.class, factory.createDestination(api, validDestination).getClass());
        }
    }

    @Test
    public void testGetOnlinePlayerDestination() throws Exception {
        assertNotNull(factory.createDestination(api, "player:someplayer").getDestination());
    }

    @Test(expected = TeleportException.class)
    public void testGetOfflinePlayerDestination() throws Exception {
        PlayerDestination dest = factory.createDestination(api, "player:fakeplayer");
        dest.getDestination();
    }

    @Test
    public void testEquals() throws Exception {
        PlayerDestination a = new PlayerDestination(api, "player");
        PlayerDestination b = new PlayerDestination(api, "player");
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
        b = new PlayerDestination(api, "player2");
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

        PlayerDestination dest = new PlayerDestination(api, "someplayer");
        assertNotEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
        dest.teleport(player, player, (Entity) player);
        assertEquals(dest.getDestination(), ((Entity) player).getLocation());
        assertEquals(1, ((Entity) player).getVelocity().length(), 0.00001);
    }
}
