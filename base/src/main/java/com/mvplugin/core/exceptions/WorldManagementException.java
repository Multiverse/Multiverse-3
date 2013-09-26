package com.mvplugin.core.exceptions;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;

public class WorldManagementException extends MultiverseException {

    public WorldManagementException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public WorldManagementException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    public WorldManagementException(@NotNull final BundledMessage languageMessage, @NotNull final MultiverseException mvException) {
        super(languageMessage, mvException);
    }

    public WorldManagementException(@NotNull final MultiverseException e) {
        super(e);
    }
}
