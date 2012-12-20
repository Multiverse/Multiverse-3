package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.mvplugin.core.api.CoreConfig;
import com.mvplugin.core.api.EventProcessor;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The primary Bukkit plugin implementation of Multiverse-Core.
 *
 * See {@link MultiverseCore} for a more detailed external api javadocs.
 */
public class MultiverseCorePlugin extends AbstractBukkitPlugin implements MultiverseCore {

    private static final int PROTOCOL = 19;
    private static final String COMMAND_PREFIX = "mv";
    private static final String PERMISSION_PREFIX = "multiverse";

    private BukkitWorldManager worldManager;

    private final EventProcessor eventProcessor = new DefaultEventProcessor(this);

    @NotNull
    @Override
    public String getPermissionName() {
        return PERMISSION_PREFIX;
    }

    @Override
    protected void onPluginLoad() {
        PropertyDescriptions.init();
    }

    @Override
    public void onPluginEnable() {
        worldManager = new BukkitWorldManager(this);
    }

    @Override
    protected void onReloadConfig() {
        worldManager = new BukkitWorldManager(this);
    }

    @Override
    protected void registerCommands() {
        registerCommand(ImportCommand.class);
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }

    @NotNull
    @Override
    protected Properties getNewConfig() throws IOException {
        return new YamlCoreConfig(this);
    }

    @Override
    public CoreConfig config() {
        return (CoreConfig) super.config();
    }

    @Override
    public CoreConfig getMVConfig() {
        return config();
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }

    @Override
    public BukkitWorldManager getWorldManager() {
        return this.worldManager;
    }

    @Override
    public MultiverseCore getCore() {
        return this;
    }

    @Override
    public void setCore(@NotNull final MultiverseCore core) { }

    @Override
    public int getProtocolVersion() {
        return PROTOCOL;
    }

    @Override
    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }
}
