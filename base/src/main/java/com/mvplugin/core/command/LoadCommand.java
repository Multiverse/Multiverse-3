package com.mvplugin.core.command;

import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

import static com.mvplugin.core.util.Language.Command.Load.*;

@CommandInfo(
        primaryAlias = "load",
        desc = "Loads a Multiverse World that has been unloaded.",
        usage = "{NAME}",
        directlyPrefixedAliases = "load",
        min = 1,
        max = 1
)
public class LoadCommand extends MultiverseCommand {
    protected LoadCommand(@NotNull final CommandProvider<MultiverseCore> plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_LOAD;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);

        try {
            getPlugin().getWorldManager().loadWorld(worldName);
            getMessager().message(sender, SUCCESS, worldName);
        } catch (final WorldManagementException e) {
            e.sendException(getMessager(), sender);
        }

        return true;
    }
}
