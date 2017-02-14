package com.mvplugin.core;

import com.mvplugin.testing.bukkit.ServerFactory;
import com.mvplugin.testing.bukkit.TestingServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@PrepareForTest({Bukkit.class, MultiverseCoreBukkitPlugin.class, JavaPlugin.class, PluginBase.class, JavaPluginLoader.class})
@RunWith(PowerMockRunner.class)
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
        System.out.println("Testing server created");

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

    @Test
    public void emptyTest() {
        // Required to make JUnit happy. There's probably a better solution.
    }
}
