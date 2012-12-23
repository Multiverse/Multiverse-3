package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;

public interface Perms {

    Perm CMD_IMPORT = PermFactory.newPerm(CorePlugin.class, "cmd.import").commandPermission().build();
}
