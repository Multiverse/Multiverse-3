package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import pluginbase.minecraft.Entity;
import pluginbase.permission.Permissible;

import static org.junit.Assert.*;

public class DestinationTest {

    protected MultiverseCoreAPI api;
    protected DestinationRegistry registry;

    private Destination basicDestination;

    @Before
    public void setUp() throws Exception {
        api = MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI();
        registry = new DestinationRegistry(api);

        basicDestination = new Destination(api) {
            @Override
            public void teleport(@NotNull Permissible teleporter, @NotNull Permissible teleportee, @NotNull Entity teleporteeEntity) throws TeleportException { }

            @NotNull
            @Override
            protected String getDestinationString() { return ""; }
        };
    }

    @Test
    public void testGetApi() throws Exception {
        assertSame(api, basicDestination.getApi());
    }

    @Test
    public void testGetSafeTeleporter() throws Exception {
        assertSame(api.getSafeTeleporter(), basicDestination.getSafeTeleporter());
    }
}