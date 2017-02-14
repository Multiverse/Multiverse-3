package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.util.CoreLogger;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.world.WorldCreationSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.datasource.DataSource;
import pluginbase.config.datasource.hocon.HoconDataSource;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.PluginBaseException;
import pluginbase.plugin.ServerInterface;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

abstract class WorldManagerUtil {

    static final String WORLD_FILE_EXT = ".conf";

    @NotNull
    protected final ServerInterface serverInterface;
    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    public WorldManagerUtil(@NotNull ServerInterface serverInterface, @NotNull File pluginDataFolder) {
        this.serverInterface = serverInterface;
        this.worldsFolder = new File(pluginDataFolder, "worlds");
        if (!worldsFolder.exists()) {
            worldsFolder.mkdirs();
        }
        this.worldPropertiesMap = new HashMap<>();
        this.defaultGens = getDefaultWorldGenerators();
        getDefaultWorldGenerators();
    }

    abstract Map<String, String> getDefaultWorldGenerators();

    // Names should be lower case
    @NotNull
    Map<String, MultiverseWorld> loadInitialWorlds() {
        InitialWorldAggregator worldAggregator = createInitialWorldAggregator(worldsFolder);
        Map<String, MultiverseWorld> initialWorldsMap = new HashMap<>(worldAggregator.getNumberOfPotentialWorlds());
        Iterator<String> loadableWorldsIterator = worldAggregator.getLoadableWorldsIterator();
        while (loadableWorldsIterator.hasNext()) {
            String worldName = loadableWorldsIterator.next();
            MultiverseWorld multiverseWorld = createMultiverseWorldByName(worldName);
            if (multiverseWorld != null) {
                initialWorldsMap.put(multiverseWorld.getName().toLowerCase(), multiverseWorld);
            } else {
                cacheNotLoadedWorldPropertiesLogErrors(worldName);
            }
        }

        // Simple Output to the Console to show how many Worlds were loaded.
        CoreLogger.config("Multiverse is now managing: %s", worldAggregator.getCommaSeparatedWorldNames());
        return initialWorldsMap;
    }

    @NotNull
    protected abstract InitialWorldAggregator createInitialWorldAggregator(@NotNull File worldsFolder);

    @Nullable
    protected abstract MultiverseWorld createMultiverseWorldByName(@NotNull String worldName);

    @Nullable
    protected MultiverseWorld loadMultiverseWorldLogErrors(@NotNull String worldName) {
        try {
            WorldProperties worldProperties = loadOrCreateWorldProperties(worldName);
            if (worldProperties.isAutoLoad()) {
                return loadWorldFromProperties(worldProperties);
            } else {
                CoreLogger.fine("Not loading '%s' because it is set to autoLoad: false", worldName);
            }
        } catch (MultiverseException e) {
            if (e instanceof WorldCreationException) {
                CoreLogger.getLogger().log(Level.WARNING, String.format("Error while attempting to load world '%s'", worldName), e);
            } else {
                CoreLogger.getLogger().log(Level.WARNING, String.format("Could not load world '%s' from file '%s", worldName, getWorldFile(worldName)), e);
            }
        }
        return null;
    }

    @NotNull
    private WorldProperties loadOrCreateWorldProperties(@NotNull final String worldName) throws MultiverseException {
        File file = getWorldFile(worldName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            DataSource dataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(file).build();
            WorldProperties defaults = new WorldProperties(worldName);
            WorldProperties worldProperties = dataSource.loadToObject(defaults);
            if (worldProperties == null) {
                worldProperties = defaults;
            }
            saveWorldProperties(worldProperties, dataSource, file);
            return worldProperties;
        } catch (IOException e) {
            throw new MultiverseException(Message.bundleMessage(Language.WORLD_SAVE_FILE_ERROR, file), e);
        } catch (PluginBaseException e) {
            if (e instanceof MultiverseException) {
                throw (MultiverseException) e;
            }
            throw new MultiverseException(e);
        }
    }

