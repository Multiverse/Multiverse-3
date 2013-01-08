package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;

public class Language {

    public static final Message WORLD_ALREADY_EXISTS = new Message("worlds.world_exists",
            "&aMultiverse&f already knows about '&b%s&f'.  Perhaps it needs to be loaded? (/mv load)");

    public static final Message WORLD_ALREADY_LOADED = new Message("world.already_loaded",
            "The world '&b%s&f' is already loaded!");

    public static final Message WORLD_NOT_MANAGED = new Message("world.not_managed",
            "&aMultiverse&f is unaware of the world '&b%s&f'.  Has it been imported? (/mv import)");

    public static final Message WORLD_LOAD_ERROR = new Message("world.load_error",
            "&Multiverse&f experienced a problem while attempting to load '&b%s&f'!");
}
