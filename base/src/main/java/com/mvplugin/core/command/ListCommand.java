package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.ChatColor;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.plugin.PluginBase;

import static com.mvplugin.core.util.Language.Command.List.*;

@CommandInfo(
        primaryAlias = "list",
        desc = "Lists all worlds managed by multiverse.",
        directlyPrefixedAliases = {"list", "l"},
        min = 0,
        max = 0
)
public class ListCommand extends MultiverseCommand {
    protected ListCommand(@NotNull final PluginBase<MultiverseCore> plugin) {
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
    public boolean runCommand(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        final StringBuilder builder = new StringBuilder();
        for (@NotNull final MultiverseWorld world : getPlugin().getWorldManager().getWorlds()) {
            if (!Perms.ACCESS.hasPermission(sender, world.getName())
                    || (world.isHidden() && !Perms.CMD_MODIFY.hasPermission(sender, world.getName()))) {
                continue;
            }
            if (builder.length() != 0) {
                builder.append("\n");
            }
            ChatColor color = ChatColor.GOLD;
            WorldEnvironment env = world.getEnvironment();
            if (env == WorldEnvironment.NETHER) {
                color = ChatColor.RED;
            } else if (env == WorldEnvironment.NORMAL) {
                color = ChatColor.DARK_GREEN;
            } else if (env == WorldEnvironment.THE_END) {
                color = ChatColor.BLACK;
            }
            if (world.isHidden()) {
                builder.append(ChatColor.GRAY).append("[H]");
            }
            builder.append(ChatColor.WHITE);
            builder.append(world.getAlias()).append(ChatColor.WHITE);
            builder.append(" - ").append(color).append(world.getEnvironment());

        }
        for (@NotNull final String name : getPlugin().getWorldManager().getUnloadedWorlds()) {
            if (Perms.ACCESS.hasPermission(sender, name)
                    && Perms.CMD_LOAD.hasPermission(sender)) {
                if (builder.length() != 0) {
                    builder.append("\n");
                }
                builder.append(ChatColor.GRAY).append(name).append(" - UNLOADED");
            }
        }
        getMessager().message(sender, LIST_WORLDS, builder.toString());
        return true;
    }
}
