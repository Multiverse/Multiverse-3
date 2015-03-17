package com.mvplugin.core;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Game;

class SpongeConvert {

    private static Game GAME;

    static void initializeWithGame(@NotNull Game game) {
        GAME = game;
    }
}
