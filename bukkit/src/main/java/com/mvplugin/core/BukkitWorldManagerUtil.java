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
import pluginbase.messages.Message;
import pluginbase.plugin.ServerInterface;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class BukkitWorldManagerUtil extends WorldManagerUtil {

    BukkitWorldManagerUtil(@NotNull ServerInterface serverInterface, @NotNull File pluginDataFolder) {
        super(serverInterface, pluginDataFolder);
    }

    Map<String, String> getDefaultWorldGenerators() {
        Map<String, String> defaultGens = new HashMap<>();
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
        return defaultGens;
    }

    @NotNull
    @Override
    protected InitialWorldAggregator createInitialWorldAggregator(@NotNull File worldsFolder) {
        return new BukkitInitialWorldAggregator(worldsFolder);
    }

    @Nullable
    protected MultiverseWorld createMultiverseWorldByName(@NotNull String worldName) {
        World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld != null) {
            return createMultiverseWorldFromBukkitWorldLogErrors(bukkitWorld);
        } else {
            return loadMultiverseWorldLogErrors(worldName);
        }
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

    @Override
    public boolean unloadWorldFromServer(@NotNull final MultiverseWorld world) {
        return Bukkit.unloadWorld(world.getName(), true);
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

    @Nullable
    @Override
    protected String getCorrectlyCasedWorldNameFromServer(@NotNull String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return world.getName();
        }
        return null;
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

    @NotNull
    @Override
    UUID getUUID(@NotNull String name) {
        World world = Bukkit.getWorld(name);
        return world != null ? world.getUID() : UUID.nameUUIDFromBytes(name.getBytes());
    }

    private static class BukkitInitialWorldAggregator extends InitialWorldAggregator {


        BukkitInitialWorldAggregator(@NotNull File worldsFolder) {
            super(worldsFolder);
        }

        @Override
        protected int getNumberOfLoadedWorlds() {
            return Bukkit.getWorlds().size();
        }

        protected void aggregateAlreadyLoadedWorlds() {
            for (final World world : Bukkit.getWorlds()) {
                addWorld(world.getName());
            }
        }
    }
}
