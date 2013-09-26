package com.mvplugin.core.destination;

import com.mvplugin.core.exceptions.TeleportException;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;

import static com.mvplugin.core.util.Language.Destination.Player.*;

/**
 * This destination type teleports to a player.
 */
public final class PlayerDestination extends SimpleDestination {
    private static final String PREFIX = "player:";

    @NotNull
    private String playerName;

    public PlayerDestination() { }
    public PlayerDestination(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        BasePlayer player = this.getApi().getServerInterface().getPlayer(playerName);
        if (player == null)
            throw new TeleportException(Message.bundleMessage(NOT_FOUND));
        if (player instanceof Entity) {
            return ((Entity) player).getLocation();
        } else throw new TeleportException(Message.bundleMessage(OFFLINE));
    }

    @Override
    public boolean tryParse(final String str) {
        if (!str.startsWith(PREFIX))
            return false;

        playerName = str.substring(PREFIX.length());
        return true;
    }

    @Override
    public String serialize() {
        return PREFIX + playerName;
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || !(o == null || getClass() != o.getClass())
                && playerName.equals(((PlayerDestination) o).playerName);
    }

    @Override
    public int hashCode() {
        return playerName.hashCode();
    }
}
