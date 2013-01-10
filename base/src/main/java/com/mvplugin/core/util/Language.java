package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messaging.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;

public class Language {

    public static final Message WORLD_ALREADY_EXISTS = new Message("worlds.world_exists",
            "&aMultiverse&f already knows about '&b%s&f'.  Perhaps it needs to be loaded? (/mv load)");

    public static final Message WORLD_ALREADY_LOADED = new Message("world.already_loaded",
            ChatColor.RED + "The world '" + ChatColor.AQUA + "%s" + ChatColor.RED + "' is already loaded!");

    public static final Message WORLD_ALREADY_UNLOADED = new Message("world.already_unloaded",
            ChatColor.RED + "The world '" + ChatColor.AQUA + "%s" + ChatColor.RED + "' is already unloaded!");

    public static final Message WORLD_NOT_MANAGED = new Message("world.not_managed",
            "&aMultiverse&f is unaware of the world '&b%s&f'.  Has it been imported? (/mv import)");

    public static final Message WORLD_LOAD_ERROR = new Message("world.load_error",
            "&aMultiverse&f experienced a problem while attempting to load '&b%s&f'!");

    public static final Message WORLD_UNLOAD_ERROR = new Message("world.unload_error",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " experienced a problem while attempting to unload '" + ChatColor.AQUA + "%s" + ChatColor.WHITE + "'.");

    public static final Message WORLD_COULD_NOT_UNLOAD_FROM_SERVER = new Message("world.could_not_unload_from_server",
            ChatColor.WHITE + "World '" + ChatColor.AQUA + "%s" + ChatColor.WHITE + "' could not be unloaded.  Is it a default world?");
}
