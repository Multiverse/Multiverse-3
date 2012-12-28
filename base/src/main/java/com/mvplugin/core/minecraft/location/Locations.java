package com.mvplugin.core.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * A helper class for creating a variety of location designating objects.
 */
public class Locations {

    private Locations() {
        throw new AssertionError();
    }

    public static final FacingCoordinates NULL_FACING = getFacingCoordinates(0D, 0D, 0D, 0F, 0F);

    public static Coordinates getCoordinates(final double x, final double y, final double z) {
        return new DefaultCoordinates(x, y, z, 0F, 0F);
    }

    public static FacingCoordinates getFacingCoordinates(final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultCoordinates(x, y, z, pitch, yaw);
    }

    public static BlockCoordinates getBlockCoordinates(@NotNull final String world,
                                                       final int x, final int y, final int z) {
        return new DefaultWorldCoordinates(world, getFacingCoordinates((double) x, (double) y, (double) z, 0F, 0F));
    }

    public static EntityCoordinates getEntityCoordinates(@NotNull final String world,
                                                         final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultWorldCoordinates(world, getFacingCoordinates(x, y, z, pitch, yaw));
    }

}
