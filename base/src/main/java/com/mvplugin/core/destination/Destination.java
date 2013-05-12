package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.permission.Permissible;
import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A destination for teleportations.
 * <p />
 * Implementations <b>must</b> provide a parameterless constructor that <b>never</b> throws exceptions.
 * Therefore, implementing a custom constructor is discouraged.
 *
 * @see SimpleDestination
 * @see DestinationRegistry
 */
public abstract class Destination {
    @Nullable
    private MultiverseCoreAPI api;

    /**
     * This should only ever be called by the {@link DestinationRegistry} to initialize
     * this object and bind it to a {@link MultiverseCoreAPI}.
     * @param api The {@link MultiverseCoreAPI} this {@link Destination} should be bound to.
     */
    final void init(@NotNull final MultiverseCoreAPI api) {
        if (this.api != null)
            throw new UnsupportedOperationException("This object is already initialized!");
        this.api = api;
    }

    /**
     * Carries out a teleportation to this {@link Destination}.
     * <br />
     * Usually {@code teleportee == teleporteeEntity}.
     *
     * @param teleporter The {@link Permissible} that initiated the teleportation.
     * @param teleportee The {@link Permissible} that is going to be teleported.
     * @param teleporteeEntity The {@link Entity} that is going to be teleported.
     * @throws TeleportException If the teleportation fails.
     */
    public abstract void teleport(Permissible teleporter, Permissible teleportee, Entity teleporteeEntity) throws TeleportException;

    /**
     * Deserializes a destination string into this destination object if the string actually is of this type.
     * <p />
     * The {@link DestinationRegistry} calls this on every destination type it knows when it's parsing
     * a destination string until one returns {@code true}. Therefore, this method should provide optimal performance
     * for destination strings that belong to a different destination type (consider using a prefix).
     * <p />
     * <b>{@link Destination} objects must not be reused. Further invocations of this method after it has returned
     * {@code true} once are unspecified behavior.</b>
     *
     * @param str The destination string that should be parsed.
     * @return Whether the destination string represented a destination of this type and could be successfully parsed.
     * @see #serialize()
     */
    public abstract boolean tryParse(String str);

    /**
     * Serializes this {@link Destination} into a destination string that can be deserialized
     * by {@link #tryParse(String)}. Implementations may use an arbitrary format, as long as it's unique.
     * Ambiguities result in unspecified behavior. Consider using a prefix.
     *
     * @return The destination string that represents this destination object.
     * @see #tryParse(String)
     */
    public abstract String serialize();

    /**
     * Gets the {@link MultiverseCoreAPI} that this {@link Destination} is bound to.
     * @return The {@link MultiverseCoreAPI} that this {@link Destination} is bound to.
     */
    @NotNull
    public final MultiverseCoreAPI getApi() {
        if (this.api == null)
            throw new UnsupportedOperationException("This object has to be initialized before it can be used!");
        return this.api;
    }

    /**
     * The {@link SafeTeleporter} is the recommended means of teleportation and should be used instead
     * of {@link Entity#teleport(EntityCoordinates)} when applicable.
     * @return The {@link SafeTeleporter} instance.
     */
    @NotNull
    protected final SafeTeleporter getSafeTeleporter() {
        return this.getApi().getSafeTeleporter();
    }
}
