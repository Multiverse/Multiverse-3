package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Message;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import static com.mvplugin.core.util.Language.Destination.World.*;

/**
 * This destination type teleports to a world's spawnpoint.
 */
public final class WorldDestination extends SimpleDestination {

    static final Set<String> PREFIXES = new CopyOnWriteArraySet<String>() {{
        add("world");
        add("w");
    }};

    @NotNull
    private String world;

    public WorldDestination(@NotNull MultiverseCoreAPI api, @NotNull String world) {
        super(api);
        this.world = world;
    }

    @NotNull
    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        MultiverseWorld mvWorld = this.getApi().getWorldManager().getWorld(world);

        if (mvWorld == null) {
            throw new TeleportException(Message.bundleMessage(NOT_LOADED, world));
        }

        return Locations.getEntityCoordinates(world, mvWorld.getSpawnLocation());
    }

    // TODO: We need to implement cardinal directions
    private static final Pattern PATTERN = Pattern.compile("world:(?<world>[^:]+)");

    @Override
    public String getDestinationString() {
        return DestinationUtil.colonJoin("world", world);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final WorldDestination that = (WorldDestination) o;
        return world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return  world.hashCode();
    }

    static class Factory implements DestinationFactory {

        @NotNull
        @Override
        public WorldDestination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException {
            destinationString = DestinationUtil.removePrefix(destinationString);

            /* We'll need pattern matching for cardinal directions
            Matcher m = PATTERN.matcher(destinationString);
            if (!m.matches()) {
                return false;
            }
            */

            return new WorldDestination(api, destinationString);
        }

        @NotNull
        @Override
        public Set<String> getDestinationPrefixes() {
            return PREFIXES;
        }
    }
}
