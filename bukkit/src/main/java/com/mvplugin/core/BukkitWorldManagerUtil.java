package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitConvert;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.world.WorldCreationSettings;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.bukkit.config.BukkitConfiguration;
import pluginbase.bukkit.config.YamlConfiguration;
import pluginbase.logging.Logging;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.PluginBaseException;

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

    static final String WORLD_FILE_EXT = ".yml";

    @NotNull
    private final MultiverseCoreBukkitPlugin plugin;
    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    BukkitWorldManagerUtil(@NotNull final MultiverseCoreBukkitPlugin plugin) {
        this.plugin = plugin;
        this.worldsFolder = new File(plugin.getDataFolder(), "worlds");
        if (!worldsFolder.exists()) {
            worldsFolder.mkdirs();
        }
        this.worldPropertiesMap = new HashMap<String, WorldProperties>();
        this.defaultGens = new HashMap<String, String>();
        initializeDefaultWorldGenerators();
    }

    private void initializeDefaultWorldGenerators() {
        File[] files = this.plugin.getServerInterface().getServerFolder().listFiles(new FilenameFilter() {
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
            Logging.warning("Could not read 'bukkit.yml'. Any Default worldgenerators will not be loaded!");
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
                loadableWorldsIterator.remove();
            }
        }

        // Simple Output to the Console to show how many Worlds were loaded.
        Logging.config("Multiverse is now managing: %s", worldAggregator.getCommaSeparatedWorldNames());
        return initialWorldsMap;
    }

    @Nullable
    private MultiverseWorld createMultiverseWorldFromBukkitWorldLogErrors(@NotNull World bukkitWorld) {
        try {
            return getBukkitWorld(bukkitWorld);
        } catch (MultiverseException e) {
            Logging.severe("Multiverse could not initialize loaded Bukkit world '%s'", bukkitWorld.getName());
            return null;
        }
    }

    @Nullable
    private MultiverseWorld loadMultiverseWorldLogErrors(@NotNull String worldName) {
        File worldFile = getWorldFile(worldName);
        try {
            WorldProperties worldProperties = loadOrCreateWorldProperties(worldFile);
            if (worldProperties.isAutoLoad()) {
                return loadWorldFromProperties(worldProperties);
            } else {
                Logging.fine("Not loading '%s' because it is set to autoLoad: false", worldName);
            }
        } catch (MultiverseException e) {
            if (e instanceof WorldCreationException) {
                Logging.getLogger().log(Level.WARNING, String.format("Error while attempting to load world '%s'", worldName), e);
            } else {
                Logging.getLogger().log(Level.WARNING, String.format("Could not load world from file '%s'", worldFile), e);
            }
        }
        return null;
    }

    private File getWorldFile(String worldName) {
        return new File(worldsFolder, worldName + WORLD_FILE_EXT);
    }

    private MultiverseWorld loadWorldFromProperties(WorldProperties properties) throws WorldCreationException {
        WorldCreationSettings settings = new WorldCreationSettings(properties.getName());
        settings.env(properties.getEnvironment());
        settings.generator(properties.getGenerator());
        settings.adjustSpawn(properties.isAdjustSpawn());
        MultiverseWorld world = createWorld(settings);
        world.setAdjustSpawn(settings.adjustSpawn());
        return world;
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
        return isThisAWorld(new File(this.plugin.getServerInterface().getWorldContainer(), name));
    }

    @NotNull
    @Override
    public BundledMessage whatWillThisDelete(@NotNull final String name) {
        final File worldFolder = new File(this.plugin.getServerInterface().getWorldContainer(), name);
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
    private WorldProperties loadOrCreateWorldProperties(@NotNull final File file) throws MultiverseException {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            YamlConfiguration config = BukkitConfiguration.loadYamlConfig(file);
            WorldProperties defaults = new WorldProperties();
            WorldProperties worldProperties = config.getToObject("settings", defaults);
            if (worldProperties == null) {
                worldProperties = defaults;
                config.set("settings", worldProperties);
                config.save(file);
            }
            return worldProperties;
        } catch (IOException e) {
            throw new MultiverseException(Message.bundleMessage(BukkitLanguage.CREATE_WORLD_FILE_ERROR, file), e);
        } catch (PluginBaseException e) {
            throw new MultiverseException(e);
        }
    }

    @NotNull
    @Override
    public WorldProperties getWorldProperties(@NotNull String worldName) throws MultiverseException {
        worldName = getCorrectlyCasedWorldName(worldName);
        if (this.worldPropertiesMap.containsKey(worldName)) {
            return this.worldPropertiesMap.get(worldName);
        } else {
            final WorldProperties worldProperties = loadOrCreateWorldProperties(new File(worldsFolder, worldName + WORLD_FILE_EXT));
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
        for (final File file : Bukkit.getWorldContainer().listFiles()) {
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
                Logging.finest("Found appropriately cased world name '%s'=>'%s'", worldName, propsName);
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
        Logging.fine("Removed world properties for world '%s'", worldName);
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
            Logging.fine("Creating bukkit world '%s'...", settings.name());
            final World w = c.createWorld();
            return getBukkitWorld(w);
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
        return this.plugin.getServer().unloadWorld(world.getName(), true);
    }

    @NotNull
    @Override
    public String getSafeWorldName() {
        return Bukkit.getWorlds().get(0).getName();
    }

    @Override
    public void deleteWorld(@NotNull final String name) throws IOException {
        final File worldFile = new File(Bukkit.getWorldContainer(), name);
        Logging.fine("Attempting to delete %s", worldFile);
        FileUtils.deleteDirectory(worldFile);
    }

    private static class InitialWorldAggregator {

        private static final int WORLD_SET_SIZE_FACTOR = 3;

        private File worldsFolder;
        private Set<String> initialWorldNames;
        private Set<String> notLoadedWorlds;

        InitialWorldAggregator(File worldsFolder) {
            this.worldsFolder = worldsFolder;
            aggregateInitialWorlds();
        }

        public int getNumberOfPotentialWorlds() {
            return initialWorldNames.size();
        }

        public Iterator<String> getLoadableWorldsIterator() {
            final Iterator<String> iterator = initialWorldNames.iterator();
            return new Iterator<String>() {
                String current = null;
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public String next() {
                    current = iterator.next();
                    return current;
                }

                @Override
                public void remove() {
                    iterator.remove();
                    notLoadedWorlds.add(current);
                }
            };
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
            notLoadedWorlds = new HashSet<String>(initialWorldNames.size());
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
