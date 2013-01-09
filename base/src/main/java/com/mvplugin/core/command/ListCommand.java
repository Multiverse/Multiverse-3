package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.util.Perms;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        primaryAlias = "list",
        desc = "Lists all worlds managed by multiverse.",
        directlyPrefixedAliases = {"list", "l"},
        min = 0,
        max = 0
)
public class ListCommand extends MultiverseCommand {

    public static final Message LIST_HELP = new Message("command.list.help",
            "Lists all worlds managed by multiverse."
            + "\nOnly the worlds you may access will be shown.");

    public static final Message LIST_WORLDS = new Message("command.list.list",
            ChatColor.LIGHT_PURPLE + "====[ Multiverse World List ]===="
            + "\n%s");

    protected ListCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_LIST;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return LIST_HELP;
    }

    @Override
    public boolean runCommand(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        final StringBuilder builder = new StringBuilder();
        for (@NotNull final MultiverseWorld world : getPlugin().getWorldManager().getWorlds()) {
            if (!Perms.ACCESS.hasPermission(sender, world.getName())
                    || (world.isHidden() && !Perms.CMD_MODIFY.hasPermission(sender))) {
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
                color = ChatColor.GREEN;
            } else if (env == WorldEnvironment.THE_END) {
                color = ChatColor.AQUA;
            }
            if (world.isHidden()) {
                builder.append(ChatColor.GRAY).append("[H]");
            }
            builder.append(ChatColor.WHITE);
            builder.append(world.getAlias()).append(ChatColor.WHITE);
            builder.append(" - ").append(color).append(world.getEnvironment());

        }
        for (@NotNull final String name : getPlugin().getWorldManager().getUnloadedWorlds()) {
            if (!getPlugin().getWorldManager().isLoaded(name)
                    && Perms.ACCESS.hasPermission(sender, name)
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
