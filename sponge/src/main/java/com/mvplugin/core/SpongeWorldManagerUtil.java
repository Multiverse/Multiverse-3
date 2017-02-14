package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.WorldCreationSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.plugin.ServerInterface;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

class SpongeWorldManagerUtil extends WorldManagerUtil {

    SpongeWorldManagerUtil(@NotNull ServerInterface serverInterface, @NotNull File pluginDataFolder) {
        super(serverInterface, pluginDataFolder);
    }

    @Override
    Map<String, String> getDefaultWorldGenerators() {
        return null;
    }

    @NotNull
    @Override
    protected InitialWorldAggregator createInitialWorldAggregator(@NotNull File worldsFolder) {
        return null;
    }

    @Nullable
    @Override
    protected MultiverseWorld createMultiverseWorldByName(@NotNull String worldName) {
        return null;
    }

    @Nullable
    @Override
    protected String getCorrectlyCasedWorldNameFromServer(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public Collection<String> getPotentialWorlds() {
        return null;
    }

    @NotNull
    @Override
    MultiverseWorld createWorld(@NotNull WorldCreationSettings settings) throws WorldCreationException {
        return null;
    }

    @Override
    boolean unloadWorldFromServer(@NotNull MultiverseWorld world) {
        return false;
    }

    @NotNull
    @Override
    String getSafeWorldName() {
        return null;
    }

    @Override
    void deleteWorld(@NotNull String name) throws IOException {

    }

    @NotNull
    @Override
    UUID getUUID(@NotNull String name) {
        return null;
    }
}
