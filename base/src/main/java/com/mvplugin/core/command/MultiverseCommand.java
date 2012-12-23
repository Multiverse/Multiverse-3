package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.plugin.command.Command;
import com.mvplugin.core.api.CorePlugin;

abstract class MultiverseCommand extends Command<CorePlugin> {

    static {
        // Statically initialize help language
        CommandHelp.init();
    }
}
