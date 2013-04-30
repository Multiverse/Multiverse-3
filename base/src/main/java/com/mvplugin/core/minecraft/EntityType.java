package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the type of an entity on the minecraft server.
 */
public interface EntityType {

    /**
     * The name of this type of entity.
     *
     * @return The name of this type of entity.
     */
    @NotNull
    String getName();

    /**
     * The name of this type of entity.
     *
     * @return The name of this type of entity.
     */
    @NotNull
    @Override
    String toString();
}
