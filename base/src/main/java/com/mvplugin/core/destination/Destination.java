package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.permission.Permissible;

/**
 * A destination for teleportations.
 * <br/>
 * In order to create a custom destination, a {@link DestinationFactory} must be registered via
 * {@link DestinationRegistry#registerDestinationFactory(DestinationFactory)}. The DestinationFactory will handle
 * creation of custom Destination objects.
 *
 * @see SimpleDestination
 * @see DestinationRegistry
 * @see DestinationFactory
 */
public abstract class Destination {

    @NotNull
    private final MultiverseCoreAPI api;

    protected Destination(@NotNull MultiverseCoreAPI coreAPI) {
        this.api = coreAPI;
    }

    /**
     * Carries out a teleportation to this {@link Destination}.
     *
     * @param teleporter The {@link Permissible} that initiated the teleportation.
     * @param teleportee The {@link Entity} that is going to be teleported.
     * @throws TeleportException If the teleportation fails.
     */
    public abstract void teleport(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws TeleportException;

    /**
     * Converts this {@link Destination} into a destination string that can be parsed by
     * {@link DestinationFactory#createDestination(MultiverseCoreAPI, String)}.
     * <br/>The string must be prefixed with a destination prefix listed in
     * {@link DestinationFactory#getDestinationPrefixes()} and a colon. If the destination has multiple prefixes,
     * it is recommended that the most unique prefix be used for the destination string.
     * <p/>
     * <strong>Example strings:</strong>
     * <ul>
     *     <li>player:someguy47</li>
     *     <li>loc:56,23,100</li>
     *     <li>world:world_nether</li>
     *     <li>w:world_nether <em>(not recommended due to the short prefix!)</em></li>
     * </ul>
     *
     * @return The destination string that represents this destination object.
     */
    @NotNull
    protected abstract String getDestinationString();

    /**
     * Gets the {@link MultiverseCoreAPI} that this {@link Destination} is bound to.
     *
     * @return The {@link MultiverseCoreAPI} that this {@link Destination} is bound to.
     */
    @NotNull
    protected final MultiverseCoreAPI getApi() {
        return this.api;
    }

    /**
     * The {@link SafeTeleporter} is the recommended means of teleportation and should be used instead
     * of {@link Entity#teleport(EntityCoordinates)} when applicable.
     *
     * @return The {@link SafeTeleporter} instance.
     */
    @NotNull
    protected final SafeTeleporter getSafeTeleporter() {
        return getApi().getSafeTeleporter();
    }

    @Override
    public String toString() {
        return getClass() + ": {" + getDestinationString() + "}";
    }
}
