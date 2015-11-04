package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.permission.Permissible;

/**
 * Inheriting from this class is encouraged if a {@link Destination} teleports to fixed coordinates.
 */
public abstract class SimpleDestination extends Destination {

    protected SimpleDestination(@NotNull MultiverseCoreAPI coreAPI) {
        super(coreAPI);
    }

    /**
     * Gets the {@link EntityCoordinates} the teleportee should be teleported to.
     *
     * @return The {@link EntityCoordinates} the teleportee should be teleported to.
     * @throws TeleportException Thrown if an error occurs while trying to determine the teleport destination.
     */
    @NotNull
    protected abstract EntityCoordinates getDestination() throws TeleportException;
    /* TODO: Figure out how to handle Bukkit's "broken" permission inheritance
    public Perm getPermission(PermissionNode node) {
        return node.getSpecific(<myDestinationName>);
    }
    */

    @Override
    public void teleport(@NotNull Permissible teleporter, @NotNull Permissible teleportee,
                         @NotNull Entity teleporteeEntity) throws TeleportException {
        // TODO: Check permissions
        this.getSafeTeleporter().safelyTeleport(null, teleporteeEntity, getDestination());
    }
}
