package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messaging.ChatColor;
import org.jetbrains.annotations.NotNull;

public class MultiverseException extends Exception {

    @NotNull
    private final BundledMessage languageMessage;

    public MultiverseException(@NotNull final BundledMessage b) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault()), b.getArgs()));
        this.languageMessage = b;
    }

    public MultiverseException(@NotNull final BundledMessage b, @NotNull final Throwable throwable) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault()), b.getArgs()), throwable);
        this.languageMessage = b;
    }

    @NotNull
    public BundledMessage getBundledMessage() {
        return this.languageMessage;
    }
}
