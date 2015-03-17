package com.mvplugin.core;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Game;
import pluginbase.minecraft.location.BlockCoordinates;

class SpongeBlockSafety extends AbstractBlockSafety {

    private final Game game;

    SpongeBlockSafety(Game game) {
        this.game = game;
    }

    @Override
    protected boolean isSolidBlock(@NotNull BlockCoordinates location) {
        return false;
    }

    @Override
    protected boolean isBlockSafe(@NotNull BlockCoordinates location) {
        return false;
    }

    @Override
    protected boolean isBlockAir(@NotNull BlockCoordinates location) {
        return false;
    }

    @Override
    protected boolean hasTwoBlocksOfWaterBelow(@NotNull BlockCoordinates location) {
        return false;
    }
}
