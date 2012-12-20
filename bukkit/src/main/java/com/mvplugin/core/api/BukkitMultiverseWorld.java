package com.mvplugin.core.api;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Additional API for a Multiverse handled world specifically for Bukkit.
 */
public interface BukkitMultiverseWorld extends MultiverseWorld {

    /**
     * Gets the Bukkit {@link World} associated with this MultiverseWorld.
     *
     * @return The Bukkit world associated with this Multiverse world.
     * @throws IllegalStateException Thrown if the reference to the Bukkit world is lost for some reason.
     */
    @NotNull
    World getBukkitWorld() throws IllegalStateException;
}
