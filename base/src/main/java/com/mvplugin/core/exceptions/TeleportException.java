package com.mvplugin.core.exceptions;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import org.jetbrains.annotations.NotNull;

public class TeleportException extends MultiverseException {

    public TeleportException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public TeleportException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    public TeleportException(@NotNull final BundledMessage languageMessage, @NotNull final MultiverseException mvException) {
        super(languageMessage, mvException);
    }

    public TeleportException(@NotNull final MultiverseException e) {
        super(e);
    }
}
