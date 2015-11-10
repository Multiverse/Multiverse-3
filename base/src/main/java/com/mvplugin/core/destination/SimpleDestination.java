package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.PermissionException;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.Language.Destination.World;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Message;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.permission.Permissible;

/**
 * This class offers a simle way to implement a Destination. It will simply teleport the player to the location
 * given by {@link #getDestination()} after checking permissions.
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

    /**
     * {@inheritDoc}
     * @implNote the default implementation of this method calls {@link #checkPermissions(Permissible, Entity)}.
     * If you override this method you will probably want to call {@link #checkPermissions(Permissible, Entity)}
     * manually to ensure permissions are checked before teleportation.
     */
    @Override
    public void teleport(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws TeleportException {
        try {
            checkPermissions(teleporter, teleportee);
        } catch (PermissionException e) {
            throw new TeleportException(e.getBundledMessage(), e);
        }
        this.getSafeTeleporter().safelyTeleport(null, teleportee, getDestination());
    }

    /**
     * This method verifies that the teleporter has the required permissions to teleport the teleportee to the
     * destination given by {@link #getDestination()}. The method should simply return if permissions are satisfied.
     * <br/>
     * The default implemenation of {@link Destination#teleport(Permissible, Entity)} will call this method before
     * teleporting anyone.
     * <br/>
     * The default implementation of this method will check the following (if relevant):
     * <ul>
     *     <li>Can the teleporter teleport self to the world specified in {@link #getDestination()}?</li>
     *     <li>Can the teleporter teleport others to the world specified in {@link #getDestination()}?</li>
     * </ul>
     * The checks pass in the default implementation if the teleportee is already in the world specified by the
     * destination. Also if {@link #getDestination()} throws a TeleportException, no checks will be done and
     * teleportation is assumed to be allowed.<br/>
     * If a destination requires more specific teleporation checks it should override this method.<br/>
     *
     * @param teleporter the person attempting cause teleportation
     * @param teleportee the target of the teleportation attempt
     * @throws PermissionException thrown when permissions are not met. The exception should include an appropriate
     * description of the permission issue to show to the teleporter.
     */
    protected void checkPermissions(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws PermissionException {
        try {
            EntityCoordinates coordinates = getDestination();
            if (!coordinates.getWorld().equals(teleportee.getLocation().getWorld())) {
                if (teleporter.equals(teleportee)) {
                    if (!teleporter.hasPerm(Perms.TP_SELF_WORLD, coordinates.getWorld())) {
                        throw new PermissionException(Message.bundleMessage(World.NO_PERMISSION, teleportee.getName(),
                                coordinates.getWorld(), Perms.TP_SELF_WORLD.getName(coordinates.getWorld())), Perms.TP_SELF_WORLD);
                    }
                } else {
                    if (!teleporter.hasPerm(Perms.TP_OTHER_WORLD, coordinates.getWorld())) {
                        throw new PermissionException(Message.bundleMessage(World.NO_PERMISSION, teleportee.getName(),
                                coordinates.getWorld(), Perms.TP_OTHER_WORLD.getName(coordinates.getWorld())), Perms.TP_OTHER_WORLD);
                    }
                }
            }
        } catch (TeleportException ignore) { }
    }
}
