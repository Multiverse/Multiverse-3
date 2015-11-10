package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.minecraft.Entity;
import pluginbase.permission.Permissible;

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

    public UnknownDestination(@NotNull MultiverseCoreAPI api, @NotNull final DestinationRegistry registry,
                              final int registrationCount, @NotNull final String destinationString) {
        super(api);
        this.registry = registry;
        this.oldRegistrationCount = registrationCount;
        this.destinationString = destinationString;
    }

    @Override
    public void teleport(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws TeleportException {
        if (reResolve() && lazilyResolvedDestination != null) {
            lazilyResolvedDestination.teleport(teleporter, teleportee);
        } else {
            throw new TeleportException(Message.bundleMessage(UNKNOWN_DESTINATION, teleportee.getName(), destinationString));
        }
    }

    @NotNull
    @Override
    public String getDestinationString() {
        return reResolve() && lazilyResolvedDestination != null ? lazilyResolvedDestination.getDestinationString() : this.destinationString;
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
            if (registry.getRegistrationCount() == oldRegistrationCount) {
                return false; // No new registrations since we last checked? Then we can skip this.
            }

            try {
                Destination d = registry.parseDestination(this.destinationString);
                if (d instanceof UnknownDestination) {
                    this.oldRegistrationCount = registry.getRegistrationCount();
                    return false;
                } else {
                    this.lazilyResolvedDestination = d;
                }
            } catch (InvalidDestinationException ignore) { }
        }
        return true;
    }
}
