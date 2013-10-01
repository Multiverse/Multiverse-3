package com.mvplugin.core.util;

import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.WorldEnvironment;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

public final class BukkitConvert {

    private BukkitConvert() {
        throw new AssertionError();
    }

    @NotNull
    public static org.bukkit.Difficulty toBukkit(@NotNull final Difficulty d) {
        return org.bukkit.Difficulty.valueOf(d.name());
    }

    @NotNull
    public static Difficulty fromBukkit(@NotNull org.bukkit.Difficulty d) {
        return Difficulty.valueOf(d.name());
    }

    @NotNull
    public static WorldType toBukkit(@NotNull final com.mvplugin.core.minecraft.WorldType t) {
        return WorldType.valueOf(t.name());
    }

    @NotNull
    public static com.mvplugin.core.minecraft.WorldType fromBukkit(@NotNull final WorldType t) {
        return com.mvplugin.core.minecraft.WorldType.valueOf(t.name());
    }

    @NotNull
    public static Environment toBukkit(@NotNull final WorldEnvironment e) {
        return Environment.valueOf(e.name());
    }

    @NotNull
    public static WorldEnvironment fromBukkit(@NotNull final Environment e) {
        return WorldEnvironment.valueOf(e.name());
    }

    @NotNull
    public static Location toBukkit(@NotNull final FacingCoordinates position, final World world) {
        return new Location(world, position.getX(), position.getY(), position.getZ());
    }

    @NotNull
    public static FacingCoordinates fromBukkit(@NotNull Location l) {
        return Locations.getFacingCoordinates(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
    }
}
