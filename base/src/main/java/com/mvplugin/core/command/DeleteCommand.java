package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.mvplugin.core.util.Language.Command.Delete.*;

@CommandInfo(
        primaryAlias = "delete",
        desc = "Deletes a world.",
        usage = "{NAME}",
        directlyPrefixedAliases = "delete",
        min = 1,
        max = 1
)
public class DeleteCommand extends QueuedMultiverseCommand {

    private static final long CONFIRMATION_TIME = 10000L;

    protected DeleteCommand(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_DELETE;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Nullable
    @Override
    protected BundledMessage getConfirmMessage() {
        return null;
    }

    @Override
    public long getExpirationDuration() {
        return CONFIRMATION_TIME;
    }

    @Override
    protected boolean preConfirm(@NotNull final BasePlayer basePlayer, @NotNull final CommandContext commandContext) {
        final String worldName = commandContext.getString(0);
        if (!getPlugin().getWorldManager().isManaged(worldName)) {
            getMessager().message(basePlayer, Language.CANNOT_DELETE_UNMANAGED);
            return true;
        }
        if (!getPlugin().getWorldManager().isThisAWorld(worldName)) {
            getMessager().message(basePlayer, Language.CANNOT_DELETE_NONWORLD, worldName);
            return true;
        }
        getMessager().message(basePlayer, getPlugin().getWorldManager().whatWillThisDelete(worldName));
        return true;
    }

    @Override
    protected void onConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext commandContext) {
        final String worldName = commandContext.getString(0);
        try {
            getMessager().message(sender, DELETING_WORLD, worldName);
            getPlugin().getWorldManager().deleteWorld(worldName, true);
            getMessager().message(sender, DELETED_WORLD, worldName);
        } catch (final WorldManagementException e) {
            e.sendException(getMessager(), sender);
        }
    }

    @Override
    protected void onExpire(@NotNull final BasePlayer basePlayer, @NotNull final CommandContext commandContext) {

    }
}
