package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.mvplugin.core.MultiverseCoreAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is responsible for registering destination types and parsing destination strings.
 *
 * @see Destination
 */
public final class DestinationRegistry {
    private final class CacheEntry {
        @NotNull
        private final Class<? extends Destination> type;
        @Nullable
        private Destination cachedInstance;

        public CacheEntry(@NotNull final Class<? extends Destination> type)
                throws IllegalAccessException, InstantiationException {
            this.type = type;
            // another advantage of the caching mechanism: it's impossible to register a non-instantiatable type
            this.cachedInstance = type.newInstance();
            this.cachedInstance.init(api);
        }

        @Nullable
        public synchronized Destination tryParseAndRegenIfNecessary(@NotNull final String str) {
            if ((cachedInstance != null) && cachedInstance.tryParse(str)) {
                try {
                    Destination d = this.cachedInstance;
                    this.cachedInstance = type.newInstance();
                    this.cachedInstance.init(api);
                    return d;
                } catch (IllegalAccessException e) {
                    throw new Error(e); // should never happen since we are testing that in registerDestination()
                } catch (InstantiationException e) {
                    // Somebody made a constructor that just randomly throws exceptions at us?
                    // *sigh* ... come on, let's just log it and then disable that clown
                    Logging.warning("The destination type '%s' triggered an exception (not Multiverse's fault!): %s",
                            type, e.getCause());
                    this.cachedInstance = null; // Disabled.
                    return null;
                }
            }
            else return null;
        }
    }

    /**
     * This cache contains all the destination types and an instance for each one, for performance reasons
     * so we don't have to create-and-throw-away objects for every type whenever we parse a destination.
     */
    @NotNull
    private List<CacheEntry> destinationCache;

    @NotNull
    private final MultiverseCoreAPI api;

    public DestinationRegistry(@NotNull final MultiverseCoreAPI api) {
        this.api = api;
        this.destinationCache = Collections.synchronizedList(new LinkedList<CacheEntry>());

        this.registerDefaultDestinationTypes();
    }

    private void registerDefaultDestinationTypes() {
        try {
            this.registerDestination(EntityCoordinatesDestination.class);
            this.registerDestination(CannonDestination.class);
            this.registerDestination(WorldDestination.class);
        } catch (IllegalAccessException e) {
            throw new Error(e); // should never happen
        } catch (InstantiationException e) {
            throw new Error(e); // should never happen
        }
    }

    /**
     * Registers a destination type.
     *
     * @param type The destination type that should be registered.
     * @throws IllegalAccessException Thrown by {@link Class#newInstance()} (invoked on {@code type}).
     * @throws InstantiationException Thrown by {@link Class#newInstance()} (invoked on {@code type}).
     */
    public void registerDestination(Class<? extends Destination> type) throws IllegalAccessException, InstantiationException {
        destinationCache.add(new CacheEntry(type));
    }

    /**
     * Upgrades the internal storage in this {@link DestinationRegistry} to a {@link CopyOnWriteArrayList}.
     * Although not strictly necessary, doing this before actively using {@link #parseDestination(String)} will
     * result in better performance, since the method no longer has to synchronize on the list.
     * <p />
     * This should only be called once almost all destination registration are completed because any further
     * invocations of {@link #registerDestination(Class)} will have <b>terrible</b> performance (copy on write).
     */
    public void upgradeStorage() {
        synchronized (destinationCache) {
            if (!(destinationCache instanceof CopyOnWriteArrayList))
                destinationCache = new CopyOnWriteArrayList<CacheEntry>(destinationCache);
        }
    }

    /**
     * Parses a destination string. If the process fails, an {@link UnknownDestination} is returned.
     *
     * @param str The destination string.
     * @return A {@link Destination} object for the destination.
     */
    @NotNull
    public Destination parseDestination(@NotNull final String str) {
        // If this cache was already upgraded to a CopyOnWriteArrayList, we don't have to synchronize on it
        if (destinationCache instanceof CopyOnWriteArrayList)
            return doParseDestination(str);
        else synchronized (destinationCache) {
            return doParseDestination(str);
        }
    }

    @NotNull
    private Destination doParseDestination(@NotNull final String str) {
        for (CacheEntry entry : destinationCache) {
            Destination d;
            if ((d = entry.tryParseAndRegenIfNecessary(str)) != null)
                return d;
        }

        // No? Fallback: UnknownDestination
        return new UnknownDestination(this, getRegistrationCount(), str);
    }

    /**
     * @return The amount of destination types that have been registered.
     */
    int getRegistrationCount() {
        return destinationCache.size();
    }
}