    private void saveWorldProperties(@NotNull WorldProperties properties, @NotNull DataSource dataSource, @NotNull File file) throws MultiverseException {
        try {
            dataSource.save(properties);
        } catch (PluginBaseException e) {
            throw new MultiverseException(Message.bundleMessage(Language.WORLD_SAVE_FILE_ERROR, file), e);
        }
    }

    private MultiverseWorld loadWorldFromProperties(WorldProperties properties) throws WorldCreationException {
        WorldCreationSettings settings = new WorldCreationSettings(properties.getName());
        settings.env(properties.getEnvironment());
        settings.generator(properties.getGenerator());
        settings.adjustSpawn(properties.isAdjustingSpawn());
        MultiverseWorld world = createWorld(settings);
        world.setAdjustSpawn(settings.adjustSpawn());
        return world;
    }

    private void cacheNotLoadedWorldPropertiesLogErrors(String worldName) {
        try {
            getWorldProperties(worldName);
        } catch (MultiverseException e) {
            CoreLogger.getLogger().log(Level.WARNING, String.format("Could not cache unloaded world '%s'", worldName), e);
        }
    }

    private File getWorldFile(String worldName) {
        if (worldName.isEmpty()) {
            throw new RuntimeException("Can't have blank world name!");
        }
        return new File(worldsFolder, worldName + WORLD_FILE_EXT);
    }

    /**
     * Gets an existing WorldProperties object or creates a new one based on the name.
     *
     * @param worldName The name of the world to get properties for.
     * @return The world properties for the given world name.
     * @throws MultiverseException In case there are any issues accessing the persistence for the world properties.
     */
    @NotNull
    public WorldProperties getWorldProperties(@NotNull String worldName) throws MultiverseException {
        worldName = getCorrectlyCasedWorldName(worldName);
        if (this.worldPropertiesMap.containsKey(worldName)) {
            return this.worldPropertiesMap.get(worldName);
        } else {
            final WorldProperties worldProperties = loadOrCreateWorldProperties(worldName);
            worldPropertiesMap.put(worldName, worldProperties);
            return worldProperties;
        }
    }

    @NotNull
    public String getCorrectlyCasedWorldName(@NotNull final String name) {
        String correctName = getCorrectlyCasedWorldNameFromServer(name);
        if (correctName == null) {
            for (final String propsName : this.worldPropertiesMap.keySet()) {
                if (name.equalsIgnoreCase(propsName)) {
                    return propsName;
                }
            }
        }
        for (final File file : serverInterface.getWorldContainer().listFiles()) {
            if (isThisAWorld(file) && file.getName().equalsIgnoreCase(name)) {
                return file.getName();
            }
        }
        return name;
    }

    @Nullable
    protected abstract String getCorrectlyCasedWorldNameFromServer(@NotNull String name);

    @NotNull
    public abstract Collection<String> getPotentialWorlds();

    /**
     * A very basic check to see if a world with the given name exists on the server.
     *
     * To clarify, this does not mean the world is loaded, just that persistence for the minecraft world exists.
     *
     * @param name The name that may be a world.
     * @return True if it looks like a world, false if not.
     */
    public boolean isThisAWorld(@NotNull final String name) {
        return isThisAWorld(new File(serverInterface.getWorldContainer(), name));
    }

    private static final String WORLD_FILE_NAME = "uid.dat";

