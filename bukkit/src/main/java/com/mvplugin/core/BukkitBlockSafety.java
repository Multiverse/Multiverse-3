package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.BlockCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

class BukkitBlockSafety extends AbstractBlockSafety {

    @Override
    protected boolean isSolidBlock(@NotNull final BlockCoordinates l) {
        if (l.getBlockY() < 0) {
            Logging.finer("Location '%s' is below the world.");
            return false;
        }
        final World world = Bukkit.getWorld(l.getWorld());
        if (world == null) {
            Logging.warning("World does not exist for location '%s'", l);
            return true;
        }
        if (l.getBlockY() >= world.getMaxHeight()) {
            Logging.finer("Location '%s' is above the world.");
            return false;
        }
        final Block block = world.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        final Material type = block.getType();
        // TODO Needs to be updated with newer blocks.
        switch (type) {
            case AIR:
                return false;
            case SNOW:
                return false;
            case TRAP_DOOR:
                return false;
            case TORCH:
                return false;
            case YELLOW_FLOWER:
                return false;
            case RED_ROSE:
                return false;
            case RED_MUSHROOM:
                return false;
            case BROWN_MUSHROOM:
                return false;
            case REDSTONE:
                return false;
            case REDSTONE_WIRE:
                return false;
            case RAILS:
                return false;
            case POWERED_RAIL:
                return false;
            case REDSTONE_TORCH_ON:
                return false;
            case REDSTONE_TORCH_OFF:
                return false;
            case DEAD_BUSH:
                return false;
            case SAPLING:
                return false;
            case STONE_BUTTON:
                return false;
            case LEVER:
                return false;
            case LONG_GRASS:
                return false;
            case PORTAL:
                return false;
            case STONE_PLATE:
                return false;
            case WOOD_PLATE:
                return false;
            case SEEDS:
                return false;
            case SUGAR_CANE_BLOCK:
                return false;
            case WALL_SIGN:
                return false;
            case SIGN_POST:
                return false;
            case WOODEN_DOOR:
                return false;
            case STATIONARY_WATER:
                return false;
            case WATER:
                return false;
            default:
                Logging.finer("Solid block detected at %s", l);
                return true;
        }
    }

    @Override
    protected boolean isBlockSafe(@NotNull final BlockCoordinates l) {
        if (l.getBlockY() < 0) {
            Logging.finer("Location '%s' is below the world.");
            return false;
        }
        final World world = Bukkit.getWorld(l.getWorld());
        if (world == null) {
            Logging.warning("World does not exist for location '%s'", l);
            return true;
        }
        if (l.getBlockY() >= world.getMaxHeight()) {
            Logging.finer("Location '%s' is above the world.");
            return false;
        }
        final Block block = world.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        final Material type = block.getType();

        if (type == Material.LAVA || type == Material.STATIONARY_LAVA) {
            Logging.finer("Lava detected at %s", l);
            return false;
        }
        if (type == Material.FIRE) {
            Logging.finer("Fire detected at %s", l);
            return false;
        }
        return true;
    }

    @Override
    protected boolean isBlockAir(@NotNull final BlockCoordinates l) {
        if (l.getBlockY() < 0) {
            Logging.finer("Location '%s' is below the world.");
            return false;
        }
        final World world = Bukkit.getWorld(l.getWorld());
        if (world == null) {
            Logging.warning("World does not exist for location '%s'", l);
            return true;
        }
        if (l.getBlockY() >= world.getMaxHeight()) {
            Logging.finer("Location '%s' is above the world.");
            return false;
        }
        final Block block = world.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        final Material type = block.getType();
        return type == Material.AIR;
    }

    @Override
    protected boolean hasTwoBlocksOfWaterBelow(@NotNull final BlockCoordinates l) {
        if (l.getBlockY() < 0) {
            Logging.finer("Location '%s' is below the world.");
            return false;
        }
        final World world = Bukkit.getWorld(l.getWorld());
        if (world == null) {
            Logging.warning("World does not exist for location '%s'", l);
            return true;
        }
        if (l.getBlockY() >= world.getMaxHeight()) {
            Logging.finer("Location '%s' is above the world.");
            return false;
        }
        final Block block = world.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        final Block blockBelow = world.getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());

        if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
            return (blockBelow.getType() == Material.WATER || blockBelow.getType() == Material.STATIONARY_WATER);
        }
        if (block.getType() != Material.AIR) {
            return false;
        }

        return hasTwoBlocksOfWaterBelow(Locations.getBlockCoordinates(l.getWorld(),
                l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()));
    }
}
