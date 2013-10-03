package com.mvplugin.core;

import com.mvplugin.core.minecraft.WorldEnvironment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BukkitWorldManagerTest extends BukkitMultiverseTest {

    @Before
    public void setup() throws Exception {
    }

    @Test
    public void testAutoLoadWorlds() throws Exception {
        assertFalse(plugin.getWorldManager().isLoaded("gargamel"));
        assertFalse(plugin.getWorldManager().isManaged("gargamel"));

        MultiverseWorld gargamel = plugin.getWorldManager().addWorld("gargamel", WorldEnvironment.NORMAL, null, null, null, null);
        assertNotNull(gargamel);
        assertTrue(plugin.getWorldManager().isLoaded("gargamel"));
        assertTrue(plugin.getWorldManager().isManaged("gargamel"));
        gargamel.setAutoLoad(false);
        plugin.getWorldManager().saveWorld(gargamel);

        reloadServer();
        assertFalse(plugin.getWorldManager().isLoaded("gargamel"));
        assertTrue(plugin.getWorldManager().isManaged("gargamel"));
    }
}
