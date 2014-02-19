package com.mvplugin.core.command;

import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.command.QueuedPluginCommand;

abstract class QueuedMultiverseCommand extends QueuedPluginCommand<MultiverseCore> {

    protected QueuedMultiverseCommand(@NotNull final PluginBase<MultiverseCore> plugin) {
        super(plugin);
    }
}
