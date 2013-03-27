package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.Message;

public class BukkitLanguage {
    public static final Message CREATE_WORLD_ERROR = Message.createMessage("worlds.create_world_error",
            "$TBukkit $-experienced a problem while attempting to create '$v%s$-'!");
    public static final Message CREATE_WORLD_FILE_ERROR = Message.createMessage("worlds.create_world_file_error",
            "$tMultiverse $-was unable to create the persistence file $v%s");
    public static final Message ALREADY_BUKKIT_WORLD = Message.createMessage("worlds.bukkit_world_exists",
            "$TBukkit $ialready knows about a world named '$v%s$i'.  $,Try importing it instead!");
    public static final Message WGEN_UNKNOWN_GENERATOR = Message.createMessage("worlds.create.unknown_generator",
            "$$I can't create a $TBukkit $$world that uses the generator $v%s$$ because the generator does not exist!");
    public static final Message WGEN_DISABLED_GENERATOR = Message.createMessage("worlds.create.unknown_generator",
            "$$I can't create a $TBukkit $$world that uses the generator $v%s$$ because the generator is not enabled!");

    public static final Message THIS_WILL_DELETE_THE_FOLLOWING = Message.createMessage("worlds.delete.this_will_delete_the_following",
            "$i$!The following will be deleted: $v%s");

    public static final Message WORLD_FILE_NOT_FOUND = Message.createMessage("worlds.file_not_found", "$^The world file was not found: $v%s");
    public static final Message WORLD_COULD_NOT_DELETE_FILE = Message.createMessage("worlds.could_not_delete_file", "$-The world file could not be deleted: $v%s");
}
