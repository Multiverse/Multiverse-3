package com.mvplugin.core.destination;

import org.jetbrains.annotations.NotNull;

/**
 * Contains some utility methods for {@link Destination} implementations.
 */
final class DestinationUtil {
    private DestinationUtil() { }

    /**
     * Creates a named regular expression matching group that matches any signed and unsigned number.
     * It is prefixed with {@code :} to simplify the creation of destination string patterns.
     * @param name The name for the matching group.
     * @return The matching group, ready to be inserted into a regular expression.
     */
    static String numberRegex(@NotNull String name) {
        return String.format("(?<%s>[-+]?\\d+(\\.\\d+)?)", name);
    }

    /**
     * Invokes {@link Object#toString()} on all arguments and then joins those string fragments
     * together, delimited with a colon.
     * @param args The arguments that should be used to create the string.
     * @return The joined string.
     */
    static String colonJoin(Object... args) {
        StringBuilder builder = new StringBuilder(args[0].toString());
        for (int i = 1; i < args.length; i++)
            builder.append(':').append(args[i].toString());
        return builder.toString();
    }

    @NotNull
    static String removePrefix(@NotNull String destinationString) {
        String[] parts = destinationString.split(":", 2);
        return parts.length == 1 ? destinationString : parts[1];
    }
}
