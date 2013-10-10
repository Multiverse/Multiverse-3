package com.mvplugin.core;

import com.mvplugin.core.WorldProperties.EntryFee;
import com.mvplugin.core.WorldProperties.Spawning;
import com.mvplugin.core.WorldProperties.Spawning.AllowedSpawns;
import com.mvplugin.core.command.CreateCommand;
import com.mvplugin.core.command.DeleteCommand;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.command.ListCommand;
import com.mvplugin.core.command.LoadCommand;
import com.mvplugin.core.command.ModifySetCommand;
import com.mvplugin.core.command.TeleportCommand;
import com.mvplugin.core.command.UnloadCommand;
import com.mvplugin.core.destination.DestinationRegistry;
import com.mvplugin.core.listeners.BukkitWorldListener;
import com.mvplugin.core.minecraft.CreatureSpawnCause;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.CoreConfig;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.PropertyDescriptions;
import com.mvplugin.core.util.SafeTeleporter;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.AbstractBukkitPlugin;
import pluginbase.config.SerializationRegistrar;
import pluginbase.messages.Messages;

/**
 * The primary Bukkit plugin implementation of Multiverse-Core.
 *
 * See {@link com.mvplugin.core.plugin.MultiverseCore} for a more detailed external api javadocs.
 */
public class MultiverseCoreBukkitPlugin extends AbstractBukkitPlugin implements MultiverseCore {

    static {
        SerializationRegistrar.registerClass(CoreConfig.class);
        SerializationRegistrar.registerClass(BukkitCoreConfig.class);
        SerializationRegistrar.registerClass(WorldProperties.class);
        SerializationRegistrar.registerClass(EntryFee.class);
        SerializationRegistrar.registerClass(Spawning.class);
        SerializationRegistrar.registerClass(AllowedSpawns.class);
        SerializationRegistrar.registerClass(SpawnException.class);
        SerializationRegistrar.registerClass(CreatureSpawnCause.class);
        SerializationRegistrar.registerClass(EntityType.class);
    }

    static {
        CreatureSpawnCause.specifyNaturalCause(SpawnReason.NATURAL.name());
        for (SpawnReason spawnReason : SpawnReason.values()) {
            CreatureSpawnCause.registerSpawnCause(spawnReason.name());
        }
        for (org.bukkit.entity.EntityType entityType : org.bukkit.entity.EntityType.values()) {
            EntityType.registerEntityType(entityType.name());
        }
    }

    private static final int PROTOCOL = 19;
    private static final String COMMAND_PREFIX = "mv";
    private static final String PERMISSION_PREFIX = "multiverse";

    private MultiverseCoreAPI api;

    @NotNull
    @Override
    public String getPermissionName() {
        return PERMISSION_PREFIX;
    }

    @Override
    protected void onPluginLoad() {

    }

    @Override
    protected void registerMessages() {
        Messages.registerMessages(this, PropertyDescriptions.class);
        Messages.registerMessages(this, Language.class);
        Messages.registerMessages(this, BukkitLanguage.class);
    }

    @Override
    public void onPluginEnable() {
        prepareAPI();
        getServer().getPluginManager().registerEvents(new BukkitWorldListener(this), this);
    }

    @Override
    protected void onReloadConfig() {
        prepareAPI();
    }

    private void prepareAPI() {
        this.api = new DefaultMultiverseCoreAPI(getServerInterface(),
                new BukkitWorldManagerUtil(getServerInterface(), getDataFolder()), new BukkitBlockSafety());
    }

    @Override
    protected void registerCommands() {
        registerCommand(ImportCommand.class);
        registerCommand(LoadCommand.class);
        registerCommand(UnloadCommand.class);
        registerCommand(ListCommand.class);
        registerCommand(DeleteCommand.class);
        registerCommand(CreateCommand.class);
        registerCommand(TeleportCommand.class);
        registerCommand(ModifySetCommand.class);
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }

    @NotNull
    @Override
    public WorldManager getWorldManager() {
        return this.api.getWorldManager();
    }

    @NotNull
    @Override
    protected BukkitCoreConfig getDefaultSettings() {
        return new BukkitCoreConfig();
    }

    @NotNull
    @Override
    public BukkitCoreConfig getSettings() {
        return (BukkitCoreConfig) super.getSettings();
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }



    @NotNull
    @Override
    public MultiverseCore getMultiverseCore() {
        return this;
    }

    @Override
    public int getProtocolVersion() {
        return PROTOCOL;
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
}
