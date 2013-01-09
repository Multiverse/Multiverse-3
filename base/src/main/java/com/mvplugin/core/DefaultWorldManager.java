package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.SafeTeleporter;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldCreationSettings;
import com.mvplugin.core.world.WorldManager;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultWorldManager<W extends MultiverseWorld> implements WorldManager<W> {

    @NotNull
    private final MultiverseCoreAPI api;
    @NotNull
    private final Map<String, W> worldsMap;
    @NotNull
    private final WorldUtil<W> worldUtil;

    DefaultWorldManager(@NotNull final MultiverseCoreAPI api, @NotNull final WorldUtil<W> worldUtil) {
        this.api = api;
        this.worldUtil = worldUtil;
        this.worldsMap = new HashMap<String, W>();
        this.worldsMap.putAll(worldUtil.getInitialWorlds());
    }

    @NotNull
    @Override
    public W addWorld(@NotNull final String name,
                                    @Nullable final WorldEnvironment env,
                                    @Nullable final String seedString,
                                    @Nullable final WorldType type,
                                    @Nullable final Boolean generateStructures,
                                    @Nullable final String generator) throws WorldCreationException {
        return this.addWorld(name, env, seedString, type, generateStructures, generator, true);
    }

    @NotNull
    @Override
    public W addWorld(@NotNull final String name,
                                    @Nullable final WorldEnvironment env,
                                    @Nullable final String seedString,
                                    @Nullable final WorldType type,
                                    @Nullable final Boolean generateStructures,
                                    @Nullable final String generator,
                                    boolean useSpawnAdjust) throws WorldCreationException {
        final WorldCreationSettings settings = new WorldCreationSettings(name);
        if (seedString != null && !seedString.isEmpty()) {
            try {
                settings.seed(Long.parseLong(seedString));
            } catch (NumberFormatException numberformatexception) {
                settings.seed((long) seedString.hashCode());
            }
        }

        settings.type(type);
        settings.generateStructures(generateStructures);

        // TODO: Use the fancy kind with the commandSender | dumptruckman has no idea what this means..
        if (generator != null && !generator.isEmpty()) {
            settings.generator(generator);
        }

        settings.adjustSpawn(useSpawnAdjust);

        return addWorld(settings);
    }

    @NotNull
    @Override
    public W addWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (this.worldsMap.containsKey(settings.name())) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_ALREADY_EXISTS, settings.name()));
        }
        W mvWorld = this.worldUtil.createWorld(settings);
        mvWorld.setAdjustSpawn(settings.adjustSpawn());
        this.worldsMap.put(settings.name(), mvWorld);
        return mvWorld;
    }

    @Override
    public boolean isLoaded(@NotNull final String name) {
        return this.worldsMap.containsKey(name);
    }

    @Override
    public boolean isManaged(@NotNull final String name) {
        return this.worldsMap.containsKey(name) || this.getUnloadedWorlds().contains(name);
    }

    @Nullable
    @Override
    public W getWorld(@NotNull final String name) {
        final W world = this.worldsMap.get(name);
        if (world != null) {
            return world;
        }
        for (final W w : this.worldsMap.values()) {
            if (w.getAlias().equals(name)) {
                return w;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Collection<W> getWorlds() {
        return Collections.unmodifiableCollection(this.worldsMap.values());
    }

    @Override
    public void loadWorld(@NotNull final String name) throws WorldCreationException {
        if (isLoaded(name)) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_ALREADY_LOADED, name));
        }
        if (!isManaged(name)) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_NOT_MANAGED, name));
        }
        try {
            // Transfer all the properties of the world ot a WorldCreationSettings object.
            final WorldProperties properties = this.worldUtil.getWorldProperties(name);
            final WorldCreationSettings settings = new WorldCreationSettings(name);
            //settings.type(properties.get(WorldProperties.TYPE));
            settings.generator(properties.get(WorldProperties.GENERATOR));
            settings.seed(properties.get(WorldProperties.SEED));
            settings.env(properties.get(WorldProperties.ENVIRONMENT));
            settings.adjustSpawn(properties.get(WorldProperties.ADJUST_SPAWN));
            //settings.generateStructures(properties.get(WorldProperties.GENERATE_STRUCTURES));

            addWorld(settings);
        } catch (final IOException e) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_LOAD_ERROR, name), e);
        }
    }

    @Override
    public boolean unloadWorld(@NotNull final String name) {
        final W world = getWorld(name);
        if (world != null) {
            return unloadWorld(world);
        }
        // TODO finish... needs to be implemented by bukkit side somehow
        /*
        else if (this.plugin.getServer().getWorld(name) != null) {
            Logging.warning("Hmm Multiverse does not know about this world but it's loaded in memory.");
            Logging.warning("To let Multiverse know about it, use:");
            Logging.warning("/mv import %s %s", name, this.plugin.getServer().getWorld(name).getEnvironment().toString());
        } else if (this.worldsFromTheConfig.containsKey(name)) {
            return true; // it's already unloaded
        } else {
            Logging.info("Multiverse does not know about '%s' and it's not loaded by Bukkit.", name);
        }
        */
        return false;
    }

    @Override
    public boolean unloadWorld(@NotNull final MultiverseWorld world) {
        try {
            removePlayersFromWorld(world);
        } catch (final TeleportException e) {
            e.printStackTrace();
            return false;
        }
        if (this.worldUtil.unloadWorldFromServer(world)) {
            this.worldsMap.remove(world.getName());
            Logging.info("World '%s' was unloaded from memory.", world.getName());
            return true;
        } else {
            Logging.warning("World '%s' could not be unloaded. Is it a default world?", world.getName());
            return false;
        }
    }

    @Override
    public void removePlayersFromWorld(@NotNull final MultiverseWorld world) throws TeleportException {
        final W safeWorld = getSafeWorld();
        final SafeTeleporter teleporter = this.api.getSafeTeleporter();
        final FacingCoordinates sLoc = safeWorld.getSpawnLocation();
        final EntityCoordinates location = Locations.getEntityCoordinates(safeWorld.getName(),
                sLoc.getX(), sLoc.getY(), sLoc.getZ(), sLoc.getPitch(), sLoc.getYaw());
        for (final BasePlayer p : world.getPlayers()) {
            if (p instanceof Entity) {
                teleporter.safelyTeleport(null, (Entity) p, location);
            }
        }
    }

    @NotNull
    @Override
    public List<String> getUnloadedWorlds() {
        return this.worldUtil.getUnloadedWorlds();
    }

    private W getSafeWorld() {
        return getWorld(this.worldUtil.getSafeWorldName());
    }
}
