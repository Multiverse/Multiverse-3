package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.mvplugin.core.WorldCreationException;
import com.mvplugin.core.minecraft.Generator;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Multiverse 2 World Manager API
 * <p>
 * This API contains all of the world managing
 * functions that your heart desires!
 */
public interface WorldManager {

    class WorldCreationSettings {

        private final String name;
        private WorldEnvironment env;
        private Long seed;
        private WorldType type;
        private Boolean generateStructures;
        private String generator;
        private boolean adjustSpawn = true;

        public WorldCreationSettings(final String name) {
            if (name == null) {
                throw new IllegalArgumentException("name cannot be null!");
            }
            this.name = name;
        }

        @NotNull
        public String name() {
            return name;
        }

        @Nullable
        public WorldEnvironment env() {
            return env;
        }

        @Nullable
        public Long seed() {
            return seed;
        }

        @Nullable
        public WorldType type() {
            return type;
        }

        @Nullable
        public Boolean generateStructures() {
            return generateStructures;
        }

        @Nullable
        public String generator() {
            return generator;
        }

        public boolean adjustSpawn() {
            return adjustSpawn;
        }

        @NotNull
        public WorldCreationSettings env(@Nullable final WorldEnvironment e) {
            this.env = e;
            return this;
        }

        @NotNull
        public WorldCreationSettings seed(@Nullable final Long l) {
            this.seed = l;
            return this;
        }

        @NotNull
        public WorldCreationSettings type(@Nullable final WorldType t) {
            this.type = t;
            return this;
        }

        @NotNull
        public WorldCreationSettings generateStructures(@Nullable final Boolean b) {
            this.generateStructures = b;
            return this;
        }

        @NotNull
        public WorldCreationSettings generator(@Nullable final String g) {
            this.generator = g;
            return this;
        }

        @NotNull
        public WorldCreationSettings adjustSpawn(final boolean a) {
            this.adjustSpawn = a;
            return this;
        }
    }

    /**
     * Add a new World to the Multiverse Setup.
     *
     * @param name               World Name
     * @param env                Environment Type
     * @param seedString         The seed in the form of a string.
     *                             If the seed is a Long,
     *                             it will be interpreted as such.
     * @param type               The Type of the world to be made.
     * @param generateStructures If true, this world will get NPC villages.
     * @param generator          The Custom generator plugin to use.
     * @return The new world.
     * @throws WorldCreationException If world creation fails.
     */
    @NotNull
    MultiverseWorld addWorld(@NotNull final String name,
                             @Nullable final WorldEnvironment env,
                             @Nullable final String seedString,
                             @Nullable final WorldType type,
                             @Nullable final Boolean generateStructures,
                             @Nullable final String generator) throws WorldCreationException;

    /**
     * Add a new World to the Multiverse Setup.
     *
     * @param name               World Name
     * @param env                Environment Type
     * @param seedString         The seed in the form of a string.
     *                             If the seed is a Long,
     *                             it will be interpreted as such.
     * @param type               The Type of the world to be made.
     * @param generateStructures If true, this world will get NPC villages.
     * @param generator          The Custom generator plugin to use.
     * @param useSpawnAdjust If true, multiverse will search for a safe spawn. If not, It will not modify the level.dat.
     * @return The new world.
     * @throws WorldCreationException If world creation fails.
     */
    @NotNull
    MultiverseWorld addWorld(@NotNull final String name,
                             @Nullable final WorldEnvironment env,
                             @Nullable final String seedString,
                             @Nullable final WorldType type,
                             @Nullable final Boolean generateStructures,
                             @Nullable final String generator,
                             final boolean useSpawnAdjust) throws WorldCreationException;

    /**
     * Adds a new World to the Multiverse Setup.
     * @param settings Settings for the world creation.
     * @return The new world.
     * @throws WorldCreationException If world creation fails.
     */
    @NotNull
    MultiverseWorld addWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException;

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
     * Checks if Multiverse is managing the given world by name.
     *
     * @param name The name of the world to check for.
     * @return True if Multiverse is managing this world.
     */
    boolean isManaged(@NotNull final String name);

    /**
     * Gets the managed world by the given name (or alias).
     *
     * Worlds managed by Multiverse that are not loaded will not be returned with this method.
     * //TODO explain how to load said worlds.
     *
     * @param name The name or alias of the world to get.
     * @return The world Multiverse is managing or null if Multiverse does not manage a world by the given name
     * or alias.
     */
    @Nullable
    MultiverseWorld getWorld(@NotNull final String name);

    /**
     * Returns a collection of all the loaded worlds managed by Multiverse.
     *
     * @return A collection of all the loaded worlds managed by Multiverse.
     */
    @NotNull
    Collection<MultiverseWorld> getWorlds();

