package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.Convert;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldProperties;
import com.mvplugin.core.world.validators.RespawnWorldValidator;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

class BukkitWorldUtil implements WorldUtil {

    @NotNull
    private final MultiverseCorePlugin plugin;
    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    BukkitWorldUtil(@NotNull final MultiverseCorePlugin plugin) {
        this.plugin = plugin;
        this.worldsFolder = new File(plugin.getDataFolder(), "worlds");
        this.worldPropertiesMap = new HashMap<String, WorldProperties>();
        this.defaultGens = new HashMap<String, String>();
        initializeDefaultWorldGenerators();
    }

    private void initializeDefaultWorldGenerators() {
        File[] files = this.plugin.getServerFolder().listFiles(new FilenameFilter() {
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
                initialWorlds.put(world.getName(), world);
                if (builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(w.getName());
            } catch (IOException e) {
                Logging.severe("Multiverse could not initialize loaded Bukkit world '%s'", w.getName());
            }
        }

        for (File file : getPotentialWorldFiles()) {
            final String worldName = getWorldNameFromFile(file);
            if (initialWorlds.containsKey(worldName)) {
                continue;
            }
            try {
                final WorldProperties worldProperties = getWorldProperties(file);
                if (worldProperties.get(WorldProperties.AUTO_LOAD)) {
                    final WorldCreationSettings settings = new WorldCreationSettings(worldName);
                    settings.env(worldProperties.get(WorldProperties.ENVIRONMENT));
                    settings.generator(worldProperties.get(WorldProperties.GENERATOR));
                    settings.adjustSpawn(worldProperties.get(WorldProperties.ADJUST_SPAWN));
                    final MultiverseWorld mvWorld = createWorld(settings);
                    mvWorld.setAdjustSpawn(settings.adjustSpawn());
                    initialWorlds.put(mvWorld.getName(), mvWorld);
                    if (builder.length() != 0) {
                        builder.append(", ");
                    }
                    builder.append(worldName);
                } else {
                    Logging.fine("Not loading '%s' because it is set to autoLoad: false", worldName);
                }
            } catch (IOException e) {
                Logging.getLogger().log(Level.WARNING, String.format("Could not load world from file '%s'", file), e);
            } catch (WorldCreationException e) {
                Logging.getLogger().log(Level.WARNING, String.format("Error while attempting to load world '%s'", worldName), e);
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
    private WorldProperties getWorldProperties(@NotNull final File file) throws IOException {
        final YamlWorldProperties worldProperties = new YamlWorldProperties(file);
        worldProperties.setPropertyValidator(WorldProperties.RESPAWN_WORLD, new RespawnWorldValidator(plugin));
        return worldProperties;
    }

    @NotNull
    @Override
    public WorldProperties getWorldProperties(@NotNull String worldName) throws IOException {
        final World world = Bukkit.getWorld(worldName);
        if (world != null) {
            worldName = world.getName();
        }
        if (worldPropertiesMap.containsKey(worldName)) {
            return worldPropertiesMap.get(worldName);
        } else {
            final WorldProperties worldProperties = getWorldProperties(new File(worldsFolder, worldName + ".yml"));
            worldPropertiesMap.put(worldName, worldProperties);
            return worldProperties;
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (Bukkit.getWorld(settings.name()) != null) {
            throw new WorldCreationException(new BundledMessage(BukkitLanguage.ALREADY_BUKKIT_WORLD, settings.name()));
        }

        final WorldCreator c = WorldCreator.name(settings.name());
        final WorldEnvironment env = settings.env();
        if (env != null) {
            c.environment(Convert.toBukkit(env));
        }
        final WorldType type = settings.type();
        if (type != null) {
            c.type(Convert.toBukkit(type));
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
                throw new WorldCreationException(new BundledMessage(BukkitLanguage.WGEN_UNKNOWN_GENERATOR, settings.generator()));
            } else if (!plugin.isEnabled()) {
                throw new WorldCreationException(new BundledMessage(BukkitLanguage.WGEN_DISABLED_GENERATOR, settings.generator()));
            } else {
                c.generator(plugin.getDefaultWorldGenerator(settings.name(), id));
            }
        }

        try {
            final World w = c.createWorld();
            return getBukkitWorld(w);
        } catch (Exception e) {
            throw new WorldCreationException(new BundledMessage(BukkitLanguage.CREATE_WORLD_ERROR, settings.name()), e);
        }
    }

    @NotNull
    private MultiverseWorld getBukkitWorld(@NotNull final World world) throws IOException {
        final WorldProperties properties = getWorldProperties(world.getName());
        return new DefaultMultiverseWorld(properties, new BukkitWorldLink(world, properties));
    }

    @NotNull
    public List<String> getUnloadedWorlds() {
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
}
