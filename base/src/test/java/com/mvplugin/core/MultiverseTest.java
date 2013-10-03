package com.mvplugin.core;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WorldManager.class, MultiverseWorld.class})
public abstract class MultiverseTest {

    @Before
    public void setupDirectories() throws Exception {
        FileLocations.setupDirectories();
    }

    @After
    public void cleanupDirectories() throws Exception {
        FileLocations.cleanupDirectories();
    }
}
