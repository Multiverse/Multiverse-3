package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.plugin.command.Command;
import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;

abstract class MultiverseCommand extends Command<MultiverseCore> {

    static {
        // Statically initialize help language
        CommandHelp.init();
    }

    protected MultiverseCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }
}
