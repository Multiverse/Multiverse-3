package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

public interface BlockCoordinates extends Cloneable {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    String getWorld();

    /**
     * Gets the block x coordinate represented by this location.
     *
     * @return The block x coordinate represented by this location.
     */
    int getBlockX();

    /**
     * Gets the block y coordinate represented by this location.
     *
     * @return The block y coordinate represented by this location.
     */
    int getBlockY();

    /**
     * Gets the block z coordinate represented by this location.
     *
     * @return The block z coordinate represented by this location.
     */
    int getBlockZ();

    @Override
    BlockCoordinates clone();

    void add(final int x, final int y, final int z);
}
