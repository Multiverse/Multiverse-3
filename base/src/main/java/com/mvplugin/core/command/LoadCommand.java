package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        primaryAlias = "load",
        desc = "Loads a Multiverse World that has been unloaded.",
        usage = "{NAME}",
        directlyPrefixedAliases = "load",
        min = 1,
        max = 1
)
public class LoadCommand extends MultiverseCommand {

    public static final Message LOAD_HELP = new Message("command.load.help",
            "Loads a world that has previously been imported but is currently unloaded."
            + "\nExamples:"
            + "\n  /mv load &6gargamel&a");

    public static final Message LOAD_SUCCESS = new Message("command.load.success",
            "Successfully loaded the world '&b%s&f'!");

    protected LoadCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_LOAD;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return LOAD_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);

        try {
            getPlugin().getWorldManager().loadWorld(worldName);
            getMessager().message(sender, LOAD_SUCCESS, worldName);
        } catch (final WorldCreationException e) {
            getMessager().message(sender, e.getBundledMessage());
        }

        return true;
    }
}
