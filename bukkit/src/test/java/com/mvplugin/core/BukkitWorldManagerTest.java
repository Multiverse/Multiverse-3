package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BukkitWorldManagerTest extends BukkitMultiverseTest {

    MultiverseCoreAPI api;

    @Before
    public void setup() throws Exception {
        api = BukkitAPIFactory.getAPI();
        assertNotNull(api);
        assertNotNull(api.getWorldManager());
    }

    @Test
    public void testAutoLoadWorlds() throws Exception {
        assertFalse(api.getWorldManager().isLoaded("gargamel"));
        assertFalse(api.getWorldManager().isManaged("gargamel"));

        MultiverseWorld gargamel = api.getWorldManager().addWorld("gargamel", WorldEnvironment.NORMAL, null, null, null, null);
        assertNotNull(gargamel);
        assertTrue(api.getWorldManager().isLoaded("gargamel"));
        assertTrue(api.getWorldManager().isManaged("gargamel"));
        gargamel.setAutoLoad(false);
        api.getWorldManager().saveWorld(gargamel);

        api = BukkitAPIFactory.getAPI();
        assertFalse(api.getWorldManager().isLoaded("gargamel"));
        assertTrue(api.getWorldManager().isManaged("gargamel"));
    }
}
