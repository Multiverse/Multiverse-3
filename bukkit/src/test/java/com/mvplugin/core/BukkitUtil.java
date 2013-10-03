package com.mvplugin.core;

import com.mvplugin.mockbukkit.MockServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class BukkitUtil {

    public static MockServer prepareBukkit(PluginInfo... plugins) throws Exception {
        final MockServer mockedServer = new MockServer();
        for (PluginInfo pluginInfo : plugins) {
            mockedServer.getPluginManager().loadPlugin(pluginInfo);
        }
        mockedServer.loadDefaultWorlds();
        for (Plugin plugin : mockedServer.getPluginManager().getPlugins()) {
            mockedServer.getPluginManager().enablePlugin(plugin);
        }

        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, mockedServer);
        assertSame(mockedServer, Bukkit.getServer());

        return mockedServer;
    }
}
