package com.mvplugin.core.destination;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvplugin.core.destination.Util.numberRegex;

/**
 * A destination that simply teleports to fixed {@link EntityCoordinates}.
 */
public final class EntityCoordinatesDestination extends SimpleDestination {
    @NotNull // not completely right for the default constructor, but tryParse has to happen before everything else
    private EntityCoordinates coordinates;

    public EntityCoordinatesDestination() { }
    public EntityCoordinatesDestination(@NotNull final EntityCoordinates coords) {
        this.coordinates = coords;
    }

    @Override
    protected EntityCoordinates getDestination() {
        return this.coordinates;
    }

    // TODO: Decide about pitch+yaw vs vector
    private static final Pattern PATTERN = Pattern.compile("coords:(?<world>[^:]+)"
            + numberRegex("x") + numberRegex("y") + numberRegex("z") + numberRegex("pitch") + numberRegex("yaw"));

    @Override
    public boolean tryParse(final String str) {
        // redundant but faster than the regex and we have to filter out other types as fast as possible
        if (!str.startsWith("coords:"))
            return false;

        final Matcher m = PATTERN.matcher(str);
        if (!m.matches())
            return false;

        try {
            this.coordinates = Locations.getEntityCoordinates(m.group("world"),
                    Double.parseDouble(m.group("x")), Double.parseDouble(m.group("y")), Double.parseDouble(m.group("z")),
                    Float.parseFloat(m.group("pitch")), Float.parseFloat(m.group("yaw")));
            return true;
        } catch (NumberFormatException e) {
            // I have no idea how this could still happen but just in case...
            return false;
        }
    }

    @Override
    public String serialize() {
        EntityCoordinates c = coordinates;
        return Util.colonJoin("coords", c.getWorld(), c.getX(), c.getY(), c.getZ(), c.getPitch(), c.getYaw());
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || ((o != null) && (getClass() == o.getClass())
                && Locations.equal(this.coordinates, ((EntityCoordinatesDestination) o).coordinates));
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
