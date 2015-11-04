package com.mvplugin.core;

import org.jetbrains.annotations.NotNull;

public class WorldManagerFactory {

    public static WorldManager getWorldManager(@NotNull MultiverseCoreAPI api) throws Exception {
        return new WorldManager(api, WorldManagerUtilFactory.getMockedWorldManagerUtil());
    }
}
