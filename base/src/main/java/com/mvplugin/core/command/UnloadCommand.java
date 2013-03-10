package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        primaryAlias = "unload",
        desc = "Unloads a world managed by Multiverse.",
        usage = "{NAME}",
        directlyPrefixedAliases = "unload",
        min = 1,
        max = 1
)
public class UnloadCommand extends MultiverseCommand {

    public static final Message UNLOAD_HELP = Message.createMessage("command.unload.help",
            "Unloads a world that has previously been imported and is currently loaded."
            + "\nThis will remove the world from memory but not delete anything."
            + "\nExamples:"
            + "\n  /mv unload &6gargamel&a");

    public static final Message UNLOAD_SUCCESS = Message.createMessage("command.unload.success",
            "'&b%s&f' has been unloaded successfully!");

    public static final Message UNLOAD_FAILURE = Message.createMessage("command.unload.failure",
            "'&b%s&f' could not be unloaded!");

    protected UnloadCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_UNLOAD;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return UNLOAD_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);
        if (!getPlugin().getWorldManager().isLoaded(worldName)) {
            getMessager().message(sender, Language.WORLD_ALREADY_UNLOADED);
        }
        try {
            if (getPlugin().getWorldManager().unloadWorld(worldName)) {
                getMessager().message(sender, UNLOAD_SUCCESS, worldName);
            } else {
                getMessager().message(sender, Language.WORLD_NOT_MANAGED, worldName);

            }
        } catch (final WorldManagementException e) {
            getMessager().message(sender, UNLOAD_FAILURE, worldName);
            e.sendException(getMessager(), sender);
        }
        return true;
    }
}
