package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messages.Message;

public class BukkitLanguage {
    public static final Message CREATE_WORLD_ERROR = new Message("worlds.create_world_error",
            "&aBukkit&f experienced a problem while attempting to create '&b%s&f'!");
    public static final Message ALREADY_BUKKIT_WORLD = new Message("worlds.bukkit_world_exists",
            "&aBukkit&f already knows about a world named '&b%s&f'.  Try importing it instead!");
    public static final Message WGEN_UNKNOWN_GENERATOR = new Message("worlds.create.unknown_generator",
            "I can't create a &bBukkit&f world that uses the generator &c%s&f because it doesn't exist!");
    public static final Message WGEN_DISABLED_GENERATOR = new Message("worlds.create.unknown_generator",
            "I can't create a &bBukkit&f world that uses the generator &c%s&f because it is not enabled!");

    public static final Message THIS_WILL_DELETE_THE_FOLLOWING = new Message("worlds.delete.this_will_delete_the_following",
            ChatColor.RED + "The following will be deleted: %s");
}
