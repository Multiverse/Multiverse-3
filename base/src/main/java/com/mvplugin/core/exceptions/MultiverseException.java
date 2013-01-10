package com.mvplugin.core.exceptions;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messaging.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messaging.Messager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiverseException extends Exception {

    @NotNull
    private final BundledMessage languageMessage;

    @Nullable
    private MultiverseException cause;

    public MultiverseException(@NotNull final BundledMessage b) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault()), b.getArgs()));
        this.languageMessage = b;
    }

    public MultiverseException(@NotNull final BundledMessage b, @NotNull final Throwable throwable) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault()), b.getArgs()), throwable);
        this.languageMessage = b;
    }

    public MultiverseException(@NotNull final BundledMessage languageMessage, @NotNull final MultiverseException mvException) {
        this(languageMessage, (Throwable) mvException);
        this.cause = mvException;
    }

    public MultiverseException(@NotNull final MultiverseException e) {
        this(e.getBundledMessage(), e.getCause());
    }

    @NotNull
    public BundledMessage getBundledMessage() {
        return this.languageMessage;
    }

    @Nullable
    public MultiverseException getCauseException() {
        return this.cause;
    }

    public void sendException(@NotNull Messager messager, @NotNull final BasePlayer player) {
        messager.message(player, getBundledMessage().getMessage(), getBundledMessage().getArgs());
        if (getCauseException() != null) {
            messager.message(player, getCauseException().getBundledMessage().getMessage(), getCauseException().getBundledMessage().getArgs());
        } else if (getCause() != null) {
            messager.message(player, getCause().getMessage());
        }
    }
}
