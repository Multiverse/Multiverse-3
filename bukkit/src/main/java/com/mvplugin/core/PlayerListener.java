package com.mvplugin.core;

import com.mvplugin.core.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.messages.ChatColor;

class PlayerListener implements Listener {

    @NotNull
    private final MultiverseCoreBukkitPlugin plugin;

    public PlayerListener(@NotNull final MultiverseCoreBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerChangeWorld(@NotNull final PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        plugin.getEventProcessor().playerJoin(player.getName(), player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(@NotNull final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getEventProcessor().playerJoin(player.getName(), player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(@NotNull final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getEventProcessor().playerQuit(player.getName());
    }

    // This has to be handled in the bukkit module since its chat format is an implementation detail.
    @EventHandler(ignoreCancelled = true)
    public void playerChat(AsyncPlayerChatEvent event) {
        if (plugin.getMVConfig().isFormattingChat()) {
            MultiverseWorld world = plugin.getPlayerTracker().getWorld(event.getPlayer().getName());
            if (isWorldChatFormattable(world)) {
                String chatFormatString = plugin.getMVConfig().getChatFormatString();
                try {
                    ChatUtil.validateChat(chatFormatString);
                } catch (PropertyVetoException e) {
                    plugin.getLog().severe(plugin.getMessager().getLocalizedMessage(e.getBundledMessage().getMessage(), e.getBundledMessage().getArgs()));
                    return;
                }
                chatFormatString = chatFormatString.replace(ChatUtil.WORLD_MARKER, world.getAlias());
                chatFormatString = ChatColor.translateAlternateColorCodes(ChatUtil.COLOR_CHAR, chatFormatString);
                chatFormatString = chatFormatString.replace(ChatUtil.PLAYER_MARKER, "%s");
                chatFormatString = chatFormatString.replace(ChatUtil.MESSAGE_MARKER, "%s");
                event.setFormat(chatFormatString);
            }
        }
    }

    private boolean isWorldChatFormattable(MultiverseWorld world) {
        return world != null && world.isFormattingChat() && !world.isHidden();
    }
}
