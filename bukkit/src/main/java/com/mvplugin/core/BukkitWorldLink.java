package com.mvplugin.core;

import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitConvert;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.BukkitTools;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.FacingCoordinates;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

class BukkitWorldLink implements WorldLink {

    @NotNull
    private final WeakReference<World> worldRef;
    @NotNull
    private final WorldType worldType;

    BukkitWorldLink(@NotNull final World world) {
        this.worldRef = new WeakReference<World>(world);
        this.worldType = BukkitConvert.fromBukkit(world.getWorldType());
    }

    @Override
    public void setSpawnLocation(@NotNull final FacingCoordinates pos) {
        getWorld().setSpawnLocation((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
    }

    @Override
    public void setPVP(final boolean enablePVP) {
        getWorld().setPVP(enablePVP);
    }

    @Override
    public void setDifficulty(@NotNull final Difficulty difficulty) {
        getWorld().setDifficulty(BukkitConvert.toBukkit(difficulty));
    }

    @Override
    public void setEnableWeather(final boolean enableWeather) {
        if (!enableWeather) {
            final World world = getWorld();
            world.setWeatherDuration(0);
            world.setStorm(false);
            world.setThunderDuration(0);
            world.setThundering(false);
        }
    }

    @NotNull
    private World getWorld() {
        final World world = this.worldRef.get();
        if (world == null) {
            throw new IllegalStateException("Lost reference to bukkit world.");
        }
        return world;
    }

    @Override
    @NotNull
    public UUID getUID() {
        return getWorld().getUID();
    }

    @Override
    @NotNull
    public String getName() {
        return getWorld().getName();
    }

    @Override
    @NotNull
    public WorldType getType() {
        return this.worldType;
    }

    @NotNull
    @Override
    public WorldEnvironment getEnvironment() {
        return BukkitConvert.fromBukkit(getWorld().getEnvironment());
    }

    @Override
    public long getTime() {
        return getWorld().getTime();
    }

    @Override
    public void setTime(final long time) {
        getWorld().setTime(time);
    }

    @Override
    @NotNull
    public Collection<BasePlayer> getPlayers() {
        final List<Player> players = getWorld().getPlayers();
        final List<BasePlayer> result = new ArrayList<BasePlayer>(players.size());
        for (final Player player : players) {
            result.add(BukkitTools.wrapPlayer(player));
        }
        return result;
    }

    @Override
    public boolean getPVP() {
        return getWorld().getPVP();
    }

    @NotNull
    @Override
    public Difficulty getDifficulty() {
        return BukkitConvert.fromBukkit(getWorld().getDifficulty());
    }

    @Override
    public FacingCoordinates getSpawnLocation() {
        return BukkitConvert.fromBukkit(getWorld().getSpawnLocation());
    }

    @Override
    public long getSeed() {
        return getWorld().getSeed();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return getWorld().getKeepSpawnInMemory();
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepSpawnInMemory) {
        getWorld().setKeepSpawnInMemory(keepSpawnInMemory);
    }
}
