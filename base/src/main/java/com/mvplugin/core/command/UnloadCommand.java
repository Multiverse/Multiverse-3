package com.mvplugin.core.command;

import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.plugin.PluginBase;

import static com.mvplugin.core.util.Language.Command.Unload.*;

@CommandInfo(
        primaryAlias = "unload",
        desc = "Unloads a world managed by Multiverse.",
        usage = "{NAME}",
        directlyPrefixedAliases = "unload",
        min = 1,
        max = 1
)
public class UnloadCommand extends MultiverseCommand {
    protected UnloadCommand(@NotNull final PluginBase<MultiverseCore> plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_UNLOAD;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);
        if (!getPlugin().getWorldManager().isLoaded(worldName)) {
            getMessager().message(sender, Language.WORLD_ALREADY_UNLOADED);
        }
        try {
            if (getPlugin().getWorldManager().unloadWorld(worldName)) {
                getMessager().message(sender, SUCCESS, worldName);
            } else {
                getMessager().message(sender, Language.WORLD_NOT_MANAGED, worldName);

            }
        } catch (final WorldManagementException e) {
            getMessager().message(sender, FAILURE, worldName);
            e.sendException(getMessager(), sender);
        }
        return true;
    }
}
