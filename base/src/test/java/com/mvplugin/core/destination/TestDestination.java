package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.minecraft.location.EntityCoordinates;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

public class TestDestination extends SimpleDestination {

    static final Set<String> PREFIXES = new CopyOnWriteArraySet<String>() {{
        add("test");
        add("t");
    }};

    private static final Set<String> WEAK_PREFIXES = new CopyOnWriteArraySet<String>() {{
       add("t");
    }};

    public TestDestination(@NotNull MultiverseCoreAPI api) {
        super(api);
    }

    @Nullable
    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        return null;
    }

    // TODO: We need to implement cardinal directions
    private static final Pattern PATTERN = Pattern.compile("world:(?<world>[^:]+)");

    @Override
    public String getDestinationString() {
        return DestinationUtil.colonJoin("test:a");
    }

    @Override
    public boolean equals(final Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return  super.hashCode();
    }

    static class Factory implements DestinationFactory {

        @NotNull
        @Override
        public Destination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException {
            destinationString = DestinationUtil.removePrefix(destinationString);

            /* We'll need pattern matching for cardinal directions
            Matcher m = PATTERN.matcher(destinationString);
            if (!m.matches()) {
                return false;
            }
            */

            return new TestDestination(api);
        }

        @NotNull
        @Override
        public Set<String> getDestinationPrefixes() {
            return PREFIXES;
        }

        @NotNull
        @Override
        public Set<String> getWeakPrefixes() {
            return WEAK_PREFIXES;
        }
    }
}
