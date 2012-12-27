package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.MultiverseWorld;
import com.mvplugin.core.api.WorldManager;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldManager implements WorldManager {

    @NotNull
    protected final MultiverseCore plugin;
    @NotNull
    protected final Map<String, MultiverseWorld> worldsMap;

    AbstractWorldManager(@NotNull final MultiverseCore plugin) {
        this.plugin = plugin;
        this.worldsMap = new HashMap<String, MultiverseWorld>();
    }

    @NotNull
    @Override
    public MultiverseWorld addWorld(@NotNull final String name,
                                    @Nullable final WorldEnvironment env,
                                    @Nullable final String seedString,
                                    @Nullable final WorldType type,
                                    @Nullable final Boolean generateStructures,
                                    @Nullable final String generator) throws WorldCreationException {
        return this.addWorld(name, env, seedString, type, generateStructures, generator, true);
    }

    @NotNull
    @Override
    public MultiverseWorld addWorld(@NotNull final String name,
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
    public MultiverseWorld addWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException {
        if (this.worldsMap.containsKey(settings.name())) {
            throw new WorldCreationException(new BundledMessage(Language.WORLD_ALREADY_EXISTS, settings.name()));
        }
        MultiverseWorld mvWorld = createWorld(settings);
        mvWorld.setAdjustSpawn(settings.adjustSpawn());
        this.worldsMap.put(settings.name(), mvWorld);
        return mvWorld;
    }

    @Override
    public boolean loadWorld(@NotNull final MultiverseWorld world) {
        if (isManaged(world.getName())) {
            return false;
        }
        this.worldsMap.put(world.getName(), world);
        return true;
    }

    @Override
    public boolean isManaged(@NotNull final String name) {
        return this.worldsMap.containsKey(name);
    }

    // TODO: Probably remove this in favor of isManaged
    @Override
    public boolean isMVWorld(@NotNull final String name) {
        return this.worldsMap.containsKey(name);
    }

    @NotNull
    @Override
    public Collection<MultiverseWorld> getMVWorlds() {
        return Collections.unmodifiableCollection(this.worldsMap.values());
    }


    /**
     * Creates a world with the given properties.
     *
     * If a Minecraft world is already loaded with this name, null will be returned.  If a Minecraft world already
     * exists but it not loaded, it will be loaded instead.
     *
     * @param settings
     * @return
     * @throws WorldCreationException
     */
    @NotNull
    protected abstract MultiverseWorld createWorld(@NotNull final WorldCreationSettings settings) throws WorldCreationException;
}
