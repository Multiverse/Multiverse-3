package com.mvplugin.core;

import org.bukkit.Bukkit;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({Bukkit.class})
public class BukkitMultiverseTest extends MultiverseTest {

    @Before
    public void initialize() throws Exception {
        // ensure static initialization
        new MultiverseCoreBukkitPlugin();
    }
}
