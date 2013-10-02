package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.world.WorldCreationSettings;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.PluginBaseException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

interface WorldManagerUtil {

    // Names should be lower case
    @NotNull
    Map<String, MultiverseWorld> loadInitialWorlds();

    /**
     * Gets an existing WorldProperties object or creates a new one based on the name.
     *
     * @param worldName The name of the world to get properties for.
     * @return The world properties for the given world name.
     * @throws PluginBaseException In case there are any issues accessing the persistence for the world properties.
     */
    @NotNull
    WorldProperties getWorldProperties(@NotNull final String worldName) throws PluginBaseException;

    void removeWorldProperties(@NotNull final String worldName) throws PluginBaseException;

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
    Collection<String> getManagedWorldNames();

    /**
     * A very basic check to see if a world with the given name exists on the server.
     *
     * To clarify, this does not mean the world is loaded, just that persistence for the minecraft world exists.
     *
     * @param name The name that may be a world.
     * @return True if it looks like a world, false if not.
     */
    boolean isThisAWorld(@NotNull final String name);

    @NotNull
    BundledMessage whatWillThisDelete(@NotNull final String name);

    @NotNull
    Collection<String> getPotentialWorlds();

    void deleteWorld(@NotNull final String name) throws IOException;

    @NotNull
    String getCorrectlyCasedWorldName(@NotNull final String name);

    void saveWorld(@NotNull MultiverseWorld worldProperties) throws MultiverseException;
}
