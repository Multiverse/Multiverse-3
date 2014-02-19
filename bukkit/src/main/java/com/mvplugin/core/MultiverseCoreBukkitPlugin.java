package com.mvplugin.core;

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
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.CoreConfig;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.PropertyDescriptions;
import com.mvplugin.core.util.SafeTeleporter;
import org.bukkit.PortalType;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.BukkitPluginAgent;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.QueuedCommand;
import pluginbase.config.SerializationRegistrar;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.Messager;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;

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
    private static final String PERMISSION_PREFIX = "multiverse";

    private MultiverseCoreAPI api;

    private final BukkitPluginAgent<MultiverseCore> pluginAgent = BukkitPluginAgent.getPluginAgent(MultiverseCore.class, this, COMMAND_PREFIX);

    public MultiverseCoreBukkitPlugin() {
        pluginAgent.setPermissionPrefix(PERMISSION_PREFIX);
        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new BukkitCoreConfig();
            }
        });

        // Register language stuff
        pluginAgent.registerMessage(PropertyDescriptions.class);
        pluginAgent.registerMessage(Language.class);
        pluginAgent.registerMessage(BukkitLanguage.class);

        // Register commands
        pluginAgent.registerCommand(ImportCommand.class);
        pluginAgent.registerCommand(LoadCommand.class);
        pluginAgent.registerCommand(UnloadCommand.class);
        pluginAgent.registerCommand(ListCommand.class);
        pluginAgent.registerQueuedCommand(DeleteCommand.class);
        pluginAgent.registerCommand(CreateCommand.class);
        pluginAgent.registerCommand(TeleportCommand.class);
        pluginAgent.registerCommand(ModifySetCommand.class);
        pluginAgent.registerCommand(ModifyAddCommand.class);
        pluginAgent.registerCommand(ModifyRemoveCommand.class);
        pluginAgent.registerCommand(ModifyClearCommand.class);
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
    public BukkitCoreConfig getSettings() {
        return (BukkitCoreConfig) getPluginBase().getSettings();
    }

    @NotNull
    @Override
    public ServerInterface getServerInterface() {
        return getPluginBase().getServerInterface();
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return getPluginBase().getCommandPrefix();
    }

    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return getPluginBase().getCommandHandler();
    }

    @Override
    public void scheduleQueuedCommandExpiration(@NotNull final QueuedCommand queuedCommand) {
        getPluginBase().scheduleQueuedCommandExpiration(queuedCommand);
    }

    @Override
    public boolean useQueuedCommands() {
        return getPluginBase().useQueuedCommands();
    }

    @NotNull
    @Override
    public String[] getAdditionalCommandAliases(final Class<? extends Command> commandClass) {
        return getPluginBase().getAdditionalCommandAliases(commandClass);
    }

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
        return getSettings();
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
