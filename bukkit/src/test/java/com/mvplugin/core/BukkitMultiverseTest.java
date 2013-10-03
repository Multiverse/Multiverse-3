package com.mvplugin.core;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;
import pluginbase.bukkit.AbstractBukkitPlugin;

@PrepareForTest({Bukkit.class, MultiverseCoreBukkitPlugin.class, AbstractBukkitPlugin.class, PluginBase.class})
public class BukkitMultiverseTest extends MultiverseTest {

    protected MultiverseCoreBukkitPlugin plugin;

    @Before
    public void initialize() throws Exception {
        reloadServer();
    }

    public void reloadServer() throws Exception {
        Server server = BukkitUtil.prepareBukkit(new PluginDescriptionFile("Multiverse-Core", "Test", MultiverseCoreBukkitPlugin.class.getName()));
        plugin = (MultiverseCoreBukkitPlugin) server.getPluginManager().getPlugin("Multiverse-Core");
    }
}
