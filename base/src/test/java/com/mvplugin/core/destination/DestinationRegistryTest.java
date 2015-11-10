package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationRegistryTest {

    protected MultiverseCoreAPI api;
    protected DestinationRegistry registry;

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        registry = new DestinationRegistry(api);
    }

    @Test
    public void testRegisterDestinationFactory() throws Exception {
        for (String prefix : TestDestination.PREFIXES) {
            assertNull(registry.getDestinationFactory(prefix));
        }
        registry.registerDestinationFactory(new TestDestination.Factory());
        for (String prefix : TestDestination.PREFIXES) {
            assertNotNull(registry.getDestinationFactory(prefix));
        }
    }

    @Test
    public void testGetDefaultDestinationFactories() {
        for (String prefix : CannonDestination.PREFIXES) {
            assertNotNull(registry.getDestinationFactory(prefix));
        }
        for (String prefix : ExactDestination.PREFIXES) {
            assertNotNull(registry.getDestinationFactory(prefix));
        }
        for (String prefix : PlayerDestination.PREFIXES) {
            assertNotNull(registry.getDestinationFactory(prefix));
        }
        for (String prefix : WorldDestination.PREFIXES) {
            assertNotNull(registry.getDestinationFactory(prefix));
        }
    }

    @Test // This is a special case test since World Destination is the only one allowed without a prefix
    public void testParseWorldDestinations() throws Exception {
        Destination dest = registry.parseDestination("someworld");
        assertNotNull(dest);
        assertEquals(WorldDestination.class, dest.getClass());
        assertEquals("world:someworld", dest.getDestinationString());
        dest = registry.parseDestination(":");
        assertNotNull(dest);
        assertEquals(UnknownDestination.class, dest.getClass());
        dest = registry.parseDestination("");
        assertNotNull(dest);
        assertEquals(WorldDestination.class, dest.getClass());
        assertEquals("world:", dest.getDestinationString());
    }

    @Test
    public void testParseInvalidDestinations() throws Exception {
        Destination dest = registry.parseDestination("blafhga:fff");
        assertNotNull(dest);
        assertEquals(UnknownDestination.class, dest.getClass());
        dest = registry.parseDestination(":::::::");
        assertNotNull(dest);
        assertEquals(UnknownDestination.class, dest.getClass());
    }

    @Test
    public void testParseExtraColonsDestinations() throws Exception {
        Destination dest = registry.parseDestination("world:some:world");
        assertNotNull(dest);
        assertEquals(WorldDestination.class, dest.getClass());
        assertEquals("world:some:world", dest.getDestinationString());
    }

    @Test
    public void testGetRegistrationCount() throws Exception {
        int originalCount = registry.getRegistrationCount();
        registry.registerDestinationFactory(new TestDestination.Factory());
        assertEquals(originalCount + TestDestination.PREFIXES.size(), registry.getRegistrationCount());
    }
}