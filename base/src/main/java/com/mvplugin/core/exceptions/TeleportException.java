package com.mvplugin.core.exceptions;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;

public class TeleportException extends MultiverseException {

    public TeleportException(@NotNull String message) {
        super(Message.bundleMessage(Message.createStaticMessage(message)));
    }

    public TeleportException(@NotNull String message, @NotNull Throwable throwable) {
        super(Message.bundleMessage(Message.createStaticMessage(message)), throwable);
    }

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
