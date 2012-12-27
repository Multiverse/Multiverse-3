package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.mvplugin.core.api.CoreConfig;
import com.mvplugin.core.api.EventProcessor;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.WorldManager;
import com.mvplugin.core.command.ImportCommand;
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

    private final EventProcessor eventProcessor = new DefaultEventProcessor(this);

    private WorldManager worldManager;

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
        prepareWorldManager();
    }

    @Override
    protected void onReloadConfig() {
        prepareWorldManager();
    }

    private void prepareWorldManager() {
        this.worldManager = new BukkitWorldManager(this, new BukkitWorldFactory(this));
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
    public BukkitWorldManager getWorldManager() {
        if (!(this.worldManager instanceof BukkitWorldManager)) {
            throw new IllegalStateException("World manager wasn't a bukkit world manager!");
        }
        return (BukkitWorldManager) this.worldManager;
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
}
