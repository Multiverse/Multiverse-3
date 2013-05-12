package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.permission.Permissible;
import com.mvplugin.core.exceptions.TeleportException;

/**
 * Inheriting from this class is encouraged if a {@link Destination} teleports to fixed coordinates.
 */
public abstract class SimpleDestination extends Destination {
    /**
     * Gets the {@link EntityCoordinates} the teleportee should be teleported to.
     *
     * @return The {@link EntityCoordinates} the teleportee should be teleported to.
     */
    protected abstract EntityCoordinates getDestination();
    /* TODO: Figure out how to handle Bukkit's "broken" permission inheritance
    public Perm getPermission(PermissionNode node) {
        return node.getSpecific(<myDestinationName>);
    }
    */

    @Override
    public void teleport(final Permissible teleporter, final Permissible teleportee,
                         final Entity teleporteeEntity) throws TeleportException {
        // TODO: Check permissions
        this.getSafeTeleporter().safelyTeleport(null, teleporteeEntity, getDestination());
    }
}
