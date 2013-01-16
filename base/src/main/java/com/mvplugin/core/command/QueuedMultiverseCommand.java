package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.QueuedCommand;
import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;

abstract class QueuedMultiverseCommand extends QueuedCommand<MultiverseCore> {

    protected QueuedMultiverseCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }
}
