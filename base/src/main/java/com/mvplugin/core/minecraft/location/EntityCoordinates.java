package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

public interface EntityCoordinates extends FacingCoordinates, BlockCoordinates, Cloneable {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    public String getWorld();

    @Override
    EntityCoordinates clone();
}
