package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.BlockCoordinates;
import org.jetbrains.annotations.NotNull;

public interface BlockSafety {

    boolean isSafeLocation(@NotNull final BlockCoordinates location);
}
