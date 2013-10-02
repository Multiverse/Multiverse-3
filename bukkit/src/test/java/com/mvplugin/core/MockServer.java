package com.mvplugin.core;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockServer {

    private Server server = PowerMockito.mock(Server.class);
    private List<World> worlds = new ArrayList<World>();

    private MockServer() {
        when(server.getWorlds()).thenReturn(worlds);
        prepareMethodCreateWorld();
        worlds.add(MockWorld.createSimpleMockedWorld("world", Environment.NORMAL));
        worlds.add(MockWorld.createSimpleMockedWorld("world_nether", Environment.NETHER));
        worlds.add(MockWorld.createSimpleMockedWorld("world_the_end", Environment.THE_END));
    }

    private void prepareMethodCreateWorld() {
        when(server.createWorld(any(WorldCreator.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WorldCreator creator = (WorldCreator) invocation.getArguments()[0];
                World world = MockWorld.createMockedWorld(creator);
                worlds.add(world);
                return world;
            }
        });
    }

    public Server getMock() {
        return server;
    }

    public static Server getMockedServer() {
        MockServer mockServer = new MockServer();
        return mockServer.getMock();
    }

    private static class MockWorld {

        private World world = PowerMockito.mock(World.class);
        private Difficulty difficulty = Difficulty.EASY;
        private Location spawn = new Location(world, 0, 0, 0);

        private MockWorld(WorldCreator creator) {
            when(world.getName()).thenReturn(creator.name());
            when(world.getSeed()).thenReturn(creator.seed());
            when(world.getWorldType()).thenReturn(creator.type());
            when(world.getEnvironment()).thenReturn(creator.environment());
            when(world.getDifficulty()).thenReturn(difficulty);
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    difficulty = (Difficulty) invocation.getArguments()[0];
                    return null;
                }
            }).when(world).setDifficulty(any(Difficulty.class));
            when(world.getSpawnLocation()).thenReturn(spawn);
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    spawn = new Location(
                            world,
                            (Integer) invocation.getArguments()[0],
                            (Integer) invocation.getArguments()[1],
                            (Integer) invocation.getArguments()[2]
                    );
                    return null;
                }
            }).when(world).setSpawnLocation(anyInt(), anyInt(), anyInt());
        }

        public World getMock() {
            return world;
        }

        public static World createMockedWorld(WorldCreator creator) {
            return new MockWorld(creator).getMock();
        }

        public static World createSimpleMockedWorld(String name, Environment environment) {
            return createMockedWorld(new WorldCreator(name).environment(environment));
        }
    }
}
