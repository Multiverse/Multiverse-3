package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;

@CommandInfo(
        primaryAlias = "create",
        desc = "Creates a new world.",
        usage = "{NAME} {ENVIRONMENT}",
        prefixedAliases = "c",
        directlyPrefixedAliases = "create",
        flags = "s:g:t:a",
        min = 2,
        max = 2
)
public class CreateCommand extends MultiverseCommand {

    public static final Message CREATE_HELP = Message.createMessage("command.create.help",
            "Creates a new world on your server with the given name."
            + "\nYou must specify a world environment such as NORMAL or NETHER."
            + "\nYou may specify a world seed."
            + "\nYou may also specify a generator to use along with an optional generator ID."
            + "\nThe generator name is case sensitive!"
            + "\nYou may specify a world type such as FLAT or LARGE_BIOMES"
            + "\nYou may specify if Multiverse should declare to generate structures or not."
            + "\nFlags:"
            + "\n  -s {SEED} Specify a world seed to use."
            + "\n  -g {GENERATOR[:ID]} Specify a generator."
            + "\n  -t {TYPE} Specify a world type."
            + "\n  -a {true|false} Specify whether or not to generate structures."
            + "\nExamples:"
            + "\n  /mv create &6gargamel&a normal"
            + "\n  /mv create &6hell_world&a nether"
            + "\n  /mv create &6Cleanroom&a normal -g &3CleanroomGenerator");

    public static final Message CREATE_FAILED = Message.createMessage("command.create.failed",
            "&cCreate failed!");

    public static final Message CREATING_WORLD = Message.createMessage("command.create.creating",
            "&aCreating new world, please wait.");

    public static final Message CREATE_SUCCESS = Message.createMessage("command.create.success",
            "&aSuccessfully created world " + ChatColor.BOLD + "%s" + ChatColor.RESET + "&a!");

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
