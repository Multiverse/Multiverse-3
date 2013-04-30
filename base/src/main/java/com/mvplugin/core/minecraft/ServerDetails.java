package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A link between the base layer of Multiverse and the Server implementation that is used to obtain implementation
 * specific details required for the operation of Multiverse.
 */
public interface ServerDetails {

    /**
     * Obtains a collection of all the valid entity types present on the server.
     *
     * @return a collection of all the valid entity types present on the server.
     */
    @NotNull
    Collection<EntityType> getEntityTypes();

    /**
     * Obtains a collection of all the valid creature spawn causes present on the server.
     *
     * @return a collection of all the valid creature spawn causes present on the server.
     */
    @NotNull
    Collection<CreatureSpawnCause> getCreatureSpawnCauses();

    @Nullable
    EntityType getEntityTypeByName(@NotNull final String entityTypeName);

    @Nullable
    CreatureSpawnCause getCreatureSpawnCauseByName(@NotNull final String spawnCauseName);

    @NotNull
    CreatureSpawnCause getNaturalCreatureSpawnCause();
}
