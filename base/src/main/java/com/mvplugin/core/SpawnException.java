package com.mvplugin.core;

import com.mvplugin.core.minecraft.CreatureSpawnCause;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.minecraft.ServerDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an exception to the entity spawning rules of the server.
 * <p/>
 * This is either to used to only allow a certain type of spawning or when allowing everything to disallow a certain type.
 * <p/>
 * This contains information pertaining to single creature type, namely, the ways that it should be allowed to spawn or
 * ways that are disallowed.
 */
public final class SpawnException {

    static {

    }

    private static final String DELIMITER = ",";

    private Set<CreatureSpawnCause> spawnCauses;
    private EntityType entityType;
    private String spawnExceptionString;

    private SpawnException(@NotNull final String spawnExceptionString, @NotNull final EntityType entityType, @NotNull final Set<CreatureSpawnCause> spawnCauses) {
        this.spawnExceptionString = spawnExceptionString;
        this.entityType = entityType;
        this.spawnCauses = Collections.unmodifiableSet(spawnCauses);
    }

    private SpawnException(@NotNull final EntityType entityType, @NotNull final Set<CreatureSpawnCause> spawnCauses) {
        this.entityType = entityType;
        this.spawnCauses = Collections.unmodifiableSet(spawnCauses);
        final StringBuilder builder = new StringBuilder(entityType.name());
        for (final CreatureSpawnCause spawnReason : this.spawnCauses) {
            builder.append(DELIMITER).append(spawnReason);
        }
        spawnExceptionString = builder.toString();
    }

    /**
     * Creates a new SpawnException from the given spawn exception string.
     * <p/>
     * If no spawn reasons are indicated in the string, the natural spawning of monsters is used.
     * <p/>
     * The spawn exception string is a comma separated value with the entity type as the first entry followed by
     * spawn reasons.
     *
     * @param serverDetails The server details connection in order to correctly parse entity information.
     * @param spawnExceptionString The string that contains SpawnException data.
     * @return a new SpawnException for the given entity type and spawn reasons.
     */
    @Nullable
    public static SpawnException valueOf(@NotNull final ServerDetails serverDetails, @NotNull final String spawnExceptionString) {
        final String[] split = spawnExceptionString.split(DELIMITER);
        final EntityType entityType = serverDetails.getEntityTypeByName(split[0]);
        final Set<CreatureSpawnCause> spawnReasons;
        if (split.length > 1) {
            spawnReasons = new HashSet<CreatureSpawnCause>(split.length - 1);
            for (int i = 1; i < split.length; i++) {
                final CreatureSpawnCause cause = serverDetails.getCreatureSpawnCauseByName(split[i]);
                if (cause != null) {
                    spawnReasons.add(cause);
                }
            }
            if (spawnReasons.isEmpty()) {

            }
        } else {
            spawnReasons = new HashSet<CreatureSpawnCause>(1);
            spawnReasons.add(serverDetails.getNaturalCreatureSpawnCause());
        }
        return new SpawnException(spawnExceptionString, entityType, spawnReasons);
    }

    /**
     * Creates a new SpawnException for the given entity type and spawn reasons.
     * <p/>
     * If no spawn reasons are given, the natural spawning of monsters is used.
     *
     * @param serverDetails The server details connection in order to correctly parse entity information.
     * @param entityType The name of the entity type to use.  This is implementation specific.
     * @param spawnReasons The names of the spawn reasons to use.  This is implementation specific.
     * @return a new SpawnException for the given entity type and spawn reasons.
     */
    @NotNull
    public static SpawnException createSpawnException(@NotNull final ServerDetails serverDetails, @NotNull final EntityType entityType, @NotNull final CreatureSpawnCause... spawnReasons) {
        // I want to validate the entity type and spawn reasons against what are in the minecraft server implementation.
        final Set<CreatureSpawnCause> spawnReasonsSet;
        if (spawnReasons.length == 0) {
            spawnReasonsSet = new HashSet<CreatureSpawnCause>(1);
            spawnReasonsSet.add(serverDetails.getNaturalCreatureSpawnCause());
        } else {
            spawnReasonsSet = new HashSet<CreatureSpawnCause>(spawnReasons.length);
            Collections.addAll(spawnReasonsSet, spawnReasons);
        }
        return new SpawnException(entityType, spawnReasonsSet);
    }

    /**
     * The name of the entity type this SpawnException represents.
     *
     * @return The name of the entity type this SpawnException represents.
     */
    @NotNull
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * The spawn reasons this SpawnException represents.
     * <p/>
     * This collection is unmodifiable.
     *
     * @return The spawn reasons this SpawnException represents.
     */
    @NotNull
    public Collection<CreatureSpawnCause> getSpawnCauses() {
        return spawnCauses;
    }

    /**
     * Gets the SpawnException's string form.
     * <p/>
     * The spawn exception string is a comma separated value with the entity type as the first entry followed by
     * spawn reasons.
     *
     * @return the SpawnException's string form.
     */
    @NotNull
    @Override
    public String toString() {
        return spawnExceptionString;
    }
}
