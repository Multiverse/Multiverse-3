package com.mvplugin.core;

import com.mvplugin.mockbukkit.MockServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({Bukkit.class, Plugin.class})
public class BukkitMultiverseTest extends MultiverseTest {

    protected MockServer server;

    @Before
    public void initialize() throws Exception {
        server = BukkitUtil.prepareBukkit(new PluginInfo("Multiverse-Core", "vTest", MultiverseCoreBukkitPlugin.class.getName(), new MultiverseCoreBukkitPlugin()));
    }
}
