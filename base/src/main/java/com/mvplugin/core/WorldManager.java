package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.minecraft.Generator;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.SafeTeleporter;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * Multiverse 2 World Manager API
 * <p>
 * This API contains all of the world managing
 * functions that your heart desires!
 */
public class WorldManager {

    @NotNull
    private final MultiverseCoreAPI api;
    @NotNull
    private final Map<String, MultiverseWorld> worldsMap;
    @NotNull
    private final WorldManagerUtil worldManagerUtil;

    WorldManager(@NotNull final MultiverseCoreAPI api, @NotNull final WorldManagerUtil worldManagerUtil) {
        this.api = api;
        this.worldManagerUtil = worldManagerUtil;
        this.worldsMap = new HashMap<String, MultiverseWorld>();
        this.worldsMap.putAll(worldManagerUtil.getInitialWorlds());
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
    public MultiverseWorld addWorld(@NotNull final String name,
                      @Nullable final WorldEnvironment env,
                      @Nullable final String seedString,
                      @Nullable final WorldType type,
                      @Nullable final Boolean generateStructures,
                      @Nullable final String generator) throws WorldCreationException {
        return this.addWorld(name, env, seedString, type, generateStructures, generator, true);
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
     * @param useSpawnAdjust If true, multiverse will search for a safe spawn. If not, It will not modify the level.dat.
     * @return The new world.
     * @throws WorldCreationException If world creation fails.
     */
    @NotNull
    public MultiverseWorld addWorld(@NotNull final String name,
                      @Nullable final WorldEnvironment env,
                      @Nullable final String seedString,
                      @Nullable final WorldType type,
                      @Nullable final Boolean generateStructures,
                      @Nullable final String generator,
                      boolean useSpawnAdjust) throws WorldCreationException {
        final WorldCreationSettings settings = new WorldCreationSettings(name);
        settings.seed(seedString);
        settings.type(type);
        settings.generateStructures(generateStructures);
        settings.env(env);

        // TODO: Use the fancy kind with the commandSender | dumptruckman has no idea what this means..
        if (generator != null && !generator.isEmpty()) {
            settings.generator(generator);
        }

        settings.adjustSpawn(useSpawnAdjust);

        return addWorld(settings);
    }

    /**
     * Adds a new World to the Multiverse Setup.
     * @param settings Settings for the world creation.
     * @return The new world.
     * @throws WorldCreationException If world creation fails.
     */
    @NotNull
    public MultiverseWorld addWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (this.worldsMap.containsKey(settings.name().toLowerCase())) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_ALREADY_EXISTS, settings.name()));
        }
        MultiverseWorld mvWorld = this.worldManagerUtil.createWorld(settings);
        mvWorld.setAdjustSpawn(settings.adjustSpawn());
        this.worldsMap.put(settings.name().toLowerCase(), mvWorld);
        Logging.fine("World '%s' has been added to multiverse management", settings.name());
        return mvWorld;
    }

    /**
     * Checks if the given world is loaded by Multiverse.
     *
     * @param name The name of the world to check.
     * @return True indicate this world is managed by Multiverse AND is loaded.
     */
    public boolean isLoaded(@NotNull final String name) {
        return this.worldsMap.containsKey(name.toLowerCase());
    }

    /**
     * Checks if Multiverse is managing the given world by name.
     *
     * This will check worlds loaded and unloaded.  Use {@link }
     *
     * @param name The name of the world to check for.
     * @return True if Multiverse is managing this world whether loaded or not.
     */
    public boolean isManaged(@NotNull final String name) {
        final String lowerName = name.toLowerCase();
        return this.worldsMap.containsKey(lowerName) || this.getUnloadedWorlds().contains(lowerName);
    }

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
    public MultiverseWorld getWorld(@NotNull final String name) {
        final MultiverseWorld world = this.worldsMap.get(name.toLowerCase());
        if (world != null) {
            return world;
        }
        for (final MultiverseWorld w : this.worldsMap.values()) {
            if (name.equalsIgnoreCase(w.getAlias())) {
                return w;
            }
        }
        return null;
    }

    /**
     * Returns a collection of all the loaded worlds managed by Multiverse.
     *
     * @return A collection of all the loaded worlds managed by Multiverse.
     */
    @NotNull
    public Collection<MultiverseWorld> getWorlds() {
        return Collections.unmodifiableCollection(this.worldsMap.values());
    }

    /**
     * Loads the world with the given name.
     *
     * The world must have already been imported previously by Multiverse.
     * This basically means this is only to be used if the world was previously unloaded by Multiverse.
     *
     * @param name The name of the world to load.
     * @throws WorldManagementException if the world is already loaded or the world fails to load for some reason.
     * The message of the exception will indicate the exact issue.
     */
    public void loadWorld(@NotNull final String name) throws WorldManagementException {
        if (isLoaded(name)) {
            throw new WorldManagementException(new BundledMessage(Language.WORLD_ALREADY_LOADED, name));
        }
        if (!isManaged(name)) {
            throw new WorldManagementException(new BundledMessage(Language.WORLD_NOT_MANAGED, name));
        }
        try {
            Logging.fine("Loading world '%s'...", name);
            // Transfer all the properties of the world ot a WorldCreationSettings object.
            final WorldProperties properties = this.worldManagerUtil.getWorldProperties(name);
            final WorldCreationSettings settings = new WorldCreationSettings(name);
            settings.generator(properties.get(WorldProperties.GENERATOR));
            settings.seed(properties.get(WorldProperties.SEED));
            settings.env(properties.get(WorldProperties.ENVIRONMENT));
            settings.adjustSpawn(properties.get(WorldProperties.ADJUST_SPAWN));

            try {
                addWorld(settings);
            } catch (final WorldCreationException e) {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_LOAD_ERROR, name), e);
            }
        } catch (final IOException e) {
            throw new WorldManagementException(new BundledMessage(Language.WORLD_LOAD_ERROR, name), e);
        }
    }

    /**
     * Unload a Multiverse managed world from the server.
     *
     * This will remove the world from memory but leave the world properties persistence object in tact.
     * This means that the server implementation shall make the world unreachable.
     *
     * @param name Name of the world to unload.
     * @return true if the world was unloaded or is already unloaded.
     * false if the specified world is not managed by Multiverse.
     * @throws WorldManagementException if there are any problems while attempting to unload the world.
     * The message of the exception will indicate the exact issue.
     */
    public boolean unloadWorld(@NotNull final String name) throws WorldManagementException {
        final MultiverseWorld world = getWorld(name);
        if (world != null) {
            unloadWorld(world);
            return true;
        }
        return false;
    }

    /**
     * Unload a Multiverse managed world from the server.
     *
     * This will remove the world from memory but leave the world properties persistence object in tact.
     * This means that the server implementation shall make the world unreachable.
     * This does nothing if the world is already unloaded.
     *
     * @param world The world to unload.
     * @throws WorldManagementException if there are any problems while attempting to unload the world.
     * The message of the exception will indicate the exact issue.
     */
    public void unloadWorld(@NotNull final MultiverseWorld world) throws WorldManagementException {
        if (isLoaded(world.getName())) {
            try {
                removePlayersFromWorld(world);
            } catch (final TeleportException e) {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_UNLOAD_ERROR, world.getName()), e);
            }
            if (this.worldManagerUtil.unloadWorldFromServer(world)) {
                this.worldsMap.remove(world.getName().toLowerCase());
                Logging.fine("World '%s' was unloaded from memory.", world.getName());
            } else {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_COULD_NOT_UNLOAD_FROM_SERVER, world.getName()));
            }
        }
    }

    /**
     * Removes all players from the specified world. // TODO better docs
     *
     * @param world World to remove players from.
     */
    public void removePlayersFromWorld(@NotNull final MultiverseWorld world) throws TeleportException {
        final MultiverseWorld safeWorld = getSafeWorld();
        final SafeTeleporter teleporter = this.api.getSafeTeleporter();
        final FacingCoordinates sLoc = safeWorld.getSpawnLocation();
        final EntityCoordinates location = Locations.getEntityCoordinates(safeWorld.getName(),
                sLoc.getX(), sLoc.getY(), sLoc.getZ(), sLoc.getPitch(), sLoc.getYaw());
        Logging.fine("Removing players from world '%s'...", world.getName());
        for (final BasePlayer p : world.getPlayers()) {
            if (p instanceof Entity) {
                teleporter.safelyTeleport(null, (Entity) p, location);
            }
        }
    }

    // TODO docs
    @NotNull
    public Collection<String> getUnloadedWorlds() {
        final Collection<String> managedWorlds = this.worldManagerUtil.getManagedWorldNames();
        final List<String> unloadedWorlds = new ArrayList<String>(managedWorlds.size() - getWorlds().size());
        for (final String name : managedWorlds) {
            if (!isLoaded(name)) {
                unloadedWorlds.add(name);
            }
        }
        return Collections.unmodifiableList(unloadedWorlds);
    }

    private MultiverseWorld getSafeWorld() {
        return getWorld(this.worldManagerUtil.getSafeWorldName());
    }

    /**
     * Removes a world from Multiverse's management.
     *
     * This will unload the world from the server and delete the properties of the world.
     * It will not delete the Minecraft world however.
     *
     * @param name The world to remove.
     * @return True if removed.  False if the world is not managed by Multiverse.
     * @throws WorldManagementException If anything goes wrong while attempting to remove the world.
     * The message of the exception will indicate the exact issue.
     */
    public boolean removeWorld(@NotNull final String name) throws WorldManagementException {
        try {
            if (isLoaded(name) && !unloadWorld(name)) {
                // This should be impossible.
                throw new WorldManagementException(new BundledMessage(Language.WORLD_NOT_MANAGED, name));
            }
        } catch (final WorldManagementException e) {
            throw new WorldManagementException(new BundledMessage(Language.WORLD_REMOVE_ERROR, name), e);
        }
        if (isManaged(name)) {
            try {
                this.worldManagerUtil.removeWorldProperties(name);
            } catch (final IOException e) {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_REMOVE_ERROR, name), e);
            }
            return true;
        }
        return false;
    }

    public void deleteWorld(@NotNull final String worldName, final boolean removeMVWorld) throws WorldManagementException {
        if (!isManaged(worldName)) {
            throw new WorldManagementException(new BundledMessage(Language.CANNOT_DELETE_UNMANAGED));
        }
        final String name = this.worldManagerUtil.getCorrectlyCasedWorldName(worldName);
        if (!isThisAWorld(name)) {
            throw new WorldManagementException(new BundledMessage(Language.CANNOT_DELETE_NONWORLD, name));
        }
        if (isLoaded(name)) {
            try {
                unloadWorld(name);
            } catch (final WorldManagementException e) {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_DELETE_ERROR, name), e);
            }
        }
        if (removeMVWorld) {
            try {
                removeWorld(name);
            } catch (final WorldManagementException e) {
                throw new WorldManagementException(new BundledMessage(Language.WORLD_DELETE_ERROR, name), e);
            }
        }
        if (!this.worldManagerUtil.deleteWorld(name)) {
            throw new WorldManagementException(new BundledMessage(Language.WORLD_DELETE_FAILED, name));
        }
    }

    /**
     * A very basic check to see if a world with the given name exists on the server.
     *
     * To clarify, this does not mean the world is loaded, just that persistence for the minecraft world exists.
     *
     * @param name The name that may be a world.
     * @return True if it looks like a world, false if not.
     */
    public boolean isThisAWorld(@NotNull final String name) {
        return this.worldManagerUtil.isThisAWorld(name);
    }

    @NotNull
    public BundledMessage whatWillThisDelete(@NotNull final String name) {
        return this.worldManagerUtil.whatWillThisDelete(name);
    }

    @NotNull
    public Collection<String> getPotentialWorlds() {
        return this.worldManagerUtil.getPotentialWorlds();
    }

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
}
