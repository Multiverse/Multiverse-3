package com.mvplugin.core.util;

import com.mvplugin.core.plugin.MultiverseCore;
import pluginbase.permission.Perm;
import pluginbase.permission.PermDefault;
import pluginbase.permission.PermFactory;

public class Perms {

    // ============ Multiverse Command Permissions ============

    public static final Perm CMD_CREATE = PermFactory.newPerm(MultiverseCore.class, "cmd.create")
            .commandPermission().usePluginName().build();

    public static final Perm CMD_IMPORT = PermFactory.newPerm(MultiverseCore.class, "cmd.import")
            .commandPermission().usePluginName().build();

    public static final Perm CMD_LOAD = PermFactory.newPerm(MultiverseCore.class, "cmd.load")
            .commandPermission().usePluginName().build();

    public static final Perm CMD_UNLOAD = PermFactory.newPerm(MultiverseCore.class, "cmd.unload")
            .commandPermission().usePluginName().build();

    public static final Perm CMD_LIST = PermFactory.newPerm(MultiverseCore.class, "cmd.list")
            .commandPermission().usePluginName().build();

    private static final Perm CMD_MODIFY_ALL = PermFactory.newPerm(MultiverseCore.class, "cmd.modify.*")
            .commandPermission().usePluginName().specificOnly().build();

    public static final Perm CMD_MODIFY = PermFactory.newPerm(MultiverseCore.class, "cmd.modify")
            .commandPermission().parent(CMD_MODIFY_ALL).usePluginName().specificOnly().build();

    public static final Perm CMD_DELETE = PermFactory.newPerm(MultiverseCore.class, "cmd.delete")
            .commandPermission().usePluginName().build();

    // ============ Multiverse Bypass Permissions ============

    private static final Perm MV = PermFactory.newPerm(MultiverseCore.class, "mv.*")
            .def(PermDefault.FALSE).build();

    private static final Perm BYPASS_ALL = PermFactory.newPerm(MultiverseCore.class, "mv.bypass.*")
            .def(PermDefault.FALSE).parent(MV).build();

    private static final Perm BYPASS_GAMEMODE_ALL = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.gamemode.*").desc("Allows a player to ignore gamemode changes for all worlds.")
            .def(PermDefault.FALSE).parent(BYPASS_ALL).build();

    private static final Perm BYPASS_PLAYERLIMIT_ALL = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.playerlimit.*").desc("Allows a player to ignore the player limit for all worlds.")
            .def(PermDefault.FALSE).parent(BYPASS_ALL).build();

    private static final Perm BYPASS_ALLOWFLY_ALL = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.allowfly.*").desc("Allows a player to ignore the flight restrictions for all worlds.")
            .def(PermDefault.FALSE).parent(BYPASS_ALL).build();

    /** Used for bypassing the gamemode of specific worlds. */
    public static final Perm BYPASS_GAMEMODE = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.gamemode").desc("Allows a player to ignore gamemode changes for a specific world.")
            .specificOnly().def(PermDefault.FALSE).parent(BYPASS_GAMEMODE_ALL).build();

    /** Used for bypassing the player limit of specific worlds. */
    public static final Perm BYPASS_PLAYERLIMIT = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.playerlimit").desc("Allows a player to ignore the player limit for a specific world.")
            .specificOnly().def(PermDefault.FALSE).parent(BYPASS_PLAYERLIMIT_ALL).build();

    /** Used for bypassing the allow fly setting of specific worlds. */
    public static final Perm BYPASS_ALLOWFLY = PermFactory.newPerm(MultiverseCore.class,
            "mv.bypass.allowfly").desc("Allows a player to ignore the flight restrictions for a specific world.")
            .specificOnly().def(PermDefault.FALSE).parent(BYPASS_ALLOWFLY_ALL).build();


    // ============ Multiverse World Permissions ============

    private static final Perm ACCESS_ALL = PermFactory.newPerm(MultiverseCore.class, "access.*")
            .desc("Allows a player to access all worlds.")
            .usePluginName().addToAll().build();

