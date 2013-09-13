package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.permission.Permissible;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.mvplugin.core.util.Language.Destination.Unknown.*;

/**
 * The unknown destination. When the {@link DestinationRegistry} can't resolve a
 * destination string, it will use this type.
 * <p />
 * MV2 had the invalid destination. However, it had one big disadvantage: If a plugin registered
 * its destination type, the user used it in countless places and then one single time that plugin
 * crashed on startup for whatever reasons and couldn't register its type, <b>all those
 * destinations would be set to {@code i:Invalid Destination}</b>! This class solves that problem
 * by serializing to the unresolved destination string and therefore not changing the config.
 * <p />
 * This class is not a valid {@link Destination} implementation as it doesn't have a parameterless
 * constructor. However, that is perfectly fine because being an invalid {@link Destination} is
 * the entire purpose of this class.
 */
public final class UnknownDestination extends Destination {
    @NotNull
    private final DestinationRegistry registry;
    private int oldRegistrationCount;
    @NotNull
    private String destinationString;
    @Nullable
    private Destination lazilyResolvedDestination;

    public UnknownDestination(@NotNull final DestinationRegistry registry, final int registrationCount,
                              @NotNull final String destinationString) {
        this.registry = registry;
        this.oldRegistrationCount = registrationCount;
        this.destinationString = destinationString;
    }

    @Override
    public void teleport(final Permissible teleporter, final Permissible teleportee, final Entity teleporteeEntity) throws TeleportException {
        if (reResolve())
            lazilyResolvedDestination.teleport(teleporter, teleportee, teleporteeEntity);
        else
            throw new TeleportException(Message.bundleMessage(UNKNOWN_DESTINATION, teleporteeEntity, destinationString));
    }

    @Override
    public boolean tryParse(final String str) {
        throw new UnsupportedOperationException("The unknown destination doesn't support the usual parsing process!");
    }

    @Override
    public String serialize() {
        return reResolve() ? lazilyResolvedDestination.serialize() : this.destinationString;
    }

    /**
     * @return True if this {@link UnknownDestination} was lazily resolved.
     */
    public boolean isResolved() {
        return lazilyResolvedDestination != null;
    }

    /**
     * Attempts to lazily resolve this {@link UnknownDestination}.
     * @return {@link #isResolved()}
     */
    public boolean reResolve() {
        if (lazilyResolvedDestination == null) {
            if (registry.getRegistrationCount() == oldRegistrationCount)
                return false; // No new registrations since we last checked? Then we can skip this.

            Destination d = registry.parseDestination(this.destinationString);
            if (d instanceof UnknownDestination) {
                this.oldRegistrationCount = registry.getRegistrationCount();
                return false;
            } else this.lazilyResolvedDestination = d;
        }
        return true;
    }
}
