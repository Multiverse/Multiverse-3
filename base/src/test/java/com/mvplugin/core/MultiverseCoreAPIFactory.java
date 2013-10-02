package com.mvplugin.core;

import com.mvplugin.core.util.BlockSafety;
import org.powermock.api.mockito.PowerMockito;
import pluginbase.plugin.ServerInterface;

public class MultiverseCoreAPIFactory {

    public static MultiverseCoreAPI getMockedMultiverseCoreAPI() {
        return PowerMockito.mock(MultiverseCoreAPI.class);
    }

    public static MultiverseCoreAPI getMultiverseCoreAPI(ServerInterface serverInterface, WorldManagerUtil worldManagerUtil, BlockSafety blockSafety) {
        return new DefaultMultiverseCoreAPI(serverInterface, worldManagerUtil, blockSafety);
    }
}
