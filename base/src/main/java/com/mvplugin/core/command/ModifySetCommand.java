package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

import static com.mvplugin.core.util.Language.Command.Modify.Set.HELP;

@CommandInfo(
        primaryAlias = "modify set",
        directlyPrefixPrimary = true,
        desc = "Modifies the properties of a world.",
        usage = "{PROPERTY} {VALUE} [WORLD]",
        directlyPrefixedAliases = {"m set", "mset"},
        min = 0,
        max = 3
)
public class ModifySetCommand extends ModifyCommandBase {
    protected ModifySetCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return null;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Override
    public boolean runCommand(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        if (context.argsLength() == 0) {
            showPropertyList(sender);
        } else if (context.argsLength() == 1) {
            showPropertyDescription(sender, context.getString(0));
        } else {
            try {
                MultiverseWorld world = getWorldFromContext(sender, context, 2);
                if (Perms.CMD_MODIFY.hasPermission(sender, world.getName())) {
                    // TODO
                } else {
                    getMessager().message(sender, Language.Command.Modify.NO_MODIFY_PERMISSION, world.getName());
                }
            } catch (MultiverseException e) {
                e.sendException(getMessager(), sender);
            }
        }
        return true;
    }
}
