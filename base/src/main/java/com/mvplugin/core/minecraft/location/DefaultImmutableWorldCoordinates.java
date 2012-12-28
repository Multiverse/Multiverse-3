package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

class DefaultImmutableWorldCoordinates implements EntityCoordinates, BlockCoordinates {

    private static int coordToBlock(final double coord) {
        final int floor = (int) coord;
        return floor == coord ? floor : floor - (int) (Double.doubleToRawLongBits(coord) >>> 63);
    }

    @NotNull
    private final String world;

    @NotNull final FacingCoordinates parent;

    DefaultImmutableWorldCoordinates(@NotNull final String world, @NotNull FacingCoordinates parent) {
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

    @Override
    public DefaultImmutableWorldCoordinates clone() {
        try {
            return (DefaultImmutableWorldCoordinates) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public void add(final double x, final double y, final double z) {
        parent.add(x, y, z);
    }

    @Override
    public void add(final int x, final int y, final int z) {
        add((double) x, (double) y, (double) z);
    }
}
