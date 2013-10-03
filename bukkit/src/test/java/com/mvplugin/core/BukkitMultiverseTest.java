package com.mvplugin.core;

import com.mvplugin.mockbukkit.MockServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;
import pluginbase.bukkit.AbstractBukkitPlugin;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@PrepareForTest({Bukkit.class, MultiverseCoreBukkitPlugin.class, AbstractBukkitPlugin.class, PluginBase.class})
public class BukkitMultiverseTest extends MultiverseTest {

    private MockServer server = null;
    protected MultiverseCoreBukkitPlugin plugin;

    @Before
    public void initialize() throws Exception {
        reloadServer();
    }

    public void reloadServer() throws Exception {
        if (server != null) {
            server.shutdown();
        }
        server = prepareBukkit(new PluginDescriptionFile("Multiverse-Core", "Test", MultiverseCoreBukkitPlugin.class.getName()));
        plugin = (MultiverseCoreBukkitPlugin) server.getPluginManager().getPlugin("Multiverse-Core");
    }

    private static MockServer prepareBukkit(PluginDescriptionFile... plugins) throws Exception {
        final MockServer mockedServer = new MockServer();

        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, mockedServer);
        assertSame(mockedServer, Bukkit.getServer());

        for (PluginDescriptionFile pluginInfo : plugins) {
            mockedServer.getPluginManager().loadPlugin(pluginInfo);
        }
        mockedServer.loadDefaultWorlds();
        for (Plugin plugin : mockedServer.getPluginManager().getPlugins()) {
            mockedServer.getPluginManager().enablePlugin(plugin);
        }

        return mockedServer;
    }
}
