package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.mvplugin.core.WorldCreationException;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.MultiverseWorld;
import com.mvplugin.core.api.Perms;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.util.Language;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private static final String WORLD_FILE_NAME = "level.dat";

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
    public boolean runCommand(@NotNull MultiverseCore core, @NotNull BasePlayer sender, @NotNull CommandContext context) {
        final String worldName = context.getString(0);

        if (worldName.toLowerCase().equals("--list") || worldName.toLowerCase().equals("-l")) {
            final String worldList = this.getPotentialWorlds(core);
            if (worldList.length() > 2) {
                core.getMessager().message(sender, POTENTIAL_WORLD_LIST, worldList);
            } else {
                core.getMessager().message(sender, NO_POTENTIAL_WORLDS);
            }
            return true;
        }
        // Since we made an exception for the list, we have to make sure they have at least 2 params:
        // Note the exception is --list, which is covered above.
        if (context.argsLength() == 1) {
            return false;
        }

        // Make sure we don't already know about this world.
        if (core.getWorldManager().isManaged(worldName)) {
            core.getMessager().message(sender, Language.WORLD_ALREADY_EXISTS, worldName);
            return true;
        }

        File worldFile = new File(core.getServerInterface().getWorldContainer(), worldName);

        String generator = context.getFlag('g');
        boolean useSpawnAdjust = !context.hasFlag('n');

        String env = context.getString(1);
        WorldEnvironment environment = WorldEnvironment.getFromString(env);
        if (environment == null) {
            core.getMessager().message(sender, INVALID_ENVIRONMENT);
            // TODO EnvironmentCommand.showEnvironments(sender);
            return true;
        }

        if (worldFile.exists() && env != null) {
            core.getMessager().messageAndLog(sender, STARTING_IMPORT, worldName);
            try {
                core.getWorldManager().addWorld(worldName, environment, null, null, null, generator, useSpawnAdjust);
                core.getMessager().messageAndLog(sender, IMPORT_COMPLETE);
            } catch (WorldCreationException e) {
                core.getMessager().messageAndLog(sender, IMPORT_FAILED);
                core.getMessager().messageAndLog(sender, e.getBundledMessage().getMessage(), e.getBundledMessage().getArgs());
                if (e.getCause() != null) {
                    e.printStackTrace();
                }
            }
        } else if (env == null) {
            core.getMessager().message(sender, IMPORT_FAILED);
            core.getMessager().message(sender, NON_EXISTENT_ENVIRONMENT);
        } else {
            core.getMessager().message(sender, IMPORT_FAILED);
            String worldList = this.getPotentialWorlds(core);
            core.getMessager().message(sender, NON_EXISTENT_FOLDER);
            sender.sendMessage(worldList);
        }
        return true;
    }

    /**
     * A very basic check to see if a folder has a level.dat file.
     * If it does, we can safely assume it's a world folder.
     *
     * @param worldFolder The File that may be a world.
     * @return True if it looks like a world, false if not.
     */
    private static boolean checkIfIsWorld(@NotNull final File worldFolder) {
        if (worldFolder.isDirectory()) {
            File[] files = worldFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, @NotNull String name) {
                    return name.equalsIgnoreCase(WORLD_FILE_NAME);
                }
            });
            if (files != null && files.length > 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private String getPotentialWorlds(@NotNull final MultiverseCore core) {
        final File worldFolder = core.getServerInterface().getWorldContainer();
        if (worldFolder == null) {
            return "";
        }
        File[] files = worldFolder.listFiles();
        if (files == null) {
            files = new File[0];
        }
        StringBuilder worldList = new StringBuilder();
        Collection<MultiverseWorld> worlds = core.getWorldManager().getWorlds();
        List<String> worldStrings = new ArrayList<String>();
        for (MultiverseWorld world : worlds) {
            worldStrings.add(world.getName());
        }
        for (String world : core.getWorldManager().getUnloadedWorlds()) {
            worldStrings.add(world);
        }
        ChatColor currColor = ChatColor.WHITE;
        for (File file : files) {
            if (file.isDirectory() && checkIfIsWorld(file) && !worldStrings.contains(file.getName())) {
                worldList.append(currColor).append(file.getName()).append(" ");
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
