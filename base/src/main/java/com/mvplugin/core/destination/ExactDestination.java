package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.PermissionException;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Language.Destination.Coordinates;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Message;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.permission.Permissible;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvplugin.core.destination.DestinationUtil.*;

/**
 * A destination that simply teleports to fixed {@link EntityCoordinates}.
 */
public final class ExactDestination extends SimpleDestination {

    static final Set<String> PREFIXES = new CopyOnWriteArraySet<String>() {{
        add("coords");
        add("coordinates");
        add("loc");
        add("location");
        add("l");
        add("e");
        add("exact");
    }};
    @NotNull
    private final EntityCoordinates coordinates;

    ExactDestination(@NotNull MultiverseCoreAPI api, @NotNull EntityCoordinates coords) {
        super(api);
        this.coordinates = coords;
    }

    @Override
    @NotNull
    protected EntityCoordinates getDestination() {
        return this.coordinates;
    }

    // TODO: Decide about pitch+yaw vs vector
    // TODO: Turn this into 1 regex
    private static final Pattern FACING_PATTERN = Pattern.compile(colonJoin("(?<world>[^:]+)", numberRegex("x"),
            numberRegex("y"), numberRegex("z"), numberRegex("pitch"), numberRegex("yaw")));
    private static final Pattern LOC_PATTERN = Pattern.compile(colonJoin("(?<world>[^:]+)", numberRegex("x"),
            numberRegex("y"), numberRegex("z")));

    @Override
    public String getDestinationString() {
        EntityCoordinates c = coordinates;
        return DestinationUtil.colonJoin("exact", c.getWorld(), c.getX(), c.getY(), c.getZ(), c.getPitch(), c.getYaw());
    }

    @Override
    protected void checkPermissions(@NotNull Permissible teleporter, @NotNull Permissible teleportee) throws PermissionException {
        super.checkPermissions(teleporter, teleportee);
        if (teleporter.equals(teleportee) && teleportee instanceof Entity) {
            if (!teleporter.hasPerm(Perms.TP_SELF_EXACT)) {
                throw new PermissionException(Message.bundleMessage(Coordinates.NO_PERMISSION, ((Entity) teleportee).getName(),
                        Perms.TP_SELF_EXACT.getName()), Perms.TP_SELF_EXACT);
            }
        } else if (!teleporter.equals(teleportee) && teleportee instanceof Entity) {
            if (!teleporter.hasPerm(Perms.TP_OTHER_EXACT)) {
                throw new PermissionException(Message.bundleMessage(Coordinates.NO_PERMISSION, ((Entity) teleportee).getName(),
                        Perms.TP_OTHER_EXACT.getName()), Perms.TP_OTHER_EXACT);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || ((o != null) && (getClass() == o.getClass())
                && Locations.equal(this.coordinates, ((ExactDestination) o).coordinates));
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    static class Factory implements DestinationFactory {

        @NotNull
        @Override
        public ExactDestination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException {
            destinationString = DestinationUtil.removePrefix(destinationString);

            Matcher m = FACING_PATTERN.matcher(destinationString);
            float pitch = 0, yaw = 0;
            if (!m.matches()) {
                m = LOC_PATTERN.matcher(destinationString);
                if (!m.matches()) {
                    throw new InvalidDestinationException(Message.bundleMessage(Language.Destination.Coordinates.INVALID_COORDS, destinationString));
                }
            } else {
                pitch = Float.parseFloat(m.group("pitch"));
                yaw = Float.parseFloat(m.group("yaw"));
            }

            try {
                EntityCoordinates coordinates = Locations.getEntityCoordinates(m.group("world"),
                        Double.parseDouble(m.group("x")), Double.parseDouble(m.group("y")), Double.parseDouble(m.group("z")),
                        pitch, yaw);
                return new ExactDestination(api, coordinates);
            } catch (NumberFormatException e) {
                // I have no idea how this could still happen but just in case...
                throw new InvalidDestinationException(Message.bundleMessage(Language.Destination.Coordinates.INVALID_COORDS, destinationString));
            }
        }

        @NotNull
        @Override
        public Set<String> getDestinationPrefixes() {
            return PREFIXES;
        }
    }
}
