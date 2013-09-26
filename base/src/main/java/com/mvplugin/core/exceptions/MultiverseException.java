package com.mvplugin.core.exceptions;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.SendablePluginBaseException;

public class MultiverseException extends SendablePluginBaseException {

    public MultiverseException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public MultiverseException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    public MultiverseException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause) {
        super(languageMessage, cause);
    }

    public MultiverseException(@NotNull final PluginBaseException e) {
        super(e);
    }
}
