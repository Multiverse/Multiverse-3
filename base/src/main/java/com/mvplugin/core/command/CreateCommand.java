package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.Theme;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.MVTheme;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        primaryAlias = "create",
        desc = "Creates a new world.",
        usage = "{NAME} {ENVIRONMENT}",
        directlyPrefixedAliases = {"c", "create"},
        flags = "s:g:t:a",
        min = 2,
        max = 2
)
public class CreateCommand extends MultiverseCommand {

    public static final Message CREATE_HELP = Message.createMessage("command.create.help",
            Theme.HELP + "Creates a new world on your server with the given name."
            + "\n" + Theme.HELP + "You must specify a world environment such as " + MVTheme.WORLD_NORMAL + "NORMAL " + Theme.HELP + "or " + MVTheme.WORLD_NORMAL + "NETHER" + Theme.HELP + "."
            + "\n" + Theme.HELP + "You may specify a world seed."
            + "\n" + Theme.HELP + "You may also specify a generator to use along with an optional generator ID."
            + "\n" + Theme.HELP + "The generator name is case sensitive!"
            + "\n" + Theme.HELP + "You may specify a world type such as FLAT or LARGE_BIOMES"
            + "\n" + Theme.HELP + "You may specify if Multiverse should declare to generate structures or not."
            + "\n" + Theme.HELP + "Flags:"
            + "\n" + Theme.CMD_FLAG + "  -s " + Theme.REQ_ARG + "{SEED}" + Theme.HELP + " Specify a world seed to use."
            + "\n" + Theme.CMD_FLAG + "  -g " + Theme.REQ_ARG + "{GENERATOR" + Theme.OPT_ARG + "[:ID]" + Theme.REQ_ARG + "}" + Theme.HELP + " Specify a generator."
            + "\n" + Theme.CMD_FLAG + "  -t " + Theme.REQ_ARG + "{TYPE}" + Theme.HELP + " Specify a world type."
            + "\n" + Theme.CMD_FLAG + "  -a " + Theme.REQ_ARG + "{true|false}" + Theme.HELP + " Specify whether or not to generate structures."
            + "\n" + Theme.HELP + "Examples:"
            + "\n  " + Theme.CMD_USAGE + "/mv create " + Theme.REQ_ARG + "gargamel " + MVTheme.WORLD_NORMAL + "normal"
            + "\n  " + Theme.CMD_USAGE + "/mv create " + Theme.REQ_ARG + "\"hell world\" " + MVTheme.WORLD_NETHER + "nether"
            + "\n  " + Theme.CMD_USAGE + "/mv create " + Theme.REQ_ARG + "space " + MVTheme.WORLD_NORMAL + "normal " + Theme.CMD_FLAG + "-g " + Theme.REQ_ARG + "CleanroomGenerator" + Theme.OPT_ARG + ":.");

    public static final Message CREATE_FAILED = Message.createMessage("command.create.failed",
            Theme.FAILURE + "Create failed!");

    public static final Message CREATING_WORLD = Message.createMessage("command.create.creating",
            Theme.PLEASE_WAIT + "Creating new world, please wait...");

    public static final Message CREATE_SUCCESS = Message.createMessage("command.create.success",
            Theme.SUCCESS + "Successfully created world " + Theme.IMPORTANT + "%s" + Theme.SUCCESS + "!");

    protected CreateCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_CREATE;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return CREATE_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        final String worldName = context.getString(0);

        // Make sure we don't already know about this world.
        if (getPlugin().getWorldManager().isManaged(worldName)) {
            getMessager().message(sender, Language.WORLD_ALREADY_EXISTS, worldName);
            return true;
        }

        final String seed = context.getFlag('s');
        final String generator = context.getFlag('g');
        final WorldType worldType = context.hasFlag('t') ? WorldType.getFromString(context.getFlag('t')) : null;
        final Boolean generateStructures = context.hasFlag('a') ? Boolean.valueOf(context.getFlag('a')) : null;

        final WorldEnvironment environment = WorldEnvironment.getFromString(context.getString(1));
        if (environment == null) {
            getMessager().message(sender, CREATE_FAILED);
            getMessager().message(sender, Language.INVALID_ENVIRONMENT, context.getString(1));
            // TODO EnvironmentCommand.showEnvironments(sender);
            return true;
        }
        if (context.hasFlag('t') && worldType == null) {
            getMessager().message(sender, CREATE_FAILED);
            getMessager().message(sender, Language.INVALID_WORLD_TYPE, context.getFlag('t'));
            // TODO TypeCommand.showTypes(sender);
            return true;
        }

        try {
            getMessager().message(sender, CREATING_WORLD);
            getPlugin().getWorldManager().addWorld(worldName, environment, seed, worldType, generateStructures, generator);
            getMessager().messageAndLog(sender, CREATE_SUCCESS, worldName);
        } catch (WorldCreationException e) {
            getMessager().message(sender, CREATE_FAILED);
            e.sendException(getMessager(), sender);
        }

        return true;
    }
}
