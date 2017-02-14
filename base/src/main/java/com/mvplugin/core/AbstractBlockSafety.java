package com.mvplugin.core;

import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.CoreLogger;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.location.BlockCoordinates;
import pluginbase.minecraft.location.Locations;

abstract class AbstractBlockSafety implements BlockSafety {

    @Override
    public boolean isSafeLocation(@NotNull final BlockCoordinates actual) {
        BlockCoordinates upOne = Locations.getBlockCoordinates(actual.getWorld(), actual.getWorldUUID(),
                actual.getBlockX(), actual.getBlockY() + 1, actual.getBlockZ());
        BlockCoordinates downOne = Locations.getBlockCoordinates(actual.getWorld(), actual.getWorldUUID(),
                actual.getBlockX(), actual.getBlockY() - 1, actual.getBlockZ());

        if (isSolidBlock(actual) || isSolidBlock(upOne)
                || !isBlockSafe(actual) || !isBlockSafe(upOne) || !isBlockSafe(downOne)) {
            return false;
        }

        if (isBlockAir(downOne)) {
            CoreLogger.finer("Air detected below %s", actual);
            final boolean waterBelow = hasTwoBlocksOfWaterBelow(downOne);
            CoreLogger.finer("Has 2 blocks of water below [%s]", waterBelow);
            return waterBelow;
        }
        return true;
    }

    protected abstract boolean isSolidBlock(@NotNull final BlockCoordinates location);

    protected abstract boolean isBlockSafe(@NotNull final BlockCoordinates location);

    protected abstract boolean isBlockAir(@NotNull final BlockCoordinates location);

    protected abstract boolean hasTwoBlocksOfWaterBelow(@NotNull final BlockCoordinates location);
}
