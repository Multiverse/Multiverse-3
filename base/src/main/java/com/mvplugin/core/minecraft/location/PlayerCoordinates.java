package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

public interface PlayerCoordinates extends FacingCoordinates, BlockCoordinates {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    public String getWorld();
}
