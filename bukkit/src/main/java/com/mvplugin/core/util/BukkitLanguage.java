package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messages.Message;

public class BukkitLanguage {
    public static final Message CREATE_WORLD_ERROR = Message.createMessage("worlds.create_world_error",
            "&aBukkit&f experienced a problem while attempting to create '&b%s&f'!");
    public static final Message ALREADY_BUKKIT_WORLD = Message.createMessage("worlds.bukkit_world_exists",
            "&aBukkit&f already knows about a world named '&b%s&f'.  Try importing it instead!");
    public static final Message WGEN_UNKNOWN_GENERATOR = Message.createMessage("worlds.create.unknown_generator",
            "I can't create a &bBukkit&f world that uses the generator &c%s&f because it doesn't exist!");
    public static final Message WGEN_DISABLED_GENERATOR = Message.createMessage("worlds.create.unknown_generator",
            "I can't create a &bBukkit&f world that uses the generator &c%s&f because it is not enabled!");

    public static final Message THIS_WILL_DELETE_THE_FOLLOWING = Message.createMessage("worlds.delete.this_will_delete_the_following",
            ChatColor.RED + "The following will be deleted: %s");

    public static final Message WORLD_FILE_NOT_FOUND = Message.createMessage("worlds.file_not_found", "The world file was not found: %s");
    public static final Message WORLD_COULD_NOT_DELETE_FILE = Message.createMessage("worlds.could_not_delete_file", "The world file could not be deleted: %s");
}
