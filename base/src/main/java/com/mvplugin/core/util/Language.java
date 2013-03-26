package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.Message;

public class Language {

    public static final Message WORLD_ALREADY_EXISTS = Message.createMessage("worlds.world_exists",
            "$tMultiverse $-already knows about '$v%s$-'.  $?Perhaps it needs to be loaded? ($~$C/mv load$?)");

    public static final Message WORLD_ALREADY_LOADED = Message.createMessage("world.already_loaded",
            "$-The world '$v%s$-' is already loaded!");

    public static final Message WORLD_ALREADY_UNLOADED = Message.createMessage("world.already_unloaded",
            "$-The world '$v%s$-' is already unloaded!");

    public static final Message WORLD_NOT_MANAGED = Message.createMessage("world.not_managed",
            "$tMultiverse $-is unaware of the world '$v%s$-'.  $?Has it been imported? ($~$C/mv import$?)");

    public static final Message WORLD_LOAD_ERROR = Message.createMessage("world.load.error",
            "$tMultiverse $-experienced a problem while attempting to load '$v%s$-'!");

    public static final Message WORLD_UNLOAD_ERROR = Message.createMessage("world.unload.error",
            "$tMultiverse $-experienced a problem while attempting to unload '$v%s$-'.");

    public static final Message WORLD_COULD_NOT_UNLOAD_FROM_SERVER = Message.createMessage("world.unload.could_not_unload_from_server",
            "$-World '$v%s$-' could not be unloaded.  $?Is it a default world?");

    public static final Message WORLD_REMOVE_ERROR = Message.createMessage("world.remove.error",
            "$tMultiverse $-experienced a problem while attempting to remove '$v%s$-'.");

    public static final Message CANNOT_DELETE_UNMANAGED = Message.createMessage("world.delete.must_be_managed",
             "$$You may only delete worlds that Multiverse manages.");

    public static final Message CANNOT_DELETE_NONWORLD = Message.createMessage("world.delete.not_a_world",
            "$$Sorry, $v%s$$ does not appear to be a world.");

    public static final Message WORLD_DELETE_ERROR = Message.createMessage("world.delete.error",
            "$tMultiverse $-experienced a problem while attempting to delete '$v%s$-'!");

    public static final Message WORLD_DELETE_PERSISTENCE_ERROR = Message.createMessage("world.delete.persistence_error",
            "$tMultiverse $-experienced a problem while attempting to delete the $tMultiverse$- information for '$v%s$-'!"
            + "\n$iYou may attempt to remove this with $~$C/mv remove");

    public static final Message WORLD_DELETE_FAILED = Message.createMessage("world.delete.failed",
            "$tMultiverse $-was unable to fully delete world '$v%s$-'."
            + "\n$i$*This probably means the files are in use somewhere else."
            + "\n$,You may attempt to delete it again with Multiverse but you will probably need to delete it manually.");

    public static final Message INVALID_ENVIRONMENT = Message.createMessage("worlds.invalid_environment",
            "$v%s $-is not a valid world environment."
            + "\n$iFor a list of available world environments, $~type $C/mvenv");
    public static final Message INVALID_WORLD_TYPE = Message.createMessage("worlds.invalid_type",
            "$v%s $-is not a valid world type."
            + "\n$iFor a list of available world types, $~type $C/mvtype");
}
