package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.PermissionException;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Language.Destination.Cannon;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.permission.Permissible;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvplugin.core.destination.DestinationUtil.*;

/**
 * A destination that accelerates the player after he was teleported.
 */
public class CannonDestination extends SimpleDestination {

    static final Set<String> PREFIXES = new CopyOnWriteArraySet<String>() {{
        add("cannon");
        add("ca");
    }};

    @Nullable
    private final EntityCoordinates coordinates;
    private final double speed;

    // TODO: Decide about pitch+yaw vs vector
    private static final Pattern LOC_PATTERN = Pattern.compile(colonJoin("(?<world>[^:]+)", numberRegex("x"),
            numberRegex("y"), numberRegex("z"), numberRegex("pitch"), numberRegex("yaw"), numberRegex("speed")));
    private static final Pattern LAUNCH_PATTERN = Pattern.compile(numberRegex("speed"));

    public CannonDestination(@NotNull MultiverseCoreAPI api, @Nullable EntityCoordinates coordinates, double speed) {
        super(api);
        this.coordinates = coordinates;
        this.speed = speed;
    }

    // TODO: These strings are wrong as they need to use speed rather than vector... I think.
    @Override
    @NotNull
    public String getDestinationString() {
        EntityCoordinates c = coordinates;
        if (c != null) {
            return DestinationUtil.colonJoin("cannon", c.getWorld(), c.getX(), c.getY(), c.getZ(), c.getPitch(), c.getYaw(), speed);
        } else {
            return DestinationUtil.colonJoin("cannon", speed);
        }
    }

    @NotNull
    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        if (coordinates != null) {
            return this.coordinates;
        } else {
            throw new TeleportException(Message.bundleMessage(Language.Destination.Cannon.LAUNCH_ONLY, getDestinationString()));
        }
    }

    @Override
    public void teleport(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws TeleportException {
        if (coordinates != null) {
            super.teleport(teleporter, teleportee);
            teleportee.setVelocity(coordinates.getDirection().multiply(speed));
        } else {
            try {
                checkPermissions(teleporter, teleportee);
            } catch (PermissionException e) {
                throw new TeleportException(e.getBundledMessage(), e);
            }
            teleportee.setVelocity(teleportee.getLocation().getDirection().multiply(speed));
        }
    }

    @Override
    protected void checkPermissions(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws PermissionException {
        super.checkPermissions(teleporter, teleportee);
        if (teleporter.equals(teleportee)) {
            if (!teleporter.hasPerm(Perms.TP_SELF_CANNON)) {
                throw new PermissionException(Message.bundleMessage(Cannon.NO_PERMISSION, teleportee.getName(),
                        Perms.TP_SELF_CANNON.getName()), Perms.TP_SELF_CANNON);
            }
        } else {
            if (!teleporter.hasPerm(Perms.TP_OTHER_CANNON)) {
                throw new PermissionException(Message.bundleMessage(Cannon.NO_PERMISSION, teleportee.getName(),
                        Perms.TP_OTHER_CANNON.getName()), Perms.TP_OTHER_CANNON);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || ((o != null) && (getClass() == o.getClass())
                && Locations.equal(this.coordinates, ((CannonDestination) o).coordinates)
                && this.speed == ((CannonDestination) o).speed);
    }

    @Override
    public int hashCode() {
        return (coordinates != null ? coordinates.hashCode() * 13 : 0) + Double.hashCode(speed);
    }

    /**
     * Returns the speed at which this cannon destination launches entities.
     *
     * @return the speed at which this cannon destination launches entities.
     */
    public double getLaunchSpeed() {
        return speed;
    }

    static class Factory implements DestinationFactory {
        @NotNull
        @Override
        public CannonDestination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException {
            destinationString = DestinationUtil.removePrefix(destinationString);

            try {
                EntityCoordinates coords = null;
                Matcher m = LOC_PATTERN.matcher(destinationString);
                if (m.matches()) {
                    String worldName = m.group("world");
                    UUID worldUID = api.getWorldManager().getWorldUUID(worldName);
                    coords = Locations.getEntityCoordinates(worldName, worldUID, Double.parseDouble(m.group("x")),
                            Double.parseDouble(m.group("y")), Double.parseDouble(m.group("z")),
                            Float.parseFloat(m.group("pitch")), Float.parseFloat(m.group("yaw")));
                } else {
                    m = LAUNCH_PATTERN.matcher(destinationString);
                    if (!m.matches()) {
                        throw new InvalidDestinationException(Message.bundleMessage(Language.Destination.Cannon.INVALID_COORDS, destinationString));
                    }
                }
                double speed = Double.parseDouble(m.group("speed"));

                return new CannonDestination(api, coords, speed);
            } catch (NumberFormatException e) {
                throw new InvalidDestinationException(Message.bundleMessage(Language.Destination.Cannon.INVALID_COORDS, destinationString));
            }
        }

        @NotNull
        @Override
        public Set<String> getDestinationPrefixes() {
            return PREFIXES;
        }
    }
}
