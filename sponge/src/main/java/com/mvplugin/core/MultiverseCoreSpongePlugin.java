package com.mvplugin.core;

import com.google.inject.Inject;
import com.mvplugin.core.WorldProperties.ConnectedWorld;
import com.mvplugin.core.WorldProperties.EntryFee;
import com.mvplugin.core.WorldProperties.Spawning;
import com.mvplugin.core.command.CreateCommand;
import com.mvplugin.core.command.DeleteCommand;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.command.ListCommand;
import com.mvplugin.core.command.LoadCommand;
import com.mvplugin.core.command.ModifyAddCommand;
import com.mvplugin.core.command.ModifyClearCommand;
import com.mvplugin.core.command.ModifyRemoveCommand;
import com.mvplugin.core.command.ModifySetCommand;
import com.mvplugin.core.command.TeleportCommand;
import com.mvplugin.core.command.UnloadCommand;
import com.mvplugin.core.destination.DestinationRegistry;
import com.mvplugin.core.minecraft.CreatureSpawnCause;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.CoreConfig;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.PropertyDescriptions;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.util.event.Subscribe;
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
    private static final String PERMISSION_PREFIX = "multiverse";

    private MultiverseCoreAPI api;

    private SpongePluginAgent<MultiverseCore> pluginAgent;

    @Inject
    private PluginContainer pluginContainer;
    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;
    @Inject
    private Game game;

    @Subscribe
    private void preInitialization(PreInitializationEvent event) {
        registerMinecraftJunk(game);
        SpongeConvert.initializeWithGame(game);

        pluginAgent = SpongePluginAgent.getPluginAgent(game, MultiverseCore.class, this, pluginContainer, COMMAND_PREFIX, dataFolder);
        pluginAgent.setPermissionPrefix(PERMISSION_PREFIX);
        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new SpongeCoreConfig();
            }
        });

        // Register language stuff
        pluginAgent.registerMessages(PropertyDescriptions.class);
        pluginAgent.registerMessages(Language.class);
        pluginAgent.registerMessages(SpongeLanguage.class);
    }

    private void registerMinecraftJunk(@NotNull Game game) {
        for (org.spongepowered.api.entity.EntityType entityType : game.getRegistry().getEntities()) {
            EntityType.registerEntityType(entityType.getId());
        }
        CreatureSpawnCause.specifyNaturalCause("NATURAL");
        CreatureSpawnCause.registerSpawnCause("OTHER");
        PortalType.registerPortalType("NETHER");
        PortalType.registerPortalType("ENDER");
        PortalType.registerPortalType("CUSTOM");
    }

    @Subscribe
    private void initialization(InitializationEvent event) {
        // Register commands
        pluginAgent.registerCommand(ImportCommand.class);
        pluginAgent.registerCommand(LoadCommand.class);
        pluginAgent.registerCommand(UnloadCommand.class);
        pluginAgent.registerCommand(ListCommand.class);
        pluginAgent.registerCommand(DeleteCommand.class);
        pluginAgent.registerCommand(CreateCommand.class);
        pluginAgent.registerCommand(TeleportCommand.class);
        pluginAgent.registerCommand(ModifySetCommand.class);
        pluginAgent.registerCommand(ModifyAddCommand.class);
        pluginAgent.registerCommand(ModifyRemoveCommand.class);
        pluginAgent.registerCommand(ModifyClearCommand.class);

        pluginAgent.loadPluginBase();
        pluginAgent.enablePluginBase();

        prepareAPI();

        /*
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        */
    }

    File getDataFolder() {
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

    @Subscribe
    private void serverStopping(ServerStoppingEvent event) {
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
