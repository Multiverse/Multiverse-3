package com.mvplugin.core;

import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.CoreLogger;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.minecraft.location.MutableEntityCoordinates;

import static com.mvplugin.core.util.Language.DefaultSafeTeleporter.*;

class DefaultSafeTeleporter implements SafeTeleporter {
    @NotNull
    protected final MultiverseCoreAPI api;

    protected DefaultSafeTeleporter(@NotNull final MultiverseCoreAPI api) {
        this.api = api;
    }

    @Nullable
    @Override
    public EntityCoordinates getSafeLocation(@NotNull final EntityCoordinates location) {
        return getSafeLocation(location, DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    @Nullable
    @Override
    public EntityCoordinates getSafeLocation(@NotNull final EntityCoordinates location, final int height, final int width) {
        // Check around the player first in a configurable radius:
        // TODO: Make this configurable
        final EntityCoordinates safe = checkAboveAndBelowLocation(location, height, width);
        if (safe != null) {
            CoreLogger.fine("Found a safe location: %s", safe); // TODO plugin.getLocationManipulation().strCoordsRaw(safe));
            return getBlockCenteredCoordinates(safe);
        } else {
            CoreLogger.fine("Uh oh! No safe location found!");
            return null;
        }
    }

    private static final double BLOCK_CENTER = .5D;

    @NotNull
    private EntityCoordinates getBlockCenteredCoordinates(@NotNull final EntityCoordinates loc) {
        return Locations.getEntityCoordinates(loc.getWorld(), loc.getBlockX() + BLOCK_CENTER,
                loc.getY(), loc.getBlockZ() + BLOCK_CENTER, loc.getPitch(), loc.getYaw());
    }

    @Nullable
    private EntityCoordinates checkAboveAndBelowLocation(@NotNull final EntityCoordinates location, final int height, final int width) {
        CoreLogger.finer("Given Location of: %s", location); // TODO plugin.getLocationManipulation().strCoordsRaw(l));
        CoreLogger.finer("Checking +-%s with a radius of %s", height, width);
        // For now this will just do a straight up block.
        // Check the main level
        EntityCoordinates safe = checkAroundLocation(location, width);
        if (safe != null)
            return safe;

        // We've already checked zero right above this.
        for (int currentLevel = 1; currentLevel <= height; currentLevel++) {
            // Check above
            if ((safe = checkAroundLocation(Locations.getEntityCoordinates(location.getWorld(), location.getX(),
                    location.getY() + currentLevel, location.getZ(), location.getPitch(), location.getYaw()), width)) != null)
                return safe;

            // Check below
            if ((safe = checkAroundLocation(Locations.getEntityCoordinates(location.getWorld(), location.getX(),
                    location.getY() - currentLevel, location.getZ(), location.getPitch(), location.getYaw()), width)) != null)
                return safe;
        }

        return null;
    }

    @Nullable
    private EntityCoordinates checkAroundLocation(@NotNull final EntityCoordinates location, final int radius) {
        // Let's check the center of the 'circle' first...
        if (api.getBlockSafety().isSafeLocation(location)) {
            return location;
        }

        // Now we're going to search in expanding concentric circles...
        for (int currentRadius = 0; currentRadius <= radius; currentRadius++) {
            MutableEntityCoordinates locToCheck = location.mutableCopy();
            if (checkAroundSpecificDiameter(locToCheck, currentRadius)) {
                // If a safe area was found: Return the checkLoc, it is the safe location.
                return locToCheck;
            }
        }

        return null;
    }

    private boolean checkAroundSpecificDiameter(@NotNull final MutableEntityCoordinates checkLoc, final int radius) {
        // Check out at the radius provided.
        checkLoc.add(radius, 0, 0);
        if (api.getBlockSafety().isSafeLocation(checkLoc)) {
            return true;
        }

        // Move up to the first corner..
        for (int i = 0; i < radius; i++) {
            checkLoc.add(0, 0, 1);
            if (api.getBlockSafety().isSafeLocation(checkLoc)) {
                return true;
            }
        }

        // Move to the second corner..
        for (int i = 0; i < radius * 2; i++) {
            checkLoc.add(-1, 0, 0);
            if (api.getBlockSafety().isSafeLocation(checkLoc)) {
                return true;
            }
        }

        // Move to the third corner..
        for (int i = 0; i < radius * 2; i++) {
            checkLoc.add(0, 0, -1);
            if (api.getBlockSafety().isSafeLocation(checkLoc)) {
                return true;
            }
        }

        // Move to the last corner..
        for (int i = 0; i < radius * 2; i++) {
            checkLoc.add(1, 0, 0);
            if (api.getBlockSafety().isSafeLocation(checkLoc)) {
                return true;
            }
        }

        // Move back to just before the starting point.
        for (int i = 0; i < radius - 1; i++) {
            checkLoc.add(0, 0, 1);
            if (api.getBlockSafety().isSafeLocation(checkLoc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void safelyTeleport(@Nullable final BasePlayer sender, @NotNull final Entity target, @NotNull final EntityCoordinates location) throws TeleportException {
        final EntityCoordinates safeLocation = getSafeLocation(location);
        if (safeLocation != null) {
            if (!target.teleport(safeLocation)) {
                throw new TeleportException(Message.bundleMessage(TELEPORT_FAILED, target, safeLocation));
            }
        } else {
            throw new TeleportException(Message.bundleMessage(NO_SAFE_LOCATION, location, target));
        }
    }
}
