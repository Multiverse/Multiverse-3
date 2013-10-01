package com.mvplugin.core;

import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.FacingCoordinates;

import java.util.Collection;
import java.util.UUID;

interface WorldLink {

    @NotNull
    UUID getUID();

    @NotNull
    String getName();

    @NotNull
    WorldType getType();

    @NotNull
    WorldEnvironment getEnvironment();

    boolean getPVP();

    @NotNull
    Difficulty getDifficulty();

    long getTime();

    FacingCoordinates getSpawnLocation();

    long getSeed();

    boolean getKeepSpawnInMemory();

    @NotNull
    Collection<BasePlayer> getPlayers();

    void setEnableWeather(final boolean enableWeather);

    void setDifficulty(@NotNull final Difficulty difficulty);

    void setPVP(final boolean enablePVP);

    void setSpawnLocation(@NotNull final FacingCoordinates spawnLocation);

    void setTime(final long time);

    void setKeepSpawnInMemory(boolean keepSpawnInMemory);
}
