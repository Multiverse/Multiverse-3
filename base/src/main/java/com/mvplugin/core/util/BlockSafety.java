package com.mvplugin.core.util;

import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.location.BlockCoordinates;

public interface BlockSafety {

    boolean isSafeLocation(@NotNull final BlockCoordinates location);
}
