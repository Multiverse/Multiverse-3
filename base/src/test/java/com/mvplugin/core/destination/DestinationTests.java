package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Vector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DestinationTests {
    private static final String TEST_WORLD = "world";
    private static final EntityCoordinates TEST_LOCATION =
            Locations.getEntityCoordinates(TEST_WORLD, -1.0, -10, 3.1415926536, 1.337f, 0f);
    private static final Vector TEST_VECTOR = new Vector(1.001, 1.002, 1.003);

    private static final Object[][] params = {
            { new EntityCoordinatesDestination(TEST_LOCATION), new EntityCoordinatesDestination() },
            { new CannonDestination(TEST_LOCATION, TEST_VECTOR), new CannonDestination() },
            { new WorldDestination(TEST_WORLD, false), new WorldDestination() },
            { new PlayerDestination("multiverse-team"), new PlayerDestination() },
    };

    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(params);
    }

    private Destination valid;
    private Destination parsed;

    public DestinationTests(Destination valid, Destination parsed) {
        this.valid = valid;
        this.parsed = parsed;
    }

    @Test
    public void testSerialization() throws Exception {
        assertNotEquals(valid, parsed);
        assertTrue(parsed.tryParse(valid.serialize()));
        assertEquals(valid, parsed);
    }

    @Test
    public void testInvalid() throws Exception {
        assertFalse(parsed.tryParse("invalid"));
    }
}
