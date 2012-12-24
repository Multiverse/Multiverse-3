package com.mvplugin.core.minecraft;

import org.jetbrains.annotations.Nullable;

public enum WorldEnvironment {
    NETHER,
    NORMAL,
    THE_END
    ;

    @Nullable
    public static WorldEnvironment getFromString(@Nullable final String name) {
        for (final WorldEnvironment env : WorldEnvironment.values()) {
            if (env.toString().equalsIgnoreCase(name)) {
                return env;
            }
        }
        return null;
    }
}
