package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

import static com.mvplugin.core.util.Language.Command.Modify.Clear.HELP;
import static com.mvplugin.core.util.Language.Command.Modify.Clear.PROPERTY_CANNOT_BE_CLEARED;
import static com.mvplugin.core.util.Language.Command.Modify.Clear.SUCCESS;
import static com.mvplugin.core.util.Language.Command.Modify.NO_MODIFY_PERMISSION;
import static com.mvplugin.core.util.Language.Command.Modify.NO_SUCH_PROPERTY;
import static com.mvplugin.core.util.Language.Command.Modify.PROBABLY_INVALID_VALUE;

@CommandInfo(
        primaryAlias = "modify clear",
        directlyPrefixPrimary = true,
        desc = "Modifies the properties of a world.",
        usage = "{PROPERTY} {VALUE} [WORLD]",
        directlyPrefixedAliases = {"m clear", "mclear"},
        min = 0,
        max = 2
)
public class ModifyClearCommand extends ModifyCommandBase {
    protected ModifyClearCommand(@NotNull final MultiverseCore plugin) {
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
        } else {
            try {
                MultiverseWorld world = getWorldFromContext(sender, context, 1);
                if (Perms.CMD_MODIFY.hasPermission(sender, world.getName())) {
                    String propertyName = context.getString(0);
                    try {
                        world.clearProperty(propertyName);
                        getWorldManager().saveWorld(world);
                        getMessager().message(sender, SUCCESS, propertyName);
                    } catch (IllegalAccessException e) {
                        getMessager().message(sender, PROPERTY_CANNOT_BE_CLEARED, propertyName);
                    } catch (NoSuchFieldException e) {
                        getMessager().message(sender, NO_SUCH_PROPERTY, propertyName);
                    } catch (PropertyVetoException e) {
                        e.sendException(getMessager(), sender);
                    } catch (IllegalArgumentException e) {
                        throw new MultiverseException(Message.bundleMessage(PROBABLY_INVALID_VALUE, propertyName), e);
                    }
                } else {
                    getMessager().message(sender, NO_MODIFY_PERMISSION, world.getName());
                }
            } catch (MultiverseException e) {
                e.sendException(getMessager(), sender);
            }
        }
        return true;
    }
}
