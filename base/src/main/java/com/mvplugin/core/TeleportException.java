package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import org.jetbrains.annotations.NotNull;

public class TeleportException extends MultiverseException {

    public TeleportException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }
}
