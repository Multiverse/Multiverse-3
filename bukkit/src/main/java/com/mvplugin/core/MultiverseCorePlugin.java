package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.mvplugin.core.api.BlockSafety;
import com.mvplugin.core.api.CoreConfig;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.SafeTeleporter;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.command.LoadCommand;
import com.mvplugin.core.command.UnloadCommand;
import com.mvplugin.core.util.BukkitLanguage;
import com.mvplugin.core.util.Language;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The primary Bukkit plugin implementation of Multiverse-Core.
 *
 * See {@link com.mvplugin.core.api.MultiverseCore} for a more detailed external api javadocs.
 */
public class MultiverseCorePlugin extends AbstractBukkitPlugin implements MultiverseCore {

    private static final int PROTOCOL = 19;
    private static final String COMMAND_PREFIX = "mv";
    private static final String PERMISSION_PREFIX = "multiverse";

    private final EventProcessor eventProcessor = new EventProcessor(this);
    private final SafeTeleporter safeTeleporter = new DefaultSafeTeleporter(this);
    private final BlockSafety blockSafety = new BukkitBlockSafety();

    private BukkitWorldManager worldManager;

    @NotNull
    @Override
    public String getPermissionName() {
        return PERMISSION_PREFIX;
    }

    @Override
    protected void onPluginLoad() {
        PropertyDescriptions.init();
        Language.class.getName();
        BukkitLanguage.class.getName();
    }

    @Override
    public void onPluginEnable() {
        prepareWorldManager();
    }

    @Override
    protected void onReloadConfig() {
        prepareWorldManager();
    }

    private void prepareWorldManager() {
        this.worldManager = new BukkitWorldManager(this);
    }

    @Override
    protected void registerCommands() {
        registerCommand(ImportCommand.class);
        registerCommand(LoadCommand.class);
        registerCommand(UnloadCommand.class);
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }

    @NotNull
    @Override
    public BukkitWorldManager getWorldManager() {
        return this.worldManager;
    }

    @NotNull
    @Override
    protected Properties getNewConfig() throws IOException {
        return new YamlCoreConfig(this);
    }

    @NotNull
    @Override
    public CoreConfig config() {
        return (CoreConfig) super.config();
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
    public CoreConfig getMVConfig() {
        return config();
    }

    @Override
    @NotNull
    public EventProcessor getEventProcessor() {
        return this.eventProcessor;
    }

    @NotNull
    @Override
    public SafeTeleporter getSafeTeleporter() {
        return safeTeleporter;
    }

    @NotNull
    @Override
    public BlockSafety getBlockSafety() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
