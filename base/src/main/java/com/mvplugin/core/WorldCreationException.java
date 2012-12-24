package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import org.jetbrains.annotations.NotNull;

public class WorldCreationException extends MultiverseException {

    public WorldCreationException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public WorldCreationException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }
}
