package com.mvplugin.core;

import pluginbase.plugin.ServerInterface;

public class BukkitAPIFactory {

    public static MultiverseCoreAPI getAPI() throws Exception {
        MockServer.prepareBukkit();
        ServerInterface serverInterface = ServerInterfaceMocker.getMockedServerInterface();
        WorldManagerUtil worldManagerUtil = new BukkitWorldManagerUtil(serverInterface, FileLocations.MULTIVERSE_DIRECTORY);
        return MultiverseCoreAPIFactory.getMultiverseCoreAPI(serverInterface, worldManagerUtil, new BukkitBlockSafety());
    }
}
