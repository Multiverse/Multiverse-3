package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
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
        directlyPrefixedAliases = {"c", "create"},
        flags = "s:g:t:a",
        min = 2,
        max = 2
)
public class CreateCommand extends MultiverseCommand {

    public static final Message CREATE_HELP = Message.createMessage("command.create.help",
            "$hCreates a new world on your server with the given name."
            + "\n$hYou must specify a world environment such as $0NORMAL $hor $1NETHER$h."
            + "\n$hYou may specify a world seed."
            + "\n$hYou may also specify a generator to use along with an optional generator ID."
            + "\n$hThe generator name is case sensitive!"
            + "\n$hYou may specify a world type such as FLAT or LARGE_BIOMES"
            + "\n$hYou may specify if $tMultiverse $hshould declare to generate structures or not."
            + "\n$hFlags:"
            + "\n$f  -s $r{SEED} $hSpecify a world seed to use."
            + "\n$f  -g $r{GENERATOR$o[:ID]$r} $hSpecify a generator."
            + "\n$f  -t $r{TYPE} $hSpecify a world type."
            + "\n$f  -a $r{true|false} $hSpecify whether or not to generate structures."
            + "\n$hExamples:"
            + "\n$c  /mv create $rgargamel $0normal"
            + "\n$c  /mv create $r\"hell world\" $1nether"
            + "\n$c  /mv create $rspace $0normal $f-g $rCleanroomGenerator$o:.");

    public static final Message CREATE_FAILED = Message.createMessage("command.create.failed",
            "$-Create failed!");

    public static final Message CREATING_WORLD = Message.createMessage("command.create.creating",
            "$wCreating new world, please wait...");

    public static final Message CREATE_SUCCESS = Message.createMessage("command.create.success",
            "$+Successfully created world $!%s$+!");

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
