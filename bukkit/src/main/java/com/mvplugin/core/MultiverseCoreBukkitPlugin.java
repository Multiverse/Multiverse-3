package com.mvplugin.core;

import com.mvplugin.core.WorldProperties.ConnectedWorld;
import com.mvplugin.core.WorldProperties.EntryFee;
import com.mvplugin.core.WorldProperties.Spawning;
import com.mvplugin.core.destination.DestinationRegistry;
import com.mvplugin.core.minecraft.CreatureSpawnCause;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.CoreConfig;
import com.mvplugin.core.util.SafeTeleporter;
import org.bukkit.PortalType;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.BukkitPluginAgent;
import pluginbase.config.SerializationRegistrar;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.Messager;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * The primary Bukkit plugin implementation of Multiverse-Core.
 *
 * See {@link com.mvplugin.core.plugin.MultiverseCore} for a more detailed external api javadocs.
 */
public class MultiverseCoreBukkitPlugin extends JavaPlugin implements MultiverseCore {

    static {
        CreatureSpawnCause.specifyNaturalCause(SpawnReason.NATURAL.name());
        for (SpawnReason spawnReason : SpawnReason.values()) {
            CreatureSpawnCause.registerSpawnCause(spawnReason.name());
        }
        for (org.bukkit.entity.EntityType entityType : org.bukkit.entity.EntityType.values()) {
            EntityType.registerEntityType(entityType.name());
        }
        for (PortalType portalType : PortalType.values()) {
            com.mvplugin.core.minecraft.PortalType.registerPortalType(portalType.name());
        }
    }

    static {
        SerializationRegistrar.registerClass(CoreConfig.class);
        SerializationRegistrar.registerClass(BukkitCoreConfig.class);
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

    private final BukkitPluginAgent<MultiverseCore> pluginAgent = BukkitPluginAgent.getPluginAgent(MultiverseCore.class, this, COMMAND_PREFIX);

    public MultiverseCoreBukkitPlugin() {
        super();
        init();
    }

    MultiverseCoreBukkitPlugin(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, server, description, dataFolder, file);
        init();
    }

    private void init() {
        new MultiverseCoreInitializer(pluginAgent);

        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new BukkitCoreConfig();
            }
        });

        // Register language stuff
        pluginAgent.registerMessages(BukkitLanguage.class);
    }

    private PluginBase<MultiverseCore> getPluginBase() {
        return pluginAgent.getPluginBase();
    }

    @Override
    public void onLoad() {
        pluginAgent.loadPluginBase();
    }

    @Override
    public void onEnable() {
        pluginAgent.enablePluginBase();
        prepareAPI();
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        try {
            pluginAgent.enableMetrics();
        } catch (IOException e) {
            new PluginBaseException(e).logException(getPluginBase().getLog(), Level.WARNING);
        }
    }

    @Override
    public void onDisable() {
        try {
            pluginAgent.disableMetrics();
        } catch (IOException e) {
            new PluginBaseException(e).logException(getPluginBase().getLog(), Level.WARNING);
        }
        pluginAgent.disablePluginBase();
    }

    @Override
    public void reloadConfig() {
        getPluginBase().reloadConfig();
        prepareAPI();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return pluginAgent.callCommand(sender, command, label, args);
    }

    private void prepareAPI() {
        this.api = new DefaultMultiverseCoreAPI(this,
                getServerInterface(),
                new BukkitWorldManagerUtil(getServerInterface(), getDataFolder()),
                new BukkitBlockSafety());
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
    public BukkitCoreConfig getMVConfig() {
        return (BukkitCoreConfig) getPluginBase().getSettings();
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
