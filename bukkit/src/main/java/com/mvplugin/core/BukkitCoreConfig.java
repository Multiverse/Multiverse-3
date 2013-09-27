package com.mvplugin.core;

import com.mvplugin.core.util.CoreConfig;
import pluginbase.config.SerializationRegistrar;

class BukkitCoreConfig extends CoreConfig {

    static {
        SerializationRegistrar.registerClass(BukkitCoreConfig.class);
    }
}
