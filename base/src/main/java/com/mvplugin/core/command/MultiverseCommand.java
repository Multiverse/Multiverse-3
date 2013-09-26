package com.mvplugin.core.command;

import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;

abstract class MultiverseCommand extends Command<MultiverseCore> {

    static {
        // Statically initialize help language
        CommandHelp.init();
    }

    protected MultiverseCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }
}
