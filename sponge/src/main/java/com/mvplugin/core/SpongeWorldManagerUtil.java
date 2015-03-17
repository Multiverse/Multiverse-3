package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.WorldCreationSettings;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.PluginBaseException;
import pluginbase.plugin.ServerInterface;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class SpongeWorldManagerUtil implements WorldManagerUtil {

    static final String WORLD_FILE_EXT = ".conf";

    @NotNull
    private final ServerInterface serverInterface;
    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    SpongeWorldManagerUtil(@NotNull ServerInterface serverInterface, @NotNull File pluginDataFolder) {
        this.serverInterface = serverInterface;
        this.worldsFolder = new File(pluginDataFolder, "worlds");
        if (!worldsFolder.exists()) {
            worldsFolder.mkdirs();
        }
        this.worldPropertiesMap = new HashMap<String, WorldProperties>();
        this.defaultGens = new HashMap<String, String>();
    }

    @NotNull
    @Override
    public Map<String, MultiverseWorld> loadInitialWorlds() {
        return null;
    }

    @NotNull
    @Override
    public WorldProperties getWorldProperties(@NotNull String worldName) throws PluginBaseException {
        return null;
    }

    @Override
    public void removeWorldProperties(@NotNull String worldName) throws PluginBaseException {

    }

    @NotNull
    @Override
    public MultiverseWorld createWorld(@NotNull WorldCreationSettings settings) throws WorldCreationException {
        return null;
    }

    @Override
    public boolean unloadWorldFromServer(@NotNull MultiverseWorld world) {
        return false;
    }

    @NotNull
    @Override
    public String getSafeWorldName() {
        return null;
    }

    @NotNull
    @Override
    public Collection<String> getManagedWorldNames() {
        return null;
    }

    @Override
    public boolean isThisAWorld(@NotNull String name) {
        return false;
    }

    @NotNull
    @Override
    public BundledMessage whatWillThisDelete(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public Collection<String> getPotentialWorlds() {
        return null;
    }

    @Override
    public void deleteWorld(@NotNull String name) throws IOException {

    }

    @NotNull
    @Override
    public String getCorrectlyCasedWorldName(@NotNull String name) {
        return null;
    }

    @Override
    public void saveWorld(@NotNull MultiverseWorld worldProperties) throws MultiverseException {

    }
}