    /**
     * Make a copy of a world.
     *
     * @param oldName            Name of world to be copied
     * @param newName            Name of world to be created
     * @param generator          The Custom generator plugin to use.
     * @return True if the world is copied successfully, false if not.
     */
    //TODO boolean cloneWorld(String oldName, String newName, String generator);

    /**
     * Remove the world from the Multiverse list, from the
     * config and deletes the folder.
     *
     * @param name The name of the world to remove
     * @return True if success, false if failure.
     */
    //TODO boolean deleteWorld(String name);

    /**
     * Remove the world from the Multiverse list, from the
     * config if wanted, and deletes the folder.
     *
     * @param name         The name of the world to remove
     * @param removeConfig If true(default), we'll remove the entries from the
     *                     config. If false, they'll stay and the world may come back.
     * @return True if success, false if failure.
     */
    //TODO boolean deleteWorld(String name, boolean removeConfig);

    /**
     *
     * @param name The name of the world to remove
     * @param removeFromConfig If true(default), we'll remove the entries from the
     *                         config. If false, they'll stay and the world may come back.
     * @param deleteWorldFolder If true the world folder will be completely deleted. If false
     *                          only the contents of the world folder will be deleted
     * @return True if success, false if failure.
     */
    //TODO boolean deleteWorld(String name, boolean removeFromConfig, boolean deleteWorldFolder);

    /**
     * Unload a world from Multiverse. TODO better docs
     *
     * @param name Name of the world to unload
     * @return True if the world was unloaded, false if not.
     */
    boolean unloadWorld(@NotNull final String name);

    //TODO docs
    boolean unloadWorld(@NotNull final MultiverseWorld world);

    /**
     * Removes all players from the specified world. // TODO better docs
     *
     * @param name World to remove players from.
     * @param name World to remove players from.
     */
    // TODO make this accept MultiverseWorld only?
    void removePlayersFromWorld(@NotNull final String name);

    /**
     * Test if a given chunk generator is valid.
     *
     * @param generator   The generator name.
     * @param generatorID The generator id.
     * @param worldName   The worldName to use as the default.
     * @return A {@link Generator} or null
     */
    //TODO Generator getChunkGenerator(String generator, String generatorID, String worldName);

    /**
     * Load the Worlds & Settings from the configuration file.
     *
     * @param forceLoad If set to true, this will perform a total
     *                  reset and not just load new worlds.
     */
    //TODO void loadWorlds(boolean forceLoad);

    /**
     * Loads the Worlds & Settings for any worlds that bukkit loaded before us.
     * <p>
     * This way people will _always_ have some worlds in the list.
     */
    //TODO void loadDefaultWorlds();

    //TODO WorldPurger getWorldPurger();

    /**
     * Gets the world players will spawn in on first join.
     * Currently this always returns worlds.get(0) from Bukkit.
     *
     * @return A Multiverse world that players will spawn in or null if no MV world has been set.
     */
    //TODO MultiverseWorld getSpawnWorld();

    /**
     * Saves the world config to disk.
     *
     * @return True if success, false if fail.
     */
    //TODO boolean saveWorldsConfig();

    /**
     * Remove the world from the Multiverse list and from the config.
     *
     * @param name The name of the world to remove
     * @return True if success, false if failure.
     */
    //TODO boolean removeWorldFromConfig(String name);

    /**
     * Sets the initial spawn world for new players.
     *
     * @param world The World new players should spawn in.
     */
    //TODO void setFirstSpawnWorld(String world);

    /**
     * Gets the world players should spawn in first.
     *
     * @return The {@link MultiverseWorld} new players should spawn in.
     */
    //TODO MultiverseWorld getFirstSpawnWorld();

    /**
     * Regenerates a world.
     *
     * @param name Name of the world to regenerate
     * @param useNewSeed If a new seed should be used
     * @param randomSeed IF the new seed should be random
     * @param seed The seed of the world.
     *
     * @return True if success, false if fail.
     */
    //TODO boolean regenWorld(String name, boolean useNewSeed, boolean randomSeed, String seed);

    List<String> getUnloadedWorlds();

    class RespawnWorldValidator implements PropertyValidator<String> {

        private final WorldManager worldManager;

        public RespawnWorldValidator(@NotNull final WorldManager worldManager) {
            this.worldManager = worldManager;
        }

        @Override
        public boolean isValid(@Nullable final String s) {
            return s != null && worldManager.isManaged(s);
        }

        @NotNull
        @Override
        public Message getInvalidMessage() {
            return PropertyDescriptions.INVALID_RESPAWN_WORLD;
        }
    }
}
