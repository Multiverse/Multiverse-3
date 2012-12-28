package com.mvplugin.core.minecraft;

/**
 * Represents a point in 3-dimensional space.
 */
public class Coordinates3D {

    private final double x, y, z;

    public Coordinates3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the x coordinate of this point.
     *
     * @return The x coordinate of this point.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y coordinate of this point.
     *
     * @return The y coordinate of this point.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets the z coordinate of this point.
     *
     * @return The z coordinate of this point.
     */
    public double getZ() {
        return this.z;
    }
}
