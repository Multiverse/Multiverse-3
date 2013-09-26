package com.mvplugin.core.command;

import com.mvplugin.core.exceptions.WorldCreationException;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

import java.io.File;

import static com.mvplugin.core.util.Language.Command.Import.*;

@CommandInfo(
        primaryAlias = "import",
        desc = "Imports an existing world.",
        usage = "{NAME} {ENVIRONMENT}",
        prefixedAliases = "im",
        directlyPrefixedAliases = "import",
        flags = "g:n",
        min = 1,
        max = 2
)
public class ImportCommand extends MultiverseCommand {
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
        return HELP;
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
            getMessager().message(sender, IMPORT_FAILED);
            getMessager().message(sender, Language.INVALID_ENVIRONMENT, env);
            // TODO EnvironmentCommand.showEnvironments(sender);
            return true;
        }

        if (worldFile.exists() && env != null) {
            getMessager().messageAndLog(sender, STARTING_IMPORT, worldName);
            try {
                getPlugin().getWorldManager().addWorld(worldName, environment, null, null, null, generator, useSpawnAdjust);
                getMessager().messageAndLog(sender, IMPORT_COMPLETE);
            } catch (WorldCreationException e) {
                getMessager().message(sender, IMPORT_FAILED);
                e.sendException(getMessager(), sender);
            }
        } else {
            getMessager().message(sender, IMPORT_FAILED);
            String worldList = this.getPotentialWorlds();
            getMessager().message(sender, NON_EXISTENT_FOLDER, worldList);
        }
        return true;
    }

    @NotNull
    private String getPotentialWorlds() {
        final StringBuilder worldList = new StringBuilder();
        int i = 1;
        for (final String potentialWorld : getPlugin().getWorldManager().getPotentialWorlds()) {
            if (!getPlugin().getWorldManager().isManaged(potentialWorld)) {
                if (i % 2 == 0) {
                    worldList.append(Theme.LIST_EVEN);
                } else {
                    worldList.append(Theme.LIST_ODD);
                }
                worldList.append(potentialWorld).append(" ");
                i++;
            }
        }
        return worldList.toString();
    }
}
