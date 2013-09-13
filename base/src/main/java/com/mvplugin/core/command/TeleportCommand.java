package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.destination.Destination;
import com.mvplugin.core.destination.UnknownDestination;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;

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
    protected TeleportCommand(@NotNull final MultiverseCore plugin) {
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
