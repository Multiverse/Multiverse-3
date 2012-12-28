package com.mvplugin.core.minecraft.location;

public interface FacingCoordinates extends Coordinates, Cloneable {

    float getPitch();

    float getYaw();

    @Override
    FacingCoordinates clone();
}
