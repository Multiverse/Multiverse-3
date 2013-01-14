package com.mvplugin.core.world;

import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Used to remove animals from worlds that don't belong there.
 */
public interface WorldPurger {
    /**
     * Synchronizes the given worlds with their settings.
     *
     * @param worlds A list of {@link MultiverseWorld}
     */
    void purgeWorlds(@NotNull final List<MultiverseWorld> worlds);

    void purgeWorld(@NotNull final MultiverseWorld world);

    /**
     * Clear all animals/monsters that do not belong to a world according to the config.
     *
     * @param mvworld The {@link MultiverseWorld}.
     * @param thingsToKill A {@link java.util.List} of animals/monsters to be killed.
     * @param negateAnimals Whether the monsters in the list should be negated.
     * @param negateMonsters Whether the animals in the list should be negated.
     */
    void purgeWorld(@NotNull final MultiverseWorld mvworld,
                    @NotNull final List<String> thingsToKill,
                    final boolean negateAnimals,
                    final boolean negateMonsters);

    /**
     * Clear all animals/monsters that do not belong to a world according to the config.
     *
     * @param mvworld The {@link MultiverseWorld}.
     * @param thingsToKill A {@link java.util.List} of animals/monsters to be killed.
     * @param negateAnimals Whether the monsters in the list should be negated.
     * @param negateMonsters Whether the animals in the list should be negated.
     * @param sender The {@link BasePlayer} that initiated the action. He will/should be notified.
     */
    void purgeWorld(@NotNull final MultiverseWorld mvworld,
                    @NotNull final List<String> thingsToKill,
                    final boolean negateAnimals,
                    final boolean negateMonsters,
                    @Nullable final BasePlayer sender);

    /**
     * Determines whether the specified creature should be killed.
     *
     * @param e The creature.
     * @param thingsToKill A {@link java.util.List} of animals/monsters to be killed.
     * @param negateAnimals Whether the monsters in the list should be negated.
     * @param negateMonsters Whether the animals in the list should be negated.
     * @return {@code true} if the creature should be killed, otherwise {@code false}.
     */
    boolean shouldWeKillThisCreature(@NotNull final Entity e,
                                     @NotNull final List<String> thingsToKill,
                                     final boolean negateAnimals,
                                     final boolean negateMonsters);

    /**
     * Determines whether the specified creature should be killed and automatically reads the params from a world object.
     *
     * @param w The world.
     * @param e The creature.
     * @return {@code true} if the creature should be killed, otherwise {@code false}.
     */
    boolean shouldWeKillThisCreature(@NotNull final MultiverseWorld w, @NotNull final Entity e);
}
