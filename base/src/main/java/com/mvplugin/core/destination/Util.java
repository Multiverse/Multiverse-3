package com.mvplugin.core.destination;

/**
 * Contains some utility methods for {@link Destination} implementations.
 */
public final class Util {
    private Util() { }

    /**
     * Creates a named regular expression matching group that matches any signed and unsigned number.
     * It is prefixed with {@code :} to simplify the creation of destination string patterns.
     * @param name The name for the matching group.
     * @return The matching group, ready to be inserted into a regular expression.
     */
    public static String numberRegex(String name) {
        return String.format(":(?<%s>[-+]?\\d+(\\.\\d+)?)", name);
    }

    /**
     * Invokes {@link Object#toString()} on all arguments and then joins those string fragments
     * together, delimited with a colon.
     * @param args The arguments that should be used to create the string.
     * @return The joined string.
     */
    public static String colonJoin(Object... args) {
        StringBuilder builder = new StringBuilder(args[0].toString());
        for (int i = 1; i < args.length; i++)
            builder.append(':').append(args[i].toString());
        return builder.toString();
    }
}
