package com.mvplugin.core.util;

import pluginbase.messages.Theme;

public enum MultiverseTheme {
    WORLD_NORMAL('0'),
    WORLD_NETHER('1'),
    WORLD_THE_END('2'),
    UNKNOWN_WORLD('9'),
    HIDDEN_WORLD('H'),
    IMPL_NAME('T')
    ;

    private final char themeTag;

    private MultiverseTheme(char themeTag) {
        this.themeTag = themeTag;
    }

    @Override
    public String toString() {
        return Theme.getColorByTag(themeTag);
    }
}
