package com.mvplugin.core.command;

import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.QueuedCommand;

abstract class QueuedMultiverseCommand extends QueuedCommand<MultiverseCore> {

    protected QueuedMultiverseCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }
}
