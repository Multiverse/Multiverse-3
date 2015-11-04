package com.mvplugin.core;

import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.SafeTeleporter;
import com.mvplugin.testing.ServerInterfaceFactory;
import org.powermock.api.mockito.PowerMockito;
import pluginbase.minecraft.location.BlockCoordinates;
import pluginbase.plugin.ServerInterface;

import static org.mockito.Mockito.*;

public class MultiverseCoreAPIFactory {

    public static MultiverseCoreAPI getMockedMultiverseCoreAPI() throws Exception {
        MultiverseCoreAPI api = PowerMockito.mock(MultiverseCoreAPI.class);

        SafeTeleporter safeTeleporter = new DefaultSafeTeleporter(api);
        when(api.getSafeTeleporter()).thenReturn(safeTeleporter);

        ServerInterface serverInterface = ServerInterfaceFactory.getMockedServerInterface();
        when(api.getServerInterface()).thenReturn(serverInterface);

        BlockSafety blockSafety = PowerMockito.mock(BlockSafety.class);
        when(blockSafety.isSafeLocation(any(BlockCoordinates.class))).thenReturn(true);
        when(api.getBlockSafety()).thenReturn(blockSafety);

        WorldManager worldManager = WorldManagerFactory.getWorldManager(api);
        when(api.getWorldManager()).thenReturn(worldManager);

        return api;
    }

    /*
    public static MultiverseCoreAPI getMultiverseCoreAPI(ServerInterface serverInterface, WorldManagerUtil worldManagerUtil, BlockSafety blockSafety) {
        return new DefaultMultiverseCoreAPI(serverInterface, worldManagerUtil, blockSafety);
    }
    */
}
