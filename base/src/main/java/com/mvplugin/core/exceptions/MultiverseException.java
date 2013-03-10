package com.mvplugin.core.exceptions;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.SendablePluginBaseException;
import org.jetbrains.annotations.NotNull;

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
