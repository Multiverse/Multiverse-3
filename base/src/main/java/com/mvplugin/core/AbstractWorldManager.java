package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.MultiverseWorld;
import com.mvplugin.core.api.WorldManager;
import com.mvplugin.core.api.WorldProperties;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
    public boolean isManaged(@NotNull final String name) {
        return this.worldsMap.containsKey(name);
    }

    @Nullable
    @Override
    public MultiverseWorld getWorld(@NotNull final String name) {
        final MultiverseWorld world = this.worldsMap.get(name);
        if (world != null) {
            return world;
        }
        for (final MultiverseWorld w : this.worldsMap.values()) {
            if (w.getAlias().equals(name)) {
                return w;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Collection<MultiverseWorld> getWorlds() {
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

    /**
     * Gets an existing WorldProperties object or creates a new one based on the name.
     *
     * @param worldName The name of the world to get properties for.
     * @return The world properties for the given world name.
     * @throws java.io.IOException In case there are any issues accessing the persistence for the world properties.
     */
    @NotNull
    protected abstract WorldProperties getWorldProperties(@NotNull final String worldName) throws IOException;
}
