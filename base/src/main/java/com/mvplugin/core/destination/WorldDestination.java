package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.world.MultiverseWorld;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This destination type teleports to a world's spawnpoint.
 * It can also load the destination world.
 */
public final class WorldDestination extends SimpleDestination {
    private static final Message CANT_LOAD = Message.createMessage("destination.world.cantload",
            "$-$*Multiverse could not load the destination world '$v%s$-$*'!");
    private static final Message NOT_LOADED = Message.createMessage("destination.world.notloaded",
            "$-$*The destination world '$v%s$-$*' is not loaded!");

    @NotNull
    private String world;
    private boolean load;

    public WorldDestination() { }
    public WorldDestination(@NotNull final String world, final boolean load) {
        this.world = world;
        this.load = load;
    }

    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        MultiverseWorld mvWorld = this.getApi().getWorldManager().getWorld(world);

        if (mvWorld == null)
            if (load)
                try {
                    mvWorld = this.getApi().getWorldManager().loadWorld(world);
                } catch (WorldManagementException e) {
                    throw new TeleportException(Message.bundleMessage(CANT_LOAD, world), e);
                }
            else throw new TeleportException(Message.bundleMessage(NOT_LOADED, world));

        return Locations.getEntityCoordinates(world, mvWorld.getSpawnLocation());
    }

    private static final Pattern PATTERN = Pattern.compile("world:(?<world>[^:]+)(:(?<load>(true)|(false)))?");

    @Override
    public boolean tryParse(final String str) {
        // redundant but faster than the regex and we have to filter out other types as fast as possible
        if (!str.startsWith("world:"))
            return false;

        this.load = true; // default
        Matcher m = PATTERN.matcher(str);
        if (!m.matches())
            return false;

        this.world = m.group("world");
        if (m.group("load") != null)
            this.load = Boolean.parseBoolean(m.group("load"));
        return true;
    }

    @Override
    public String serialize() {
        return Util.colonJoin("world", world, load);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final WorldDestination that = (WorldDestination) o;
        return load == that.load && world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return  31 * world.hashCode() + (load ? 1 : 0);
    }
}
