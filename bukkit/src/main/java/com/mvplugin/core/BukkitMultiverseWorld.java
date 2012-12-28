package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.properties.ValueProperty;
import com.mvplugin.core.api.WorldProperties;
import com.mvplugin.core.api.WorldProperties.Spawning.Animals;
import com.mvplugin.core.minecraft.PlayerPosition;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Bukkit implementation of MultiverseWorld which links to a {@link World}.
 *
 * This class may offer additional methods over the standard ones provided by
 * {@link com.mvplugin.core.api.MultiverseWorld} that relate specifically to Bukkit.
 */
public class BukkitMultiverseWorld extends AbstractMultiverseWorld {

    @NotNull
    private final String name;
    @NotNull
    private final UUID worldUID;
    @NotNull
    private final WorldType worldType;

    BukkitMultiverseWorld(@NotNull final World world, @NotNull final WorldProperties worldProperties) {
        super(worldProperties);
        this.name = world.getName();
        this.worldUID = world.getUID();
        this.worldType = Convert.fromBukkit(world.getWorldType());
    }

    @Override
    protected void update(@NotNull final ValueProperty obj) {
        if (obj == WorldProperties.DIFFICULTY) {
            getBukkitWorld().setDifficulty(Convert.toBukkit(getDifficulty()));
        } else if (obj == WorldProperties.ALLOW_WEATHER) {
            if (!isWeatherEnabled()) {
                final World world = getBukkitWorld();
                world.setWeatherDuration(0);
                world.setStorm(false);
                world.setThunderDuration(0);
                world.setThundering(false);
            }
        } else if (obj == WorldProperties.PVP) {
            getBukkitWorld().setPVP(isPVPEnabled());
        } else if (obj == WorldProperties.SPAWN_LOCATION) {
            final PlayerPosition pos = getSpawnLocation();
            getBukkitWorld().setSpawnLocation((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
        } else if (obj == Animals.SPAWN_RATE) {
            //getBukkitWorld().set
        }
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public UUID getWorldUID() {
        return this.worldUID;
    }

    @NotNull
    @Override
    public WorldType getWorldType() {
        return worldType;
    }

    @NotNull
    @Override
    public String getTime() {
        return null;
    }

    @Override
    public boolean setTime(@NotNull final String timeAsString) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the Bukkit {@link World} associated with this MultiverseWorld.
     *
     * @return The Bukkit world associated with this Multiverse world.
     * @throws IllegalStateException Thrown if the reference to the Bukkit world is lost for some reason.
     */
    @NotNull
    public World getBukkitWorld() {
        final World world = Bukkit.getWorld(worldUID);
        if (world == null) {
            throw new IllegalStateException("Multiverse lost track of Bukkit world '" + this.name + "'");
        }
        return world;
    }
}
