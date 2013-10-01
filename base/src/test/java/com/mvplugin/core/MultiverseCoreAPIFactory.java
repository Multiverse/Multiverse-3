package com.mvplugin.core;

import org.powermock.api.mockito.PowerMockito;

public class MultiverseCoreAPIFactory {

    public static MultiverseCoreAPI getMockedMultiverseCoreAPI() {
        return PowerMockito.mock(MultiverseCoreAPI.class);
    }
}
