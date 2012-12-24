package com.mvplugin.core;

import com.mvplugin.core.api.MultiverseWorld;
import com.mvplugin.core.api.WorldManager;
import com.mvplugin.core.api.WorldProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

interface WorldFactory {

    /**
     * Tells the WorldFactory to do any initialization of the worlds that is necessary such as loading all of
     * our persisted worlds and tracking all worlds present on the server already.
     */
    void initializeWorlds();

    /**
     * Creates a world with the given properties.
     *
     * If a Minecraft world is already loaded with this name, null will be returned.  If a Minecraft world already
     * exists but it not loaded, it will be loaded instead.
     *
     * @param name The name for the world.  May not be null.
     * @param env The environment for the world.  May not be null.
     * @param seed The seed for the world.  Null means a random seed will be used if creating a new Minecraft world.
     * @param type The world type for the world.  Null means the Minecraft default will be used if creating a new
     *             Minecraft world or, if loading a world, the loaded world's type will be used.
     * @param generateStructures Whether or not to generate structures for the world.  Null means the Minecraft default
     *                           will be used if creating a new world or, if loading a world, the loaded world's
     *                           setting will be used.
     * @param generator The name of the generator for the world.  Null may be used to specify no generator.
     * @return A new {@link W} or null if a Minecraft world by this name is already loaded.
     * @throws WorldCreationException If any problems occured while trying to create the world.
     */

    /**
     * Creates a world with the given properties.
     *
     * If a Minecraft world is already loaded with this name, null will be returned.  If a Minecraft world already
     * exists but it not loaded, it will be loaded instead.
     *
     * @param settings
     * @return
     * @throws WorldCreationException
     */
    @NotNull
    MultiverseWorld createWorld(@NotNull final WorldManager.WorldCreationSettings settings) throws WorldCreationException;

    /**
     * Gets an existing WorldProperties object or creates a new one based on the name.
     *
     * TODO explain that they should use getWorld() in general.
     *
     * @param worldName The name of the world to get properties for.
     * @return The world properties for the given world name.
     * @throws java.io.IOException In case there are any issues accessing the persistence for the world properties.
     */
    @NotNull
    WorldProperties getWorldProperties(@NotNull final String worldName) throws IOException;


    // Leaving in for now
    List<String> getUnloadedWorlds();
}
