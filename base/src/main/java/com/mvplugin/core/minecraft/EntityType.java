package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.FauxEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents the type of an entity on the minecraft server.
 */
@FauxEnum
public final class EntityType {

    private static final Map<String, EntityType> ENTITY_TYPES = new HashMap<String, EntityType>(100);

    public static void registerEntityType(@NotNull String entityType) {
        String uName = entityType.toUpperCase(Locale.ENGLISH);
        if (ENTITY_TYPES.containsKey(uName)) {
            throw new IllegalArgumentException("'" + entityType + "' is an already registered entity type");
        }
        ENTITY_TYPES.put(uName, new EntityType(entityType));
    }

    public static EntityType valueOf(String entityType) {
        return ENTITY_TYPES.get(entityType.toUpperCase(Locale.ENGLISH));
    }

    public static EntityType[] values() {
        Collection<EntityType> values = ENTITY_TYPES.values();
        return values.toArray(new EntityType[values.size()]);
    }

    @NotNull
    private String name;

    private EntityType(@NotNull String name) {
        this.name = name.toUpperCase(Locale.ENGLISH);
    }

    /**
     * The name of this type of entity.
     *
     * @return The name of this type of entity.
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * The name of this type of entity.
     *
     * @return The name of this type of entity.
     */
    @Override
    @NotNull
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityType)) return false;

        final EntityType that = (EntityType) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
