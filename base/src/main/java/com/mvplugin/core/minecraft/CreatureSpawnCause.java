package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.FauxEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents cause of an creature's spawning.
 */
@FauxEnum
public final class CreatureSpawnCause {

    private static final Map<String, CreatureSpawnCause> SPAWN_CAUSES = new HashMap<String, CreatureSpawnCause>(20);
    private static String naturalCauseKey = null;

    public static void registerSpawnCause(@NotNull String spawnCause) {
        if (naturalCauseKey == null) {
            throw new IllegalArgumentException("You must specify the natural cause first");
        }
        String uName = spawnCause.toUpperCase(Locale.ENGLISH);
        if (SPAWN_CAUSES.containsKey(uName)) {
            throw new IllegalArgumentException("'" + spawnCause + "' is an already registered spawn cause");
        }
        SPAWN_CAUSES.put(uName, new CreatureSpawnCause(spawnCause));
    }

    public static void specifyNaturalCause(@NotNull String spawnCause) {
        naturalCauseKey = spawnCause;
    }

    public static CreatureSpawnCause valueOf(String spawnCause) {
        return SPAWN_CAUSES.get(spawnCause.toUpperCase(Locale.ENGLISH));
    }

    public static CreatureSpawnCause[] values() {
        Collection<CreatureSpawnCause> values = SPAWN_CAUSES.values();
        return values.toArray(new CreatureSpawnCause[values.size()]);
    }

    public static CreatureSpawnCause getNaturalSpawnCause() {
        if (naturalCauseKey == null) {
            throw new IllegalStateException("No spawn causes have been registered");
        }
        return SPAWN_CAUSES.get(naturalCauseKey);
    }

    @NotNull
    private String name;

    private CreatureSpawnCause(@NotNull String name) {
        this.name = name.toUpperCase(Locale.ENGLISH);
    }

    /**
     * The name of the spawn cause.
     *
     * @return The name of the spawn cause.
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * The name of the spawn cause.
     *
     * @return The name of the spawn cause.
     */
    @Override
    @NotNull
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatureSpawnCause)) return false;

        final CreatureSpawnCause that = (CreatureSpawnCause) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