    protected boolean isThisAWorld(@NotNull final File worldFolder) {
        if (worldFolder.isDirectory()) {
            File[] files = worldFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, @NotNull String name) {
                    return name.equalsIgnoreCase(WORLD_FILE_NAME);
                }
            });
            if (files != null && files.length > 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public BundledMessage whatWillThisDelete(@NotNull final String name) {
        final File worldFolder = new File(serverInterface.getWorldContainer(), name);
        StringBuilder toDelete = new StringBuilder();
        if (isThisAWorld(worldFolder)) {
            toDelete.append("\n  File: ").append(worldFolder);
        }
        final File persistenceFile = new File(worldsFolder, name + WORLD_FILE_EXT);
        toDelete.append("\n  File: ").append(persistenceFile);
        return Message.bundleMessage(Language.THIS_WILL_DELETE_THE_FOLLOWING, toDelete.toString());
    }

    public void removeWorldProperties(@NotNull String worldName) throws MultiverseException {
        for (final String propsName : this.worldPropertiesMap.keySet()) {
            if (worldName.equalsIgnoreCase(propsName)) {
                CoreLogger.finest("Found appropriately cased world name '%s'=>'%s'", worldName, propsName);
                worldName = propsName;
                break;
            }
        }
        final File file = new File(worldsFolder, worldName + WORLD_FILE_EXT);
        if (!file.exists()) {
            throw new MultiverseException(Message.bundleMessage(Language.WORLD_FILE_NOT_FOUND, file));
        }
        if (!file.delete()) {
            throw new MultiverseException(Message.bundleMessage(Language.WORLD_COULD_NOT_DELETE_FILE, file));
        }
        CoreLogger.fine("Removed world properties for world '%s'", worldName);
        this.worldPropertiesMap.remove(worldName);
    }

    @NotNull
    public List<String> getManagedWorldNames() {
        return Collections.unmodifiableList(new ArrayList<String>(worldPropertiesMap.keySet()));
    }

    public void saveWorld(@NotNull MultiverseWorld world) throws MultiverseException {
        File worldFile = getWorldFile(world.getName());
        DataSource dataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(worldFile).build();
        saveWorldProperties(world.getProperties(), dataSource, worldFile);
    }

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
    abstract MultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException;

    abstract boolean unloadWorldFromServer(@NotNull final MultiverseWorld world);

    @NotNull
    abstract String getSafeWorldName();

    abstract void deleteWorld(@NotNull final String name) throws IOException;

    /**
     * Returns the UUID of the given world name, if the world exists. Otherwise, returns a UUID based on the given name.
     */
    @NotNull
    abstract UUID getUUID(@NotNull final String name);

    abstract static class InitialWorldAggregator {

        private static final int WORLD_SET_SIZE_FACTOR = 3;

        private final File worldsFolder;
        private Set<String> initialWorldNames;

        InitialWorldAggregator(@NotNull File worldsFolder) {
            this.worldsFolder = worldsFolder;
            aggregateInitialWorlds();
        }

        public int getNumberOfPotentialWorlds() {
            return initialWorldNames.size();
        }

        public Iterator<String> getLoadableWorldsIterator() {
            return initialWorldNames.iterator();
        }

        public String getCommaSeparatedWorldNames() {
            StringBuilder worldNameCSV = new StringBuilder();
            for (String worldName : initialWorldNames) {
                if (worldNameCSV.length() != 0) {
                    worldNameCSV.append(", ");
                }
                worldNameCSV.append(worldName);
            }
            return worldNameCSV.toString();
        }

        private void aggregateInitialWorlds() {
            initialWorldNames = new HashSet<String>(getNumberOfLoadedWorlds() * WORLD_SET_SIZE_FACTOR);
            aggregateAlreadyLoadedWorlds();
            aggregatePotentialWorlds();
        }

        protected abstract int getNumberOfLoadedWorlds();

        protected abstract void aggregateAlreadyLoadedWorlds();

        protected void addWorld(String worldName) {
            initialWorldNames.add(worldName);
        }

        private void aggregatePotentialWorlds() {
            for (File file : getPotentialWorldFiles()) {
                String worldName = getWorldNameFromFile(file);
                if (!initialWorldNames.contains(worldName)) {
                    addWorld(worldName);
                }
            }
        }

        private File[] getPotentialWorldFiles() {
            return worldsFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(final File dir, @NotNull final String name) {
                    return name.endsWith(WORLD_FILE_EXT);
                }
            });
        }

        private String getWorldNameFromFile(@NotNull final File file) {
            final String simpleName = file.getName();
            if (simpleName.endsWith(WORLD_FILE_EXT)) {
                return simpleName.substring(0, simpleName.indexOf(WORLD_FILE_EXT));
            }
            return simpleName;
        }
    }
}
