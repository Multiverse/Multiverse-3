package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;

/**
 * Represents cause of an creature's spawning.
 */
public interface CreatureSpawnCause {

    /**
     * The name of the spawn cause.
     *
     * @return The name of the spawn cause.
     */
    @NotNull
    String getName();

    /**
     * The name of the spawn cause.
     *
     * @return The name of the spawn cause.
     */
    @Override
    @NotNull
    String toString();
}
