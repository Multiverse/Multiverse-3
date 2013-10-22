package com.mvplugin.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.jetbrains.annotations.NotNull;

class WeatherListener implements Listener {

    @NotNull
    private final MultiverseCoreBukkitPlugin plugin;

    public WeatherListener(@NotNull final MultiverseCoreBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void weatherChange(WeatherChangeEvent event) {
        MultiverseWorld mvWorld = plugin.getWorldManager().getWorld(event.getWorld().getName());
        if (mvWorld != null) {
            event.setCancelled(event.toWeatherState() && !mvWorld.isWeatherEnabled());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void thunderChange(ThunderChangeEvent event) {
        MultiverseWorld mvWorld = plugin.getWorldManager().getWorld(event.getWorld().getName());
        if (mvWorld != null) {
            event.setCancelled(event.toThunderState() && !mvWorld.isWeatherEnabled());
        }
    }
}
