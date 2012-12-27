package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.mvplugin.core.api.CoreConfig;
import com.mvplugin.core.api.CorePlugin;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.command.ImportCommand;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The primary Bukkit plugin implementation of Multiverse-Core.
 *
 * See {@link com.mvplugin.core.api.CorePlugin} for a more detailed external api javadocs.
 */
public class MultiverseCorePlugin extends AbstractBukkitPlugin implements CorePlugin {

    private static final int PROTOCOL = 19;
    private static final String COMMAND_PREFIX = "mv";
    private static final String PERMISSION_PREFIX = "multiverse";

    @NotNull
    private DefaultMultiverseCore core;

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
        prepareCore();
    }

    @Override
    protected void onReloadConfig() {
        prepareCore();
    }

    private void prepareCore() {
        this.core = new DefaultMultiverseCore(this, new BukkitWorldFactory(this));
        this.core.initialize();
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
        return this.core;
    }

    @Override
    public int getProtocolVersion() {
        return PROTOCOL;
    }
}
