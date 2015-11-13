package com.mvplugin.core;

import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;
import pluginbase.plugin.VersionInfoModifier;

import java.util.ArrayList;
import java.util.List;

class VersionInfo implements VersionInfoModifier {

    @NotNull
    private final MultiverseCore core;

    public VersionInfo(@NotNull MultiverseCore core) {
        this.core = core;
    }

    @NotNull
    @Override
    public List<String> modifyVersionInfo(@NotNull List<String> versionInfo) {
        versionInfo = new ArrayList<>();
        versionInfo.add("Multiverse-Core Version: " + core.getVersion());
        versionInfo.add("Multiverse-Core Protocol: " + core.getProtocolVersion());
        versionInfo.add("Server Name: " + core.getServerInterface().getName());
        versionInfo.add("Server Version: " + core.getServerInterface().getVersion());
        versionInfo.add("===== Multiverse-Core Config Dump =====");
        for (String property : core.getMVConfig().getAllPropertyNames()) {
            try {
                versionInfo.add("  " + property + " = " + core.getMVConfig().getProperty(property));
            } catch (NoSuchFieldException ignore) { }
        }
        versionInfo.add("===== Multiverse-Core World Dump =====");
        for (MultiverseWorld world : core.getWorldManager().getWorlds()) {
            versionInfo.add("--- World: " + world.getName() + " ---");
            for (String property : world.getProperties().getAllPropertyNames()) {
                try {
                    versionInfo.add("  " + property + " = " + world.getProperties().getProperty(property));
                } catch (NoSuchFieldException ignore) { }
            }
        }

        return versionInfo;
    }
}
