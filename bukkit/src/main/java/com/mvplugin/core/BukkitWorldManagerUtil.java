package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitConvert;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.CoreLogger;
import com.mvplugin.core.world.WorldCreationSettings;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
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
import java.util.logging.Level;

class BukkitWorldManagerUtil implements WorldManagerUtil {

    static final String WORLD_FILE_EXT = ".conf";

    @NotNull
    private final ServerInterface serverInterface;
    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    BukkitWorldManagerUtil(@NotNull ServerInterface serverInterface, @NotNull File pluginDataFolder) {
        this.serverInterface = serverInterface;
        this.worldsFolder = new File(pluginDataFolder, "worlds");
        if (!worldsFolder.exists()) {
            worldsFolder.mkdirs();
        }
        this.worldPropertiesMap = new HashMap<String, WorldProperties>();
        this.defaultGens = new HashMap<String, String>();
        initializeDefaultWorldGenerators();
    }

    private void initializeDefaultWorldGenerators() {
        File[] files = serverInterface.getServerFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File file, @NotNull final String s) {
                return s.equalsIgnoreCase("bukkit.yml");
            }
        });
        if (files != null && files.length == 1) {
            FileConfiguration bukkitConfig = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(files[0]);
            if (bukkitConfig.isConfigurationSection("worlds")) {
                Set<String> keys = bukkitConfig.getConfigurationSection("worlds").getKeys(false);
                for (String key : keys) {
                    defaultGens.put(key, bukkitConfig.getString("worlds." + key + ".generator", ""));
                }
            }
        } else {
            CoreLogger.warning("Could not read 'bukkit.yml'. Any Default worldgenerators will not be loaded!");
        }
    }

    @NotNull
    @Override
    public Map<String, MultiverseWorld> loadInitialWorlds() {
        InitialWorldAggregator worldAggregator = new InitialWorldAggregator(worldsFolder);
        Map<String, MultiverseWorld> initialWorldsMap = new HashMap<String, MultiverseWorld>(worldAggregator.getNumberOfPotentialWorlds());
        Iterator<String> loadableWorldsIterator = worldAggregator.getLoadableWorldsIterator();
        while (loadableWorldsIterator.hasNext()) {
            String worldName = loadableWorldsIterator.next();
            World bukkitWorld = Bukkit.getWorld(worldName);
            MultiverseWorld multiverseWorld;
            if (bukkitWorld != null) {
                multiverseWorld = createMultiverseWorldFromBukkitWorldLogErrors(bukkitWorld);
            } else {
                multiverseWorld = loadMultiverseWorldLogErrors(worldName);
            }
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

    @Nullable
    private MultiverseWorld createMultiverseWorldFromBukkitWorldLogErrors(@NotNull World bukkitWorld) {
        try {
            return getBukkitWorld(bukkitWorld);
        } catch (MultiverseException e) {
            CoreLogger.severe("Multiverse could not initialize loaded Bukkit world '%s'", bukkitWorld.getName());
            return null;
        }
    }

    @Nullable
    private MultiverseWorld loadMultiverseWorldLogErrors(@NotNull String worldName) {
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

    private File getWorldFile(String worldName) {
        if (worldName.isEmpty()) {
            throw new RuntimeException("Can't have blank world name!");
        }
        return new File(worldsFolder, worldName + WORLD_FILE_EXT);
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

    @NotNull
    @Override
    public Collection<String> getPotentialWorlds() {
        final Collection<String> potentialWorlds = new ArrayList<String>();
        final File worldFolder = Bukkit.getWorldContainer();
        if (worldFolder == null) {
            return potentialWorlds;
        }
        File[] files = worldFolder.listFiles();
        if (files == null) {
            files = new File[0];
        }
        for (final File file : files) {
            if (isThisAWorld(file)) {
                potentialWorlds.add(file.getName());
            }
        }
        return potentialWorlds;
    }

    @Override
    public boolean isThisAWorld(@NotNull final String name) {
        return isThisAWorld(new File(serverInterface.getWorldContainer(), name));
    }

    @NotNull
    @Override
    public BundledMessage whatWillThisDelete(@NotNull final String name) {
        final File worldFolder = new File(serverInterface.getWorldContainer(), name);
        StringBuilder toDelete = new StringBuilder();
        if (isThisAWorld(worldFolder)) {
            toDelete.append("\n  File: ").append(worldFolder);
        }
        final File persistenceFile = new File(worldsFolder, name + WORLD_FILE_EXT);
        toDelete.append("\n  File: ").append(persistenceFile);
        return Message.bundleMessage(BukkitLanguage.THIS_WILL_DELETE_THE_FOLLOWING, toDelete.toString());
    }

    private static final String WORLD_FILE_NAME = "uid.dat";

    private boolean isThisAWorld(@NotNull final File worldFolder) {
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
    private WorldProperties loadOrCreateWorldProperties(@NotNull final String worldName) throws MultiverseException {
        File file = getWorldFile(worldName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            DataSource dataSource = HoconDataSource.builder().setFile(file).build();
            WorldProperties defaults = new WorldProperties(worldName);
            WorldProperties worldProperties = dataSource.loadToObject(defaults);
            if (worldProperties == null) {
                worldProperties = defaults;
            }
            saveWorldProperties(worldProperties, dataSource, file);
            return worldProperties;
        } catch (IOException e) {
            throw new MultiverseException(Message.bundleMessage(BukkitLanguage.SAVE_WORLD_FILE_ERROR, file), e);
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
            throw new MultiverseException(Message.bundleMessage(BukkitLanguage.SAVE_WORLD_FILE_ERROR, file), e);
        }
    }

    @NotNull
    @Override
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
    @Override
    public String getCorrectlyCasedWorldName(@NotNull final String name) {
        final World world = Bukkit.getWorld(name);
        if (world != null) {
            return world.getName();
        } else {
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

    @Override
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
            throw new MultiverseException(Message.bundleMessage(BukkitLanguage.WORLD_FILE_NOT_FOUND, file));
        }
        if (!file.delete()) {
            throw new MultiverseException(Message.bundleMessage(BukkitLanguage.WORLD_COULD_NOT_DELETE_FILE, file));
        }
        CoreLogger.fine("Removed world properties for world '%s'", worldName);
        this.worldPropertiesMap.remove(worldName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (Bukkit.getWorld(settings.name()) != null) {
            throw new WorldCreationException(Message.bundleMessage(BukkitLanguage.ALREADY_BUKKIT_WORLD, settings.name()));
        }

        final WorldCreator c = WorldCreator.name(settings.name());
        final WorldEnvironment env = settings.env();
        if (env != null) {
            c.environment(BukkitConvert.toBukkit(env));
        }
        final WorldType type = settings.type();
        if (type != null) {
            c.type(BukkitConvert.toBukkit(type));
        }
        final Long seed = settings.seed();
        if (seed != null) {
            c.seed(seed);
        }
        final Boolean generateStructures = settings.generateStructures();
        if (generateStructures != null) {
            c.generateStructures(generateStructures);
        }
        final String generator = settings.generator();
        if (generator != null && !generator.isEmpty()) {
            final String[] split = generator.split(":", 2);
            final String id = (split.length > 1) ? split[1] : null;
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);

            if (plugin == null) {
                throw new WorldCreationException(Message.bundleMessage(BukkitLanguage.WGEN_UNKNOWN_GENERATOR, settings.generator()));
            } else if (!plugin.isEnabled()) {
                throw new WorldCreationException(Message.bundleMessage(BukkitLanguage.WGEN_DISABLED_GENERATOR, settings.generator()));
            } else {
                c.generator(plugin.getDefaultWorldGenerator(settings.name(), id));
            }
        }

        try {
            CoreLogger.fine("Creating bukkit world '%s'...", settings.name());
            final World w = c.createWorld();
            MultiverseWorld mvWorld = getBukkitWorld(w);
            mvWorld.setGenerator(settings.generator());
            return mvWorld;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WorldCreationException(Message.bundleMessage(BukkitLanguage.CREATE_WORLD_ERROR, settings.name()), e);
        }
    }

    @NotNull
    private MultiverseWorld getBukkitWorld(@NotNull final World world) throws MultiverseException {
        return new MultiverseWorld(getWorldProperties(world.getName()), new BukkitWorldLink(world));
    }

    @NotNull
    public List<String> getManagedWorldNames() {
        return Collections.unmodifiableList(new ArrayList<String>(worldPropertiesMap.keySet()));
    }

    @Override
    public boolean unloadWorldFromServer(@NotNull final MultiverseWorld world) {
        return Bukkit.unloadWorld(world.getName(), true);
    }

    @NotNull
    @Override
    public String getSafeWorldName() {
        return Bukkit.getWorlds().get(0).getName();
    }

    @Override
    public void deleteWorld(@NotNull final String name) throws IOException {
        final File worldFile = new File(Bukkit.getWorldContainer(), name);
        CoreLogger.fine("Attempting to delete %s", worldFile);
        FileUtils.deleteDirectory(worldFile);
    }

    @Override
    public void saveWorld(@NotNull MultiverseWorld world) throws MultiverseException {
        File worldFile = getWorldFile(world.getName());
        DataSource dataSource = HoconDataSource.builder().setFile(worldFile).build();
        saveWorldProperties(world.getProperties(), dataSource, worldFile);
    }

    private static class InitialWorldAggregator {

        private static final int WORLD_SET_SIZE_FACTOR = 3;

        private File worldsFolder;
        private Set<String> initialWorldNames;

        InitialWorldAggregator(File worldsFolder) {
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
            initialWorldNames = new HashSet<String>(Bukkit.getWorlds().size() * WORLD_SET_SIZE_FACTOR);
            aggregateAlreadyLoadedWorlds();
            aggregatePotentialWorlds();
        }

        private void aggregateAlreadyLoadedWorlds() {
            for (final World world : Bukkit.getWorlds()) {
                addWorld(world.getName());
            }
        }

        private void addWorld(String worldName) {
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
