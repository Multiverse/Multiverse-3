package com.mvplugin.core;

import com.google.inject.Inject;
import com.mvplugin.core.WorldProperties.ConnectedWorld;
import com.mvplugin.core.WorldProperties.EntryFee;
import com.mvplugin.core.WorldProperties.Spawning;
import com.mvplugin.core.destination.DestinationRegistry;
import com.mvplugin.core.minecraft.CreatureSpawnCause;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.CoreConfig;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import pluginbase.config.SerializationRegistrar;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.sponge.SpongePluginAgent;

import java.io.File;
import java.util.concurrent.Callable;

@Plugin(id = "Multiverse-Core", name = "Multiverse-Core")
public class MultiverseCoreSpongePlugin implements MultiverseCore {

    static {
        SerializationRegistrar.registerClass(CoreConfig.class);
        SerializationRegistrar.registerClass(SpongeCoreConfig.class);
        SerializationRegistrar.registerClass(WorldProperties.class);
        SerializationRegistrar.registerClass(EntryFee.class);
        SerializationRegistrar.registerClass(Spawning.class);
        SerializationRegistrar.registerClass(SpawnException.class);
        SerializationRegistrar.registerClass(ConnectedWorld.class);
        SerializationRegistrar.registerClass(CreatureSpawnCause.class);
        SerializationRegistrar.registerClass(EntityType.class);
    }

    private static final int PROTOCOL_VERSION = 19;
    private static final String COMMAND_PREFIX = "mv";

    private MultiverseCoreAPI api;

    private SpongePluginAgent<MultiverseCore> pluginAgent;

    @Inject
    private PluginContainer pluginContainer;
    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;
    @Inject
    private Game game;

    @Listener
    private void preInitialization(GamePreInitializationEvent event) {
        registerMinecraftJunk(game);
        SpongeConvert.initializeWithGame(game);

        pluginAgent = SpongePluginAgent.getPluginAgent(game, MultiverseCore.class, this, pluginContainer, COMMAND_PREFIX, dataFolder);

    }

    private void registerMinecraftJunk(@NotNull Game game) {
        for (org.spongepowered.api.entity.EntityType entityType : game.getRegistry().getAllOf(org.spongepowered.api.entity.EntityType.class)) {
            EntityType.registerEntityType(entityType.getId());
        }
        CreatureSpawnCause.specifyNaturalCause("NATURAL");
        CreatureSpawnCause.registerSpawnCause("OTHER");
        PortalType.registerPortalType("NETHER");
        PortalType.registerPortalType("ENDER");
        PortalType.registerPortalType("CUSTOM");
    }

    @Listener
    private void initialization(GameInitializationEvent event) {
        new MultiverseCoreInitializer(pluginAgent);

        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new SpongeCoreConfig();
            }
        });

        // Register language stuff
        pluginAgent.registerMessages(SpongeLanguage.class);

        pluginAgent.loadPluginBase();
        pluginAgent.enablePluginBase();

        prepareAPI();

        /*
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        */
    }

    public File getDataFolder() {
        return dataFolder;
    }

    private void prepareAPI() {
        this.api = new DefaultMultiverseCoreAPI(this,
                getServerInterface(),
                new SpongeWorldManagerUtil(getServerInterface(), getDataFolder()),
                new SpongeBlockSafety(game));
    }

    public void reloadConfig() {
        getPluginBase().reloadConfig();
        prepareAPI();
    }

    private PluginBase getPluginBase() {
        return pluginAgent.getPluginBase();
    }

    @Listener
    private void serverStopping(GameStoppingServerEvent event) {
        pluginAgent.disablePluginBase();
    }

    @NotNull
    @Override
    public WorldManager getWorldManager() {
        return this.api.getWorldManager();
    }

    @NotNull
    @Override
    public ServerInterface getServerInterface() {
        return getPluginBase().getServerInterface();
    }

    @NotNull
    @Override
    public Messager getMessager() {
        return getPluginBase().getMessager();
    }

    @NotNull
    @Override
    public PluginLogger getLog() {
        return getPluginBase().getLog();
    }

    @NotNull
    @Override
    public MultiverseCore getMultiverseCore() {
        return this;
    }

    @Override
    public int getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    @Override
    @NotNull
    public SpongeCoreConfig getMVConfig() {
        return (SpongeCoreConfig) getPluginBase().getSettings();
    }

    @Override
    @NotNull
    public EventProcessor getEventProcessor() {
        return this.api.getEventProcessor();
    }

    @NotNull
    @Override
    public SafeTeleporter getSafeTeleporter() {
        return this.api.getSafeTeleporter();
    }

    @NotNull
    @Override
    public BlockSafety getBlockSafety() {
        return this.api.getBlockSafety();
    }

    @NotNull
    @Override
    public DestinationRegistry getDestinationRegistry() {
        return this.api.getDestinationRegistry();
    }

    @NotNull
    @Override
    public PlayerTracker getPlayerTracker() {
        return api.getPlayerTracker();
    }
}
