package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.BlockCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.mvplugin.core.util.BlockSafety;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBlockSafety implements BlockSafety {

    @Override
    public boolean isSafeLocation(@NotNull final BlockCoordinates actual) {
        BlockCoordinates upOne = Locations.copyOf(actual);
        BlockCoordinates downOne = Locations.copyOf(actual);
        upOne.add(0, 1, 0);
        downOne.subtract(0, 1, 0);

        if (isSolidBlock(actual) || isSolidBlock(upOne)
                || !isBlockSafe(actual) || !isBlockSafe(upOne) || !isBlockSafe(downOne)) {
            return false;
        }

        if (isBlockAir(downOne)) {
            Logging.finer("Air detected below %s", actual);
            final boolean waterBelow = hasTwoBlocksOfWaterBelow(downOne);
            Logging.finer("Has 2 blocks of water below [%s]", waterBelow);
            return waterBelow;
        }
        return true;
    }

    protected abstract boolean isSolidBlock(@NotNull final BlockCoordinates location);

    protected abstract boolean isBlockSafe(@NotNull final BlockCoordinates location);

    protected abstract boolean isBlockAir(@NotNull final BlockCoordinates location);

    protected abstract boolean hasTwoBlocksOfWaterBelow(@NotNull final BlockCoordinates location);
}
