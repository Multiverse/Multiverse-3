package com.mvplugin.core;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class BukkitUtil {

    public static void prepareBukkitClass() throws Exception {
        final Server mockedServer = MockServer.getMockedServer();
        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, mockedServer);
        assertSame(mockedServer, Bukkit.getServer());
    }
}
