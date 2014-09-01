package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.WorldManager;
import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;

abstract class MultiverseCommand extends Command<MultiverseCore> {

    static {
        // Statically initialize help language
        CommandHelp.init();
    }

    protected MultiverseCommand(@NotNull final CommandProvider<MultiverseCore> plugin) {
        super(plugin);
    }

    protected WorldManager getWorldManager() {
        return getPlugin().getWorldManager();
    }

    @NotNull
    protected MultiverseWorld getWorldFromContext(@NotNull BasePlayer sender, @NotNull CommandContext context, int expectedWorldArgIndex) throws MultiverseException {
        MultiverseWorld world;
        WorldManager worldManager = getPlugin().getWorldManager();
        if (context.argsLength() > expectedWorldArgIndex) {
            world = worldManager.getWorld(context.getString(expectedWorldArgIndex));
            if (world == null) {
                throw new MultiverseException(Message.bundleMessage(Language.WORLD_NOT_MANAGED));
            }
        } else if (sender instanceof Entity) {
            world = getEntityWorld((Entity) sender);
        } else {
            throw new MultiverseException(Message.bundleMessage(Language.Command.MUST_SPECIFY_WORLD));
        }
        return world;
    }

    private MultiverseWorld getEntityWorld(Entity entity) {
        EntityCoordinates location = entity.getLocation();
        return getPlugin().getWorldManager().getWorld(location.getWorld());
    }
}
