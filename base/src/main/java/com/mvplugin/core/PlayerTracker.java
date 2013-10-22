package com.mvplugin.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTracker {

    private Map<String, MultiverseWorld> playerWorldMap = new ConcurrentHashMap<String, MultiverseWorld>();

    PlayerTracker() { }

    @Nullable
    public MultiverseWorld getWorld(@NotNull String playerName) {
        return playerWorldMap.get(playerName);
    }

    void playerJoinedMultiverseWorld(@NotNull String playerName, @NotNull MultiverseWorld world) {
        playerWorldMap.put(playerName, world);
    }

    void playerLeftMultiverseWorlds(@NotNull String playerName) {
        playerWorldMap.remove(playerName);
    }
}
