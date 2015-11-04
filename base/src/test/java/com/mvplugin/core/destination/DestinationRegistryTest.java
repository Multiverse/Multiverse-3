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
        TestDestination.PREFIXES.forEach(x -> {assertNull(registry.getDestinationFactory(x));});
        registry.registerDestinationFactory(new TestDestination.Factory());
        TestDestination.PREFIXES.forEach(x -> {assertNotNull(registry.getDestinationFactory(x));});
    }

    @Test
    public void testGetDefaultDestinationFactories() {
        CannonDestination.PREFIXES.forEach(x -> {assertNotNull(registry.getDestinationFactory(x));});
        ExactDestination.PREFIXES.stream().forEach(x -> {assertNotNull(registry.getDestinationFactory(x));});
        PlayerDestination.PREFIXES.forEach(x -> {assertNotNull(registry.getDestinationFactory(x));});
        WorldDestination.PREFIXES.forEach(x -> {assertNotNull(registry.getDestinationFactory(x));});
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