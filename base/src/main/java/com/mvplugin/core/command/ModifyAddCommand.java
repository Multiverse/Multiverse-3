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

import static com.mvplugin.core.util.Language.Command.Modify.Add.*;
import static com.mvplugin.core.util.Language.Command.Modify.*;

@CommandInfo(
        primaryAlias = "modify add",
        directlyPrefixPrimary = false,
        desc = "Modifies the properties of a world.",
        usage = "{PROPERTY} {VALUE} [WORLD]",
        directlyPrefixedAliases = {"m add", "madd", "modify add"},
        min = 0,
        max = 3
)
public class ModifyAddCommand extends ModifyCommandBase {
    protected ModifyAddCommand(@NotNull final MultiverseCore plugin) {
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
                    String propertyName = context.getString(0);
                    String value = context.getString(1);
                    try {
                        world.addProperty(propertyName, value);
                        getWorldManager().saveWorld(world);
                        Object realValue = world.getProperty(propertyName);
                        getMessager().message(sender, SUCCESS, value, propertyName, realValue);
                    } catch (IllegalAccessException e) {
                        getMessager().message(sender, PROPERTY_CANNOT_BE_ADDED, propertyName);
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
