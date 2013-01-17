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
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " is unaware of the world '&b%s&f'.  Has it been imported? (/mv import)");

    public static final Message WORLD_LOAD_ERROR = new Message("world.load.error",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " experienced a problem while attempting to load '&b%s&f'!");

    public static final Message WORLD_UNLOAD_ERROR = new Message("world.unload.error",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " experienced a problem while attempting to unload '" + ChatColor.AQUA + "%s" + ChatColor.WHITE + "'.");

    public static final Message WORLD_COULD_NOT_UNLOAD_FROM_SERVER = new Message("world.unload.could_not_unload_from_server",
            ChatColor.WHITE + "World '" + ChatColor.AQUA + "%s" + ChatColor.WHITE + "' could not be unloaded.  Is it a default world?");

    public static final Message WORLD_REMOVE_ERROR = new Message("world.remove.error",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " experienced a problem while attempting to remove '" + ChatColor.AQUA + "%s" + ChatColor.WHITE + "'.");

    public static final Message CANNOT_DELETE_UNMANAGED = new Message("world.delete.must_be_managed",
            ChatColor.RED + "You may only delete worlds that Multiverse manages.");

    public static final Message CANNOT_DELETE_NONWORLD = new Message("world.delete.not_a_world",
            ChatColor.DARK_GRAY + "Sorry, %s does not appear to be a world.");

    public static final Message WORLD_DELETE_ERROR = new Message("world.delete.error",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " experienced a problem while attempting to delete '&b%s&f'!");

    public static final Message WORLD_DELETE_FAILED = new Message("world.delete.failed",
            ChatColor.GREEN + "Multiverse" + ChatColor.WHITE + " was unable to fully delete world '&b%s&f'."
            + "\n" + ChatColor.GRAY + ChatColor.ITALIC + "This probably means the files are in use somewhere else."
            + "\n" + ChatColor.RESET + ChatColor.GRAY + "The Multiverse world files " + ChatColor.UNDERLINE + "have" + ChatColor.RESET + ChatColor.GRAY + " been removed."
            + "\n" + ChatColor.DARK_GRAY + ChatColor.UNDERLINE + "You will need to import the world to attempt delete again.");
}