    private static final Perm COST_EXEMPT_ALL = PermFactory.newPerm(MultiverseCore.class, "exempt.*")
            .desc("Allows a player to bypass the costs for worlds and MV portals.")
            .usePluginName().addToAll().build();

    private static final Perm SEE_HIDDEN_ALL = PermFactory.newPerm(MultiverseCore.class, "see_hidden.*")
            .desc("Allows a player to see all hidden worlds.")
            .usePluginName().addToAll().build();

    public static final Perm ACCESS = PermFactory.newPerm(MultiverseCore.class, "access")
            .desc("Allows a player to access a specific world.")
            .usePluginName().specificOnly().parent(ACCESS_ALL).build();

    public static final Perm COST_EXEMPT = PermFactory.newPerm(MultiverseCore.class, "exempt")
            .desc("Allows a player to bypass the costs for a specific world and MV portals in that world.")
            .usePluginName().specificOnly().parent(COST_EXEMPT_ALL).build();

    public static final Perm SEE_HIDDEN = PermFactory.newPerm(MultiverseCore.class, "see_hidden")
            .desc("Allows a player to see a specific hidden world.")
            .usePluginName().specificOnly().parent(SEE_HIDDEN_ALL).build();


    // ============ Multiverse Teleport Permissions ============

    private static final Perm TELEPORT_ALL = PermFactory.newPerm(MultiverseCore.class, "teleport.*")
            .desc("Allows the player to use all forms of teleportation.")
            .usePluginName().addToAll().build();

    private static final Perm TELEPORT_SELF_ALL = PermFactory.newPerm(MultiverseCore.class, "teleport.self.*")
            .desc("Allows the player to use all forms of self teleportation.")
            .usePluginName().parent(TELEPORT_ALL).build();

    private static final Perm TELEPORT_OTHER_ALL = PermFactory.newPerm(MultiverseCore.class, "teleport.other.*")
            .desc("Allows the player to use all forms of teleportation on other players.")
            .usePluginName().parent(TELEPORT_ALL).build();

