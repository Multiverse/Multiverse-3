package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.Theme;
import org.jetbrains.annotations.NotNull;

public enum MVTheme {
    WORLD_NORMAL('0'),
    WORLD_NETHER('1'),
    WORLD_THE_END('2'),
    ;

    private final String theme;

    private MVTheme(final char themeTag) {
        this.theme = new String(new char[]{Theme.THEME_MARKER, themeTag});
    }

    @Override
    @NotNull
    public String toString() {
        return theme;
    }
}
