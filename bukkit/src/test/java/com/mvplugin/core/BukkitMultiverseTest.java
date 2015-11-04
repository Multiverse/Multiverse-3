package com.mvplugin.core;

import com.mvplugin.testing.bukkit.ServerFactory;
import com.mvplugin.testing.bukkit.TestingServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@PrepareForTest({Bukkit.class, MultiverseCoreBukkitPlugin.class, JavaPlugin.class, PluginBase.class})
public class BukkitMultiverseTest extends MultiverseTest {

    private Server server = null;
    protected MultiverseCoreBukkitPlugin plugin;

    protected void extraSetup() throws Exception {
        reloadServer();
    }

    protected void extraCleanup() throws Exception {

    }

    public void reloadServer() throws Exception {
        if (server != null) {
            server.shutdown();
        }
        server = prepareBukkit(new PluginDescriptionFile("Multiverse-Core", "Test", MultiverseCoreBukkitPlugin.class.getName()));
        plugin = (MultiverseCoreBukkitPlugin) server.getPluginManager().getPlugin("Multiverse-Core");
    }

    private static Server prepareBukkit(PluginDescriptionFile... plugins) throws Exception {
        final TestingServer testingServer = ServerFactory.createTestingServer();

        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, testingServer);
        assertSame(testingServer, Bukkit.getServer());

        for (PluginDescriptionFile pluginInfo : plugins) {
            testingServer.getPluginManager().loadPlugin(pluginInfo);
        }
        testingServer.loadDefaultWorlds();
        for (Plugin plugin : testingServer.getPluginManager().getPlugins()) {
            testingServer.getPluginManager().enablePlugin(plugin);
        }

        return testingServer;
    }
}
