package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.util.CoreLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for registering destination types and parsing destination strings.
 *
 * @see Destination
 */
public final class DestinationRegistry {

    @NotNull
    private Map<String, DestinationFactory> prefixFactoryMap = new HashMap<>();
    @NotNull
    private WorldDestination.Factory worldDestinationFactory = new WorldDestination.Factory();

    @NotNull
    private final MultiverseCoreAPI api;

    public DestinationRegistry(@NotNull final MultiverseCoreAPI api) {
        this.api = api;

        registerDestinationFactory(new ExactDestination.Factory());
        registerDestinationFactory(new CannonDestination.Factory());
        registerDestinationFactory(worldDestinationFactory);
        registerDestinationFactory(new PlayerDestination.Factory());
    }

    public void registerDestinationFactory(@NotNull DestinationFactory destinationFactory) {
        CoreLogger.fine("Registering DestinationFactory: " + destinationFactory);
        if (destinationFactory.getDestinationPrefixes().isEmpty()) {
            CoreLogger.warning("DestinationFactory: %s cannot be registered without any prefixes.", destinationFactory.getClass());
        }
        for (String prefix : destinationFactory.getDestinationPrefixes()) {
            if (prefix.isEmpty()) {
                CoreLogger.warning("DestinationFactory: %s attempted to register an empty prefix.", destinationFactory.getClass());
                continue;
            }
            prefix = prefix.toLowerCase();
            if (!prefixFactoryMap.containsKey(prefix) || !destinationFactory.getWeakPrefixes().contains(prefix)) {
                prefixFactoryMap.put(prefix, destinationFactory);
                CoreLogger.finer("Registered prefix: %s to DestinationFactory: %s", prefix, destinationFactory.getClass());
            } else {
                CoreLogger.finer("Skipped weak prefix: %s for DestinationFactory: %s as that prefix has previously been registered.", prefix, destinationFactory.getClass());
            }
        }
    }

    /**
     * Parses a destination string. If the process fails, an {@link UnknownDestination} is returned.
     *
     * @param destinationString The destination string.
     * @return A {@link Destination} object for the destination.
     */
    @NotNull
    public Destination parseDestination(@NotNull final String destinationString) throws InvalidDestinationException {
        String[] destParts = destinationString.split(":", 2);
        if (destParts.length == 1) {
            return worldDestinationFactory.createDestination(api, destinationString);
        }

        DestinationFactory destinationFactory = getDestinationFactory(destParts[0]);
        if (destinationFactory != null) {
            return destinationFactory.createDestination(api, destinationString);
        }

        // No? Fallback: UnknownDestination
        return new UnknownDestination(api, this, getRegistrationCount(), destinationString);
    }

    @Nullable
    DestinationFactory getDestinationFactory(@NotNull String prefix) {
        return prefixFactoryMap.get(prefix);
    }

    /**
     * @return The amount of destination types that have been registered.
     */
    int getRegistrationCount() {
        return prefixFactoryMap.size();
    }
}
