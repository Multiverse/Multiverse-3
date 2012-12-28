package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

class DefaultWorldCoordinates implements EntityCoordinates, BlockCoordinates {

    private static int coordToBlock(final double coord) {
        final int floor = (int) coord;
        return floor == coord ? floor : floor - (int) (Double.doubleToRawLongBits(coord) >>> 63);
    }

    @NotNull
    private final String world;

    @NotNull final FacingCoordinates parent;

    DefaultWorldCoordinates(@NotNull final String world, @NotNull FacingCoordinates parent) {
        this.world = world;
        this.parent = parent;
    }

    @NotNull
    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public double getX() {
        return parent.getX();
    }

    @Override
    public double getY() {
        return parent.getY();
    }

    @Override
    public double getZ() {
        return parent.getZ();
    }

    @Override
    public int getBlockX() {
        return coordToBlock(getX());
    }

    @Override
    public int getBlockY() {
        return coordToBlock(getY());
    }

    @Override
    public int getBlockZ() {
        return coordToBlock(getZ());
    }

    @Override
    public float getPitch() {
        return parent.getPitch();
    }

    @Override
    public float getYaw() {
        return parent.getYaw();
    }
}
