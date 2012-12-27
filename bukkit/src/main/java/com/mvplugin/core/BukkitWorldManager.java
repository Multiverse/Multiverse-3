package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.mvplugin.core.api.WorldProperties;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

public class BukkitWorldManager extends AbstractWorldManager {

    @NotNull
    private final MultiverseCorePlugin plugin;

    @NotNull
    private final File worldsFolder;

    @NotNull
    private final Map<String, WorldProperties> worldPropertiesMap;
    @NotNull
    private final Map<String, String> defaultGens;

    BukkitWorldManager(@NotNull MultiverseCorePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.worldsFolder = new File(plugin.getDataFolder(), "worlds");
        this.worldPropertiesMap = new HashMap<String, WorldProperties>();
        this.defaultGens = new HashMap<String, String>();
        initializeDefaultWorldGenerators();
        initializeWorlds();
    }

    @NotNull
    @Override
    public BukkitMultiverseWorld addWorld(@NotNull final String name, @Nullable final WorldEnvironment env, @Nullable final String seedString, @Nullable final WorldType type, @Nullable final Boolean generateStructures, @Nullable final String generator) throws WorldCreationException {
        return (BukkitMultiverseWorld) super.addWorld(name, env, seedString, type, generateStructures, generator);
    }

    @NotNull
    @Override
    public BukkitMultiverseWorld addWorld(@NotNull final String name, @Nullable final WorldEnvironment env, @Nullable final String seedString, @Nullable final WorldType type, @Nullable final Boolean generateStructures, @Nullable final String generator, final boolean useSpawnAdjust) throws WorldCreationException {
        return (BukkitMultiverseWorld) super.addWorld(name, env, seedString, type, generateStructures, generator, useSpawnAdjust);
    }

    @NotNull
    @Override
    public BukkitMultiverseWorld addWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        return (BukkitMultiverseWorld) super.addWorld(settings);
    }

    @Override
    @Nullable
    public BukkitMultiverseWorld getWorld(@NotNull final String name) {
        return (BukkitMultiverseWorld) super.getWorld(name);
    }

    /**
     * Convenience method for retrieving a Multiverse world from a given Bukkit world.
     *
     * @param world The Bukkit world to get the Multiverse world for.
     * @return The Multiverse world associated with the given Bukkit world or null if no match is found.
     */
    @Nullable
    public BukkitMultiverseWorld getWorld(@NotNull final World world) {
        return getWorld(world.getName());
    }

    private void initializeDefaultWorldGenerators() {
        File[] files = this.plugin.getServerFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
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

    private void initializeWorlds() {
        StringBuilder builder = new StringBuilder();
        for (final World w : Bukkit.getWorlds()) {
            try {
                final BukkitMultiverseWorld world = getBukkitWorld(w);
                this.worldsMap.put(world.getName(), world);
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
            if (isManaged(worldName)) {
                continue;
            }
            try {
                final WorldProperties worldProperties = getWorldProperties(file);
                if (worldProperties.get(WorldProperties.AUTO_LOAD)) {
                    addWorld(worldName, worldProperties.get(WorldProperties.ENVIRONMENT), null, null,
                            null, worldProperties.get(WorldProperties.GENERATOR),
                            worldProperties.get(WorldProperties.ADJUST_SPAWN));
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
            public boolean accept(File dir, String name) {
                return name.endsWith(".yml");
            }
        });
    }

    private WorldProperties getWorldProperties(@NotNull final File file) throws IOException {
        final YamlWorldProperties worldProperties = new YamlWorldProperties(file);
        worldProperties.setPropertyValidator(WorldProperties.RESPAWN_WORLD, new RespawnWorldValidator(this));
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
    public BukkitMultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (Bukkit.getWorld(settings.name()) != null) {
            return null;
        }

        final WorldCreator c = WorldCreator.name(settings.name());
        if (settings.env() != null) {
            c.environment(Convert.toBukkit(settings.env()));
        }
        if (settings.type() != null) {
            c.type(Convert.toBukkit(settings.type()));
        }
        if (settings.seed() != null) {
            c.seed(settings.seed());
        }
        if (settings.generateStructures() != null) {
            c.generateStructures(settings.generateStructures());
        }
        if (settings.generator() != null) {
            final String[] split = settings.generator().split(":", 2);
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
    private BukkitMultiverseWorld getBukkitWorld(@NotNull final World world) throws IOException {
        return new BukkitMultiverseWorld(world, getWorldProperties(world.getName()));
    }

    @NotNull
    public List<String> getUnloadedWorlds() {
        return Collections.unmodifiableList(new ArrayList<String>(worldPropertiesMap.keySet()));
    }
}
