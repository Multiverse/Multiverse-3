package com.mvplugin.core.command;

import com.mvplugin.core.destination.Destination;
import com.mvplugin.core.destination.UnknownDestination;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.permission.Perm;

import static com.mvplugin.core.util.Language.Command.Teleport.*;

@CommandInfo(
        primaryAlias = "teleport",
        prefixedAliases = { "tp" },
        directlyPrefixedAliases = { "tp" },
        desc = "Teleports a player (or the user if no player is given) to a specified destination.",
        usage = "[player] <destination>",
        min = 1,
        max = 2
)
public class TeleportCommand extends MultiverseCommand {
    protected TeleportCommand(@NotNull final CommandProvider<MultiverseCore> plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_LIST;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        BasePlayer teleportee;
        String strDestination;
        if (context.argsLength() == 1) {
            if (!sender.isPlayer()) {
                getMessager().message(sender, NEED_PLAYER);
                return true;
            }
            teleportee = sender;
            strDestination = context.getString(0);
        } else if (context.argsLength() == 2) {
            teleportee = getPlugin().getServerInterface().getPlayer(context.getString(0));
            if (teleportee == null) {
                getMessager().message(sender, NO_SUCH_PLAYER, context.getString(0));
                return true;
            }
            strDestination = context.getString(1);
        } else throw new IllegalStateException();

        Destination destination = this.getPlugin().getDestinationRegistry().parseDestination(strDestination);
        if (destination instanceof UnknownDestination) {
            getMessager().message(sender, NO_SUCH_DESTINATION, strDestination);
            return true;
        }

        try {
            destination.teleport(sender, teleportee, (Entity) teleportee);
        } catch (TeleportException e) {
            getMessager().message(sender, e.getBundledMessage());
        }
        return true;
    }
}
