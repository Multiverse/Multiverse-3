package com.mvplugin.core;

import org.powermock.api.mockito.PowerMockito;
import pluginbase.plugin.ServerInterface;

import static org.mockito.Mockito.*;

public class ServerInterfaceMocker {
    public static ServerInterface getMockedServerInterface() {
        ServerInterface serverInterface = PowerMockito.mock(ServerInterface.class);

        when(serverInterface.getServerFolder()).thenReturn(FileLocations.SERVER_DIRECTORY);
        when(serverInterface.getWorldContainer()).thenReturn(FileLocations.SERVER_DIRECTORY);

        return serverInterface;
    }
}
