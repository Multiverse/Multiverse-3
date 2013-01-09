package com.mvplugin.core.exceptions;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import org.jetbrains.annotations.NotNull;

public class TeleportException extends MultiverseException {

    public TeleportException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }
}
