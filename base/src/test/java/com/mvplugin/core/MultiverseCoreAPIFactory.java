package com.mvplugin.core;

import org.powermock.api.mockito.PowerMockito;

public class MultiverseCoreAPIFactory {

    public static MultiverseCoreAPI getMockedMultiverseCoreAPI() {
        return PowerMockito.mock(MultiverseCoreAPI.class);
    }

    /*
    public static MultiverseCoreAPI getMultiverseCoreAPI(ServerInterface serverInterface, WorldManagerUtil worldManagerUtil, BlockSafety blockSafety) {
        return new DefaultMultiverseCoreAPI(serverInterface, worldManagerUtil, blockSafety);
    }
    */
}
