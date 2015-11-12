package com.mvplugin.core;

import com.mvplugin.core.command.CreateCommand;
import com.mvplugin.core.command.DeleteCommand;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.command.InfoCommand;
import com.mvplugin.core.command.ListCommand;
import com.mvplugin.core.command.LoadCommand;
import com.mvplugin.core.command.ModifyAddCommand;
import com.mvplugin.core.command.ModifyClearCommand;
import com.mvplugin.core.command.ModifyRemoveCommand;
import com.mvplugin.core.command.ModifySetCommand;
import com.mvplugin.core.command.TeleportCommand;
import com.mvplugin.core.command.UnloadCommand;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.CoreLogger;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.PropertyDescriptions;
import pluginbase.plugin.PluginAgent;

/**
 * This class needs to be instantiated once for each plugin object created. It handles all the one time initialization
 * required for MultiverseCore such as registering commands and language classes.
 */
class MultiverseCoreInitializer {

    private static final String PERMISSION_PREFIX = "multiverse";

    MultiverseCoreInitializer(PluginAgent<MultiverseCore> pluginAgent) {
        CoreLogger.init(pluginAgent.getPluginBase());

        pluginAgent.setPermissionPrefix(PERMISSION_PREFIX);

        // Register language stuff
        pluginAgent.registerMessages(PropertyDescriptions.class);
        pluginAgent.registerMessages(Language.class);

        // Register commands
        pluginAgent.registerCommand(ImportCommand.class);
        pluginAgent.registerCommand(LoadCommand.class);
        pluginAgent.registerCommand(UnloadCommand.class);
        pluginAgent.registerCommand(ListCommand.class);
        pluginAgent.registerCommand(DeleteCommand.class);
        pluginAgent.registerCommand(CreateCommand.class);
        pluginAgent.registerCommand(TeleportCommand.class);
        pluginAgent.registerCommand(ModifySetCommand.class);
        pluginAgent.registerCommand(ModifyAddCommand.class);
        pluginAgent.registerCommand(ModifyRemoveCommand.class);
        pluginAgent.registerCommand(ModifyClearCommand.class);
        pluginAgent.registerCommand(InfoCommand.class);
    }
}
