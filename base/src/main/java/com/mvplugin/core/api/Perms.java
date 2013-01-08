package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;

public interface Perms {

    Perm CMD_IMPORT = PermFactory.newPerm(MultiverseCore.class, "cmd.import").commandPermission().build();
    Perm CMD_LOAD = PermFactory.newPerm(MultiverseCore.class, "cmd.load").commandPermission().build();
}
