package com.mvplugin.testing.bukkit;

import com.mvplugin.testing.bukkit.plugin.TestingPluginManager;
import org.bukkit.Server;

public interface TestingServer extends Server {

    void loadDefaultWorlds();

    TestingPluginManager getPluginManager();

    void setAllowEnd(boolean allowEnd);

    void setAllowNether(boolean allowNether);

    void setTicksPerMonsterSpawn(int ticksPerMonsterSpawn);

    void setTicksPerAnimalSpawn(int ticksPerAnimalSpawn);
}
