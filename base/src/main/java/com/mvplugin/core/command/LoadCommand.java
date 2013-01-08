package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.mvplugin.core.WorldCreationException;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.Perms;
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

    public static final Message LOAD_HELP = new Message("command.import.help",
            "Loads a world that has previously been imported but is currently unloaded."
            + "\nExamples:"
            + "\n  /mv load &6gargamel&a");

    public static final Message POTENTIAL_WORLD_LIST = new Message("command.import.potential_world_list",
            "&b====[ These look like worlds ]====\n%s");

    public static final Message NO_POTENTIAL_WORLDS = new Message("command.import.no_potential_worlds",
            "&cNo potential worlds found. Sorry!");

    public static final Message INVALID_ENVIRONMENT = new Message("command.import.invalid_environment",
            "&cThat is not a valid environment.");

    public static final Message STARTING_IMPORT = new Message("command.import.starting_import",
            "Starting import of world '%s'...");

    public static final Message IMPORT_COMPLETE = new Message("command.import.import_complete",
            "&aImport complete!");

    public static final Message IMPORT_FAILED = new Message("command.import.import_failed",
            "&cImport failed!");

    public static final Message NON_EXISTENT_ENVIRONMENT = new Message("command.import.non_existent_environment",
            "&cThat world environment does not exist."
            + "\nFor a list of available world types, type: &b/mvenv");

    public static final Message NON_EXISTENT_FOLDER = new Message("command.import.non_existent_folder",
            "&cThat world folder does not exist. &bThese look like worlds to me:");

    private static final String WORLD_FILE_NAME = "level.dat";

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
    public boolean runCommand(@NotNull MultiverseCore core, @NotNull BasePlayer sender, @NotNull CommandContext context) {
        final String worldName = context.getString(0);

        try {
            core.getWorldManager().loadWorld(worldName);
        } catch (final WorldCreationException e) {
            core.getMessager().message(sender, e.getBundledMessage());
        }

        return true;
    }
}
