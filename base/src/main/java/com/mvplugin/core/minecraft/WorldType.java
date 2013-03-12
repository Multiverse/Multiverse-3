package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.Nullable;

public enum WorldType {
    FLAT,
    LARGE_BIOMES,
    NORMAL,
    VERSION_1_1;

    @Nullable
    public static WorldType getFromString(@Nullable final String name) {
        for (final WorldType env : WorldType.values()) {
            if (env.toString().equalsIgnoreCase(name)) {
                return env;
            }
        }
        return null;
    }
}
