package com.mvplugin.core;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

interface WorldManagerUtil {

    // Names should be lower case
    @NotNull
    Map<String, MultiverseWorld> getInitialWorlds();

    /**
     * Gets an existing WorldProperties object or creates a new one based on the name.
     *
     * @param worldName The name of the world to get properties for.
     * @return The world properties for the given world name.
     * @throws java.io.IOException In case there are any issues accessing the persistence for the world properties.
     */
    @NotNull
    WorldProperties getWorldProperties(@NotNull final String worldName) throws IOException;

    void removeWorldProperties(@NotNull final String worldName) throws IOException;

    /**
     * Creates a world with the given properties.
     * </p>
     * If a Minecraft world is already loaded with this name, a WorldCreationException will be thrown with a message
     * stating such.
     * </p>
     * If a Minecraft world already exists but it not loaded, it will be loaded and a Multiverse world will be created
     * to represent it.
     * </p>
     * If no previous Minecraft world exists it will be created and loaded and a Multiverse world will be created to
     * represent it.
     *
     * @param settings The settings to set up the world with.
     * @return The new Multiverse world created to represent the given world.
     * @throws com.mvplugin.core.exceptions.WorldCreationException thrown if anything goes wrong during world creation.
     */
    @NotNull
    MultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException;

    boolean unloadWorldFromServer(@NotNull final MultiverseWorld world);

    @NotNull
    String getSafeWorldName();

    // Names should be lowercase
    @NotNull
    List<String> getManagedWorldNames();
}
