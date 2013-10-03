package com.mvplugin.mockbukkit.plugin;

import com.mvplugin.core.FileLocations;
import com.mvplugin.core.PluginInfo;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class MockPluginManager implements PluginManager {

    List<Plugin> plugins = new ArrayList<Plugin>();
    Set<Plugin> enabledPlugins = new HashSet<Plugin>();

    Server server;

    public MockPluginManager(Server server) {
        this.server = server;
    }

    @Override
    public void registerInterface(Class<? extends PluginLoader> aClass) throws IllegalArgumentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Plugin getPlugin(String s) {
        for (Plugin plugin : plugins) {
            if (plugin.getName().equals(s)) {
                return plugin;
            }
        }
        return null;
    }

    @Override
    public Plugin[] getPlugins() {
        return plugins.toArray(new Plugin[plugins.size()]);
    }

    @Override
    public boolean isPluginEnabled(String s) {
        for (Plugin plugin : enabledPlugins) {
            if (plugin.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return enabledPlugins.contains(plugin);
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return null;
    }

    public Plugin loadPlugin(PluginInfo pluginInfo) {
        try {
            Plugin plugin = PowerMockito.spy(pluginInfo.plugin);
            System.out.println(plugin.getClass());
            when(plugin.getServer()).thenReturn(server);
            when(plugin.getDescription()).thenReturn(new PluginDescriptionFile(pluginInfo.name, pluginInfo.version, pluginInfo.mainClass));
            when(plugin.getDataFolder()).thenReturn(new File(FileLocations.PLUGIN_DIRECTORY, plugin.getName()));
            plugin.onLoad();
            return plugin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Plugin[] loadPlugins(File file) {
        return new Plugin[0];
    }

    @Override
    public void disablePlugins() {
        for (Plugin plugin : new HashSet<Plugin>(enabledPlugins)) {
            disablePlugin(plugin);
        }
    }

    @Override
    public void clearPlugins() {
        disablePlugins();
        plugins.clear();
    }

    @Override
    public void callEvent(Event event) throws IllegalStateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerEvents(Listener listener, Plugin plugin) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin, boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        plugin.onEnable();
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (enabledPlugins.contains(plugin)) {
            plugin.onDisable();
            enabledPlugins.remove(plugin);
        }
    }

    @Override
    public Permission getPermission(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addPermission(Permission permission) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removePermission(Permission permission) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removePermission(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean b) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void recalculatePermissionDefaults(Permission permission) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void subscribeToPermission(String s, Permissible permissible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsubscribeFromPermission(String s, Permissible permissible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void subscribeToDefaultPerms(boolean b, Permissible permissible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean b, Permissible permissible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean b) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Permission> getPermissions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean useTimings() {
        return false;
    }
}
