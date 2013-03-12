package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.bukkit.properties.YamlProperties;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitConvert;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import com.mvplugin.core.world.serializers.BukkitFacingCoordinatesSerializer;
import com.mvplugin.core.world.validators.RespawnWorldValidator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

class BukkitWorldManagerUtil implements WorldManagerUtil {

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
            FileConfiguration bukkitConfig = YamlConfiguration.loadConfiguration(files[0]);
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
    public Map<String, MultiverseWorld> getInitialWorlds() {
        Map<String, MultiverseWorld> initialWorlds = new HashMap<String, MultiverseWorld>(Bukkit.getWorlds().size());
        StringBuilder builder = new StringBuilder();
        for (final World w : Bukkit.getWorlds()) {
            try {
                final MultiverseWorld world = getBukkitWorld(w);
                initialWorlds.put(world.getName().toLowerCase(), world);
                if (builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(w.getName());
            } catch (PluginBaseException e) {
                Logging.severe("Multiverse could not initialize loaded Bukkit world '%s'", w.getName());
            }
        }

        for (File file : getPotentialWorldFiles()) {
            final String worldName = getWorldNameFromFile(file);
            if (initialWorlds.containsKey(worldName.toLowerCase())) {
                continue;
            }
            try {
                final WorldProperties worldProperties = newWorldProperties(file);
                if (worldProperties.get(WorldProperties.AUTO_LOAD)) {
                    final WorldCreationSettings settings = new WorldCreationSettings(worldName);
                    settings.env(worldProperties.get(WorldProperties.ENVIRONMENT));
                    settings.generator(worldProperties.get(WorldProperties.GENERATOR));
                    settings.adjustSpawn(worldProperties.get(WorldProperties.ADJUST_SPAWN));
                    final MultiverseWorld mvWorld = createWorld(settings);
                    mvWorld.setAdjustSpawn(settings.adjustSpawn());
                    initialWorlds.put(mvWorld.getName().toLowerCase(), mvWorld);
                    if (builder.length() != 0) {
                        builder.append(", ");
                    }
                    builder.append(worldName);
                } else {
                    Logging.fine("Not loading '%s' because it is set to autoLoad: false", worldName);
                }
            } catch (MultiverseException e) {
                if (e instanceof WorldCreationException) {
                    Logging.getLogger().log(Level.WARNING, String.format("Error while attempting to load world '%s'", worldName), e);
                } else {
                    Logging.getLogger().log(Level.WARNING, String.format("Could not load world from file '%s'", file), e);
                }
            }
        }

        // Simple Output to the Console to show how many Worlds were loaded.
        Logging.config("Multiverse is now managing: %s", builder.toString());
        return initialWorlds;
    }

    private String getWorldNameFromFile(@NotNull final File file) {
        final String simpleName = file.getName();
        if (simpleName.endsWith(".yml")) {
            return simpleName.substring(0, simpleName.indexOf(".yml"));
        }
        return simpleName;
    }

    private File[] getPotentialWorldFiles() {
        return worldsFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, @NotNull final String name) {
                return name.endsWith(".yml");
            }
        });
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
        final File persistenceFile = new File(worldsFolder, name + ".yml");
        toDelete.append("\n  File: ").append(persistenceFile);
        return Message.bundleMessage(BukkitLanguage.THIS_WILL_DELETE_THE_FOLLOWING, toDelete.toString());
    }

    private static final String WORLD_FILE_NAME = "level.dat";

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
    private WorldProperties newWorldProperties(@NotNull final File file) throws MultiverseException {
        try {
            final DefaultWorldProperties worldProperties = new DefaultWorldProperties(new YamlProperties(false, true, file, WorldProperties.class) {
                @Override
                protected void registerSerializers() {
                    super.registerSerializers();
                    setPropertySerializer(FacingCoordinates.class, new BukkitFacingCoordinatesSerializer());
                }
            });
            worldProperties.setPropertyValidator(WorldProperties.RESPAWN_WORLD, new RespawnWorldValidator(plugin));
            return worldProperties;
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
            final WorldProperties worldProperties = newWorldProperties(new File(worldsFolder, worldName + ".yml"));
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
        final File file = new File(worldsFolder, worldName + ".yml");
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
            throw new WorldCreationException(Message.bundleMessage(BukkitLanguage.CREATE_WORLD_ERROR, settings.name()), e);
        }
    }

    @NotNull
    private MultiverseWorld getBukkitWorld(@NotNull final World world) throws MultiverseException {
        return new DefaultMultiverseWorld(getWorldProperties(world.getName()), new BukkitWorldLink(world));
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
}
