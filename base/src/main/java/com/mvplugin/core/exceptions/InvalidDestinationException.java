package com.mvplugin.core.exceptions;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;

public class InvalidDestinationException extends MultiverseException {

    public InvalidDestinationException(@NotNull String message) {
        super(Message.bundleMessage(Message.createStaticMessage(message)));
    }

    public InvalidDestinationException(@NotNull String message, @NotNull Throwable throwable) {
        super(Message.bundleMessage(Message.createStaticMessage(message)), throwable);
    }

    public InvalidDestinationException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public InvalidDestinationException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    public InvalidDestinationException(@NotNull final BundledMessage languageMessage, @NotNull final MultiverseException mvException) {
        super(languageMessage, mvException);
    }

    public InvalidDestinationException(@NotNull final MultiverseException e) {
        super(e);
    }
}
