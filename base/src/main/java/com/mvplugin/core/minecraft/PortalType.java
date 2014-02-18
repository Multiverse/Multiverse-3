package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.FauxEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents the type of a portal on the minecraft server.
 */
@FauxEnum
public final class PortalType {

    private static final Map<String, PortalType> PORTAL_TYPES = new HashMap<String, PortalType>(100);

    public static void registerPortalType(@NotNull String portalType) {
        String uName = portalType.toUpperCase(Locale.ENGLISH);
        if (PORTAL_TYPES.containsKey(uName)) {
            throw new IllegalArgumentException("'" + portalType + "' is an already registered portal type");
        }
        PORTAL_TYPES.put(uName, new PortalType(portalType));
    }

    public static PortalType valueOf(String portalType) {
        return PORTAL_TYPES.get(portalType.toUpperCase(Locale.ENGLISH));
    }

    public static PortalType[] values() {
        Collection<PortalType> values = PORTAL_TYPES.values();
        return values.toArray(new PortalType[values.size()]);
    }

    @NotNull
    private String name;

    private PortalType(@NotNull String name) {
        this.name = name.toUpperCase(Locale.ENGLISH);
    }

    /**
     * The name of this type of portal.
     *
     * @return The name of this type of portal.
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * The name of this type of portal.
     *
     * @return The name of this type of portal.
     */
    @Override
    @NotNull
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortalType)) return false;

        final PortalType that = (PortalType) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
