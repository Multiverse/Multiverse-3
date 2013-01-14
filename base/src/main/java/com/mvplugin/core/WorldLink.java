package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;

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
    Collection<BasePlayer> getPlayers();

    void setEnableWeather(final boolean enableWeather);

    void setDifficulty(@NotNull final Difficulty difficulty);

    void setPVP(final boolean enablePVP);

    void setSpawnLocation(@NotNull final FacingCoordinates spawnLocation);
}
