package com.mvplugin.core;

import org.bukkit.plugin.Plugin;

public class PluginInfo {

    public String name;
    public String version;
    public String mainClass;
    public Plugin plugin;

    public PluginInfo(String name, String version, String mainClass, Plugin plugin) {
        this.name = name;
        this.version = version;
        this.mainClass = mainClass;
        this.plugin = plugin;
    }
}
