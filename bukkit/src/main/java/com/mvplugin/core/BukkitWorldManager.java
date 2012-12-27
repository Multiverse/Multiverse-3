package com.mvplugin.core;

import com.mvplugin.core.api.MultiverseWorld;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class BukkitWorldManager extends DefaultWorldManager {

    public BukkitWorldManager(@NotNull MultiverseCorePlugin plugin, @NotNull WorldFactory worldFactory) {
        super(plugin, worldFactory);
    }

    public MultiverseWorld getWorld(World world) {
        return this.worldsMap.get(world.getName());
    }

}
