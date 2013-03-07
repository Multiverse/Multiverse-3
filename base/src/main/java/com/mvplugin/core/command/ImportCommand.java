package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@CommandInfo(
        primaryAlias = "import",
        desc = "Import a world.",
        usage = "{NAME} {ENVIRONMENT}",
        prefixedAliases = "im",
        directlyPrefixedAliases = "import",
        flags = "g:n",
        min = 1,
        max = 2
)
public class ImportCommand extends MultiverseCommand {

    public static final Message IMPORT_HELP = new Message("command.import.help",
            "Imports a world into the server from a folder with the given name."
            + "\nThe folder must exist in the location where worlds are normally located and must contain Minecraft world data."
            + "\nYou must specify a world environment such as NORMAL or NETHER."
            + "\nYou may also specify a generator to use along with an optional generator ID."
            + "\nThe generator name is case sensitive!"
            + "\nFlags:"
            + "\n  -g {GENERATOR[:ID]} Specify a generator."
            + "\n  -n Do not adjust spawn"
            + "\nExamples:"
            + "\n  /mv import &6gargamel&a normal"
            + "\n  /mv import &6hell_world&a nether"
            + "\n  /mv import &6Cleanroom&a normal -g &3CleanroomGenerator");

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

    protected ImportCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_IMPORT;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return IMPORT_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);

        if (worldName.toLowerCase().equals("--list") || worldName.toLowerCase().equals("-l")) {
            final String worldList = this.getPotentialWorlds();
            if (worldList.length() > 2) {
                getMessager().message(sender, POTENTIAL_WORLD_LIST, worldList);
            } else {
                getMessager().message(sender, NO_POTENTIAL_WORLDS);
            }
            return true;
        }
        // Since we made an exception for the list, we have to make sure they have at least 2 params:
        // Note the exception is --list, which is covered above.
        if (context.argsLength() == 1) {
            return false;
        }

        // Make sure we don't already know about this world.
        if (getPlugin().getWorldManager().isManaged(worldName)) {
            getMessager().message(sender, Language.WORLD_ALREADY_EXISTS, worldName);
            return true;
        }

        File worldFile = new File(getPlugin().getServerInterface().getWorldContainer(), worldName);

        String generator = context.getFlag('g');
        boolean useSpawnAdjust = !context.hasFlag('n');

        String env = context.getString(1);
        WorldEnvironment environment = WorldEnvironment.getFromString(env);
        if (environment == null) {
            getMessager().message(sender, INVALID_ENVIRONMENT);
            // TODO EnvironmentCommand.showEnvironments(sender);
            return true;
        }

        if (worldFile.exists() && env != null) {
            getMessager().messageAndLog(sender, STARTING_IMPORT, worldName);
            try {
                getPlugin().getWorldManager().addWorld(worldName, environment, null, null, null, generator, useSpawnAdjust);
                getMessager().messageAndLog(sender, IMPORT_COMPLETE);
            } catch (WorldCreationException e) {
                getMessager().messageAndLog(sender, IMPORT_FAILED);
                e.sendException(getMessager(), sender);
            }
        } else if (env == null) {
            getMessager().message(sender, IMPORT_FAILED);
            getMessager().message(sender, NON_EXISTENT_ENVIRONMENT);
        } else {
            getMessager().message(sender, IMPORT_FAILED);
            String worldList = this.getPotentialWorlds();
            getMessager().message(sender, NON_EXISTENT_FOLDER);
            sender.sendMessage(worldList);
        }
        return true;
    }

    @NotNull
    private String getPotentialWorlds() {
        final StringBuilder worldList = new StringBuilder();
        ChatColor currColor = ChatColor.WHITE;
        for (final String potentialWorld : getPlugin().getWorldManager().getPotentialWorlds()) {
            if (!getPlugin().getWorldManager().isManaged(potentialWorld)) {
                worldList.append(currColor).append(potentialWorld).append(" ");
                if (currColor == ChatColor.WHITE) {
                    currColor = ChatColor.YELLOW;
                } else {
                    currColor = ChatColor.WHITE;
                }
            }
        }
        return worldList.toString();
    }
}
