package com.mvplugin.core;

import org.powermock.api.mockito.PowerMockito;

public class WorldManagerFactory {

    public static WorldManager getWorldManager() throws Exception {
        return new WorldManager(MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI(), WorldManagerUtilFactory.getMockedWorldManagerUtil());
    }

    public static WorldManager getMockedWorldManager() throws Exception {
        WorldManager worldManager = PowerMockito.mock(WorldManager.class);
        return worldManager;
    }
}
