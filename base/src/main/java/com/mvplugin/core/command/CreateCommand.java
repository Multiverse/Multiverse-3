package com.mvplugin.core.command;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.plugin.PluginBase;

import static com.mvplugin.core.util.Language.Command.Create.*;

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
    protected CreateCommand(@NotNull final PluginBase<MultiverseCore> plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_CREATE;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
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
            getMessager().message(sender, FAILED);
            getMessager().message(sender, Language.INVALID_ENVIRONMENT, context.getString(1));
            // TODO EnvironmentCommand.showEnvironments(sender);
            return true;
        }
        if (context.hasFlag('t') && worldType == null) {
            getMessager().message(sender, FAILED);
            getMessager().message(sender, Language.INVALID_WORLD_TYPE, context.getFlag('t'));
            // TODO TypeCommand.showTypes(sender);
            return true;
        }

        try {
            getMessager().message(sender, CREATING_WORLD);
            getPlugin().getWorldManager().addWorld(worldName, environment, seed, worldType, generateStructures, generator);
            getMessager().messageAndLog(sender, SUCCESS, worldName);
        } catch (WorldCreationException e) {
            getMessager().message(sender, FAILED);
            e.sendException(getMessager(), sender);
        }

        return true;
    }
}
