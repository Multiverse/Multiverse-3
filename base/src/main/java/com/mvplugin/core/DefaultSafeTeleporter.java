package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.Destination;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DefaultSafeTeleporter implements SafeTeleporter {

    private static final Message NO_SAFE_LOCATION = new Message("teleporter.no_safe_location",
            "Multiverse could not find a safe location near '%s' for teleporting '%s'.");
    private static final Message TELEPORT_FAILED = new Message("teleporter.failed",
            "Multiverse could not teleport '%s' to safe location '%s'.");

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
            Logging.fine("Found a safe location: %s", safe); // TODO plugin.getLocationManipulation().strCoordsRaw(safe));
            return getBlockCenteredCoordinates(safe);
        } else {
            Logging.fine("Uh oh! No safe location found!");
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
        Logging.finer("Given Location of: %s", location);// TODO plugin.getLocationManipulation().strCoordsRaw(l));
        Logging.finer("Checking +-%s with a radius of %s", height, width);
        // For now this will just do a straight up block.
        EntityCoordinates locToCheck = Locations.copyOf(location);
        // Check the main level
        EntityCoordinates safe = checkAroundLocation(locToCheck, width);
        if (safe != null) {
            return safe;
        }
        // We've already checked zero right above this.
        int currentLevel = 1;
        while (currentLevel <= height) {
            // Check above
            locToCheck = Locations.copyOf(location);
            locToCheck.add(0, currentLevel, 0);
            safe = checkAroundLocation(locToCheck, width);
            if (safe != null) {
                return safe;
            }

            // Check below
            locToCheck = Locations.copyOf(location);
            locToCheck.subtract(0, currentLevel, 0);
            safe = checkAroundLocation(locToCheck, width);
            if (safe != null) {
                return safe;
            }
            currentLevel++;
        }
        return null;
    }

    @Nullable
    private EntityCoordinates checkAroundLocation(@NotNull final EntityCoordinates location, final int radius) {
        EntityCoordinates locToCheck = Locations.copyOf(location);

        // Let's check the center of the 'circle' first...
        if (api.getBlockSafety().isSafeLocation(locToCheck)) {
            return locToCheck;
        }
        // Now we're going to search in expanding concentric circles...
        int currentRadius = 1;
        while (currentRadius <= radius) {
            boolean foundSafeArea = checkAroundSpecificDiameter(locToCheck, currentRadius);
            // If a safe area was found:
            if (foundSafeArea) {
                // Return the checkLoc, it is the safe location.
                return locToCheck;
            }
            // Otherwise, let's reset our location
            locToCheck = Locations.copyOf(location);
            // And increment the radius
            currentRadius++;
        }
        return null;
    }

    private boolean checkAroundSpecificDiameter(@NotNull final EntityCoordinates checkLoc, final int radius) {
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
    public void safelyTeleport(@Nullable final BasePlayer sender, @NotNull final Entity target, @NotNull final Destination destination) throws TeleportException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void safelyTeleport(@Nullable final BasePlayer sender, @NotNull final Entity target, @NotNull final EntityCoordinates location) throws TeleportException {
        final EntityCoordinates safeLocation = getSafeLocation(location);
        if (safeLocation != null) {
            if (!target.teleport(location)) {
                throw new TeleportException(new BundledMessage(TELEPORT_FAILED, target, safeLocation));
            }
        }
        throw new TeleportException(new BundledMessage(NO_SAFE_LOCATION, location, target));
    }

    @NotNull
    @Override
    public EntityCoordinates getSafeLocation(@NotNull final Entity entity, @NotNull final Destination destination) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
