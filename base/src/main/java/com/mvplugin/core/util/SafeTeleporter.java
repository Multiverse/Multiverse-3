package com.mvplugin.core.util;

import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;

/**
 * Used to safely teleport people.
 *
 * Safe teleportation means that they won't end up in some game location that will harm them.
 */
public interface SafeTeleporter {

    /** The default height radius to scan for safe locations */
    int DEFAULT_HEIGHT = 3;
    /** The default width radius to scan for safe locations */
    int DEFAULT_WIDTH = 9;

    /**
     * Gets the next safe location around the given location.
     *
     * Safe entails that the returned location will not be somewhere that would harm a player.
     * This method will use the default height and width for a search area.
     *
     * @param location The location to search nearby.
     * @return A safe location near the original location or the original location if it is deemed safe.  If no safe
     * location can be found, null is returned.
     */
    @Nullable
    EntityCoordinates getSafeLocation(@NotNull final EntityCoordinates location);

    /**
     * Gets the next safe location around the given location with a given tolerance and search radius.
     *
     * Safe entails that the returned location will not be somewhere that would harm a player.
     *
     * @param location The location to search nearby.
     * @param height The radius of blocks on the y-axis to search.
     * @param width The radius of blocks on the x and z-axis to search.
     * @return A safe location near the original location or the original location if it is deemed safe.  If no safe
     * location can be found, null is returned.
     */
    @Nullable
    EntityCoordinates getSafeLocation(@NotNull final EntityCoordinates location, final int height, final int width);

    /**
     * Safely teleport the target to the location. This will perform checks to see if the place is safe, and if
     * it's not, will adjust the final destination accordingly.
     *
     * @param sender Person who performed the teleport command, if anyone.
     * @param target Entity to teleport.
     * @param location location to teleport them to.
     * @throws TeleportException If any problems occur that prevent teleporation.  The message included in the
     * exception will explain any issues.
     */
    void safelyTeleport(@Nullable final BasePlayer sender, @NotNull final Entity target,
                        @NotNull final EntityCoordinates location) throws TeleportException;
}
