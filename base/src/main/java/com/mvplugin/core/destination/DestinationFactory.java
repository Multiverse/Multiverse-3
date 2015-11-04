package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * This class should be implemented in order to provide destinations that can be teleported to.
 *
 * @see Destination
 */
public interface DestinationFactory {

    /**
     * Creates a destination based on the given destination string.
     * <br/>
     * This DestinationFactory should parse the destination string and return an appropriate Destination object.
     * <br/>
     * <strong>Note: </strong> The prefix will still be included in the destination string and must be parsed out by
     * the DestinationFactory. This will allow a single DestinationFactory to produce multiple destination types though
     * this is probably not as efficient as producing only 1 type.
     * <br/>
     * Destination strings pointing to an invalid destination should throw an {@link InvalidDestinationException}
     *
     * @param api The MultiverseCore API object to pass to the new Destination object.
     * @param destinationString The destination string which still includes the prefix.
     * @return a Destination object representing the given destination string.
     * @throws InvalidDestinationException this may be thrown when the given destination represents a destination type
     * that this DestinationFactory can process but is not currently a valid location. Example: a player that is not
     * online.
     */
    @NotNull
    Destination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException;

    /**
     * Gets the set of destination prefixes that this DestinationFactory will create destinations for.
     * <br/>
     * Prefixes should be unique if possible as duplicates in another DestinationFactory will cause
     * unspecified behavior. If simple prefixes (ex: single character) are desired, it is highly recommended that they
     * are also listed in {@link #getWeakPrefixes()} and that a longer, more unique prefix is provided as the primary
     * prefix.
     * <p/>
     * <strong>Note: </strong>Prefixes always prepend the destination string and are followed by a colon. This
     * colon should not be included in the strings provided by this method.
     *
     * @return The set of destination prefixes that this DestinationFactory will create destinations for.
     */
    @NotNull
    Set<String> getDestinationPrefixes();

    /**
     * This returns a set of prefixes which will not be registered if the prefix already exists in the registry.
     * <br/>
     * These prefixes must exists in the set returned by {@link #getDestinationPrefixes()}. This method has a default
     * implementation that returns an empty set. Implement this for less unique prefixes, such as single letters.
     *
     * @return A set of prefixes that will not be registered if the prefix has previously been registered by
     * a different DestinationFactory.
     */
    @NotNull
    default Set<String> getWeakPrefixes() {
        return Collections.emptySet();
    }
}