    private static final Perm TELEPORT_SELF_WORLD = PermFactory.newPerm(MultiverseCore.class, "teleport.self.w")
            .desc("Allows the player to teleport self to any world.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_WORLD = PermFactory.newPerm(MultiverseCore.class, "teleport.self.w")
            .desc("Allows the player to teleport self to a specific world.")
            .usePluginName().specificOnly().parent(TELEPORT_SELF_WORLD).build();

    private static final Perm TELEPORT_SELF_ANCHOR = PermFactory.newPerm(MultiverseCore.class, "teleport.self.a")
            .desc("Allows the player to teleport self to any anchor.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_ANCHOR = PermFactory.newPerm(MultiverseCore.class, "teleport.self.a")
            .desc("Allows the player to teleport self to a specific anchor.")
            .usePluginName().specificOnly().parent(TELEPORT_SELF_ANCHOR).build();

    public static final Perm TP_SELF_EXACT = PermFactory.newPerm(MultiverseCore.class, "teleport.self.e")
            .desc("Allows the player to teleport self to an exact location.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    private static final Perm TELEPORT_SELF_PLAYER = PermFactory.newPerm(MultiverseCore.class, "teleport.self.pl")
            .desc("Allows the player to teleport self to any player.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_PLAYER = PermFactory.newPerm(MultiverseCore.class, "teleport.self.pl")
            .desc("Allows the player to teleport self to a specific player.")
            .usePluginName().specificOnly().parent(TELEPORT_SELF_PLAYER).build();

    public static final Perm TP_SELF_CANNON = PermFactory.newPerm(MultiverseCore.class, "teleport.self.ca")
            .desc("Allows the player to teleport self to cannon.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_BED = PermFactory.newPerm(MultiverseCore.class, "teleport.self.b")
            .desc("Allows the player to teleport self to their bed.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    private static final Perm TELEPORT_SELF_PORTAL = PermFactory.newPerm(MultiverseCore.class, "teleport.self.p")
            .desc("Allows the player to teleport self to any MV portal.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_PORTAL = PermFactory.newPerm(MultiverseCore.class, "teleport.self.p")
            .desc("Allows the player to teleport self to a specific MV portal.")
            .usePluginName().specificOnly().parent(TELEPORT_SELF_PORTAL).build();

    private static final Perm TELEPORT_SELF_OPENWARP = PermFactory.newPerm(MultiverseCore.class, "teleport.self.ow")
            .desc("Allows the player to teleport self to any OpenWarp destination.")
            .usePluginName().parent(TELEPORT_SELF_ALL).build();

    public static final Perm TP_SELF_OPENWARP = PermFactory.newPerm(MultiverseCore.class, "teleport.self.ow")
            .desc("Allows the player to teleport self to a specific OpenWarp destination.")
            .usePluginName().specificOnly().parent(TELEPORT_SELF_OPENWARP).build();

    private static final Perm TELEPORT_OTHER_WORLD = PermFactory.newPerm(MultiverseCore.class, "teleport.other.w")
            .desc("Allows the player to teleport other to any world.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_WORLD = PermFactory.newPerm(MultiverseCore.class, "teleport.other.w")
            .desc("Allows the player to teleport other to a specific world.")
            .usePluginName().specificOnly().parent(TELEPORT_OTHER_WORLD).build();

    private static final Perm TELEPORT_OTHER_ANCHOR = PermFactory.newPerm(MultiverseCore.class, "teleport.other.a")
            .desc("Allows the player to teleport other to any anchor.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_ANCHOR = PermFactory.newPerm(MultiverseCore.class, "teleport.other.a")
            .desc("Allows the player to teleport other to a specific anchor.")
            .usePluginName().specificOnly().parent(TELEPORT_OTHER_ANCHOR).build();

    public static final Perm TP_OTHER_EXACT = PermFactory.newPerm(MultiverseCore.class, "teleport.other.e")
            .desc("Allows the player to teleport other to an exact location.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    private static final Perm TELEPORT_OTHER_PLAYER = PermFactory.newPerm(MultiverseCore.class, "teleport.other.pl")
            .desc("Allows the player to teleport other to any player.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_PLAYER = PermFactory.newPerm(MultiverseCore.class, "teleport.other.pl")
            .desc("Allows the player to teleport other to a specific player.")
            .usePluginName().specificOnly().parent(TELEPORT_OTHER_PLAYER).build();

    public static final Perm TP_OTHER_CANNON = PermFactory.newPerm(MultiverseCore.class, "teleport.other.ca")
            .desc("Allows the player to teleport other to cannon.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_BED = PermFactory.newPerm(MultiverseCore.class, "teleport.other.b")
            .desc("Allows the player to teleport other to their bed.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    private static final Perm TELEPORT_OTHER_PORTAL = PermFactory.newPerm(MultiverseCore.class, "teleport.other.p")
            .desc("Allows the player to teleport other to any MV portal.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_PORTAL = PermFactory.newPerm(MultiverseCore.class, "teleport.other.p")
            .desc("Allows the player to teleport other to a specific MV portal.")
            .usePluginName().specificOnly().parent(TELEPORT_OTHER_PORTAL).build();

    private static final Perm TELEPORT_OTHER_OPENWARP = PermFactory.newPerm(MultiverseCore.class, "teleport.other.ow")
            .desc("Allows the player to teleport other to any OpenWarp destination.")
            .usePluginName().parent(TELEPORT_OTHER_ALL).build();

    public static final Perm TP_OTHER_OPENWARP = PermFactory.newPerm(MultiverseCore.class, "teleport.other.ow")
            .desc("Allows the player to teleport other to a specific OpenWarp destination.")
            .usePluginName().specificOnly().parent(TELEPORT_OTHER_OPENWARP).build();
}
