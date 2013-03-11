package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.mvplugin.core.minecraft.WorldEnvironment;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;

public final class BukkitConvert {

    private BukkitConvert() {
        throw new AssertionError();
    }

    public static Difficulty toBukkit(@NotNull final com.mvplugin.core.minecraft.Difficulty d) {
        return Difficulty.valueOf(d.name());
    }
    
    public static WorldType toBukkit(@NotNull final com.mvplugin.core.minecraft.WorldType t) {
        return WorldType.valueOf(t.name());
    }

    public static com.mvplugin.core.minecraft.WorldType fromBukkit(@NotNull final WorldType t) {
        return com.mvplugin.core.minecraft.WorldType.valueOf(t.name());
    }

    public static Environment toBukkit(@NotNull final WorldEnvironment e) {
        return Environment.valueOf(e.name());
    }

    public static WorldEnvironment fromBukkit(@NotNull final Environment e) {
        return WorldEnvironment.valueOf(e.name());
    }

    @NotNull
    public static Location toBukkit(@NotNull final FacingCoordinates position, final World world) {
        return new Location(world, position.getX(), position.getY(), position.getZ());
    }
}
