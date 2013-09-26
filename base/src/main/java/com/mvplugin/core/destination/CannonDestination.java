package com.mvplugin.core.destination;

import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.minecraft.location.Vector;
import pluginbase.permission.Permissible;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvplugin.core.destination.Util.*;

/**
 * A destination that accelerates the player after he was teleported.
 */
public class CannonDestination extends SimpleDestination {
    @NotNull
    private EntityCoordinates coordinates;
    @NotNull
    private Vector velocity;

    // TODO: Decide about pitch+yaw vs vector
    private static final Pattern PATTERN = Pattern.compile("cannon:(?<world>[^:]+)"
            + numberRegex("x") + numberRegex("y") + numberRegex("z") + numberRegex("pitch") + numberRegex("yaw")
            + numberRegex("vx") + numberRegex("vy") + numberRegex("vz"));

    public CannonDestination() { }
    public CannonDestination(@NotNull final EntityCoordinates coordinates, @NotNull final Vector velocity) {
        this.coordinates = coordinates;
        this.velocity = velocity;
    }

    @Override
    public boolean tryParse(final String str) {
        // redundant but faster than the regex and we have to filter out other types as fast as possible
        if (!str.startsWith("cannon:"))
            return false;

        final Matcher m = PATTERN.matcher(str);
        if (!m.matches())
            return false;

        try {
            this.coordinates = Locations.getEntityCoordinates(m.group("world"),
                    Double.parseDouble(m.group("x")), Double.parseDouble(m.group("y")), Double.parseDouble(m.group("z")),
                    Float.parseFloat(m.group("pitch")), Float.parseFloat(m.group("yaw")));
            this.velocity = new Vector(Double.parseDouble(m.group("vx")), Double.parseDouble(m.group("vy")),
                    Double.parseDouble(m.group("vz")));
            return true;
        } catch (NumberFormatException e) {
            // I have no idea how this could still happen but just in case...
            return false;
        }
    }

    @Override
    public String serialize() {
        EntityCoordinates c = coordinates;
        return Util.colonJoin("cannon", c.getWorld(), c.getX(), c.getY(), c.getZ(), c.getPitch(), c.getYaw(),
                velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    protected EntityCoordinates getDestination() {
        return this.coordinates;
    }

    @Override
    public void teleport(final Permissible teleporter, final Permissible teleportee,
                         final Entity teleporteeEntity) throws TeleportException {
        super.teleport(teleporter, teleportee, teleporteeEntity);
        teleporteeEntity.setVelocity(this.velocity);
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || ((o != null) && (getClass() == o.getClass())
                && Locations.equal(this.coordinates, ((CannonDestination) o).coordinates)
                && this.velocity.equals(((CannonDestination) o).velocity));
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode() * 13 + velocity.hashCode();
    }
}
