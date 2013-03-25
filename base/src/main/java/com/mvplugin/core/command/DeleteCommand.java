package com.mvplugin.core.command;

import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.Theme;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.mvplugin.core.exceptions.WorldManagementException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static final Message DELETE_HELP = Message.createMessage("command.delete.help",
            Theme.HELP + "Deletes a world from the server, removing it from Multiverse's management."
                    + "\n" + Theme.HELP + "The world must be managed by Multiverse to use this command."
                    + "\n" + Theme.HELP + "Examples:"
                    + "\n" + Theme.CMD_USAGE + "  /mv delete " + Theme.REQ_ARG + "gargamel");

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
        return DELETE_HELP;
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
        getMessager().message(basePlayer, getPlugin().getWorldManager().whatWillThisDelete(commandContext.getString(0)));
        return true;
    }

    @Override
    protected void onConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext commandContext) {
        try {
            getPlugin().getWorldManager().deleteWorld(commandContext.getString(0), true);
        } catch (final WorldManagementException e) {
            e.sendException(getMessager(), sender);
        }
    }

    @Override
    protected void onExpire(@NotNull final BasePlayer basePlayer, @NotNull final CommandContext commandContext) {

    }
}
