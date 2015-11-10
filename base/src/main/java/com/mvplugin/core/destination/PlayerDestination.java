package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import com.mvplugin.core.exceptions.PermissionException;
import com.mvplugin.core.exceptions.TeleportException;
import com.mvplugin.core.util.Language.Destination.Player;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.permission.Permissible;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.mvplugin.core.util.Language.Destination.Player.*;

/**
 * This destination type teleports to a player.
 */
public final class PlayerDestination extends SimpleDestination {

    static final Set<String> PREFIXES = new CopyOnWriteArraySet<String>() {{
        add("player");
        add("pl");
    }};

    @NotNull
    private final String playerName;

    PlayerDestination(@NotNull MultiverseCoreAPI api, @NotNull String playerName) {
        super(api);
        this.playerName = playerName;
    }

    @NotNull
    @Override
    protected EntityCoordinates getDestination() throws TeleportException {
        BasePlayer player = this.getApi().getServerInterface().getPlayer(playerName);
        if (player instanceof Entity) {
            return ((Entity) player).getLocation();
        } else {
            throw new TeleportException(Message.bundleMessage(NOT_FOUND, playerName));
        }
    }

    @NotNull
    @Override
    public String getDestinationString() {
        return DestinationUtil.colonJoin("player", playerName);
    }

    @Override
    protected void checkPermissions(@NotNull Permissible teleporter, @NotNull Entity teleportee) throws PermissionException {
        super.checkPermissions(teleporter, teleportee);
        if (teleporter.equals(teleportee)) {
            if (!teleporter.hasPerm(Perms.TP_SELF_PLAYER, playerName)) {
                throw new PermissionException(Message.bundleMessage(Player.NO_PERMISSION, teleportee.getName(),
                        playerName, Perms.TP_SELF_PLAYER.getName(playerName)), Perms.TP_SELF_PLAYER);
            }
        } else if (!teleporter.equals(teleportee)) {
            if (!teleporter.hasPerm(Perms.TP_OTHER_PLAYER, playerName)) {
                throw new PermissionException(Message.bundleMessage(Player.NO_PERMISSION, teleportee.getName(),
                        playerName, Perms.TP_OTHER_PLAYER.getName(playerName)), Perms.TP_OTHER_PLAYER);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && playerName.equals(((PlayerDestination) o).playerName);
    }

    @Override
    public int hashCode() {
        return playerName.hashCode();
    }

    static class Factory implements DestinationFactory {
        @NotNull
        @Override
        public PlayerDestination createDestination(@NotNull MultiverseCoreAPI api, @NotNull String destinationString) throws InvalidDestinationException {
            destinationString = DestinationUtil.removePrefix(destinationString);
            return new PlayerDestination(api, destinationString);
        }

        @NotNull
        @Override
        public Set<String> getDestinationPrefixes() {
            return PREFIXES;
        }
    }
}
