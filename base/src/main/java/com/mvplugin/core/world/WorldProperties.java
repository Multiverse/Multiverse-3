package com.mvplugin.core.world;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import com.mvplugin.core.SpawnException;
import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.GameMode;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.Nullable;

/**
 * Houses all of the properties for a Multiverse world.
 */
public interface WorldProperties extends Properties {

    SimpleProperty<String> ALIAS = PropertyFactory.newProperty(String.class, "alias", "")
            .comment("World aliases allow you to name a world differently than what the folder name is.")
            .comment("This lets you choose fancy names for your worlds while keeping the folders nice and neat.")
            .comment("You may add minecraft color and formatting codes here prepended with a &")
            .description(PropertyDescriptions.ALIAS)
            .build();

    SimpleProperty<Boolean> HIDDEN = PropertyFactory.newProperty(Boolean.class, "hidden", false)
            .comment("The hidden property allows you to have a world that exists but does not show up in lists.")
            .description(PropertyDescriptions.HIDDEN)
            .build();

    SimpleProperty<Boolean> PREFIX_CHAT = PropertyFactory.newProperty(Boolean.class, "prefixChat", true)
            .comment("The prefixChat property adds the world's name (or alias) as a prefix to chat messages.")
            .comment("Please note, this property can be disabled globally in the configuration.")
            .description(PropertyDescriptions.PREFIX_CHAT)
            .build();

    SimpleProperty<Long> SEED = PropertyFactory.newProperty(Long.class, "seed", 0L)
            .comment("The seed property allows you to change the world's seed.")
            .description(PropertyDescriptions.SEED)
            .build();

    SimpleProperty<String> GENERATOR = PropertyFactory.newProperty(String.class, "generator", "")
            .comment("The generator property allows you to specify the generator used to generate this world.")
            .description(PropertyDescriptions.GENERATOR)
            .build();

    SimpleProperty<WorldEnvironment> ENVIRONMENT = PropertyFactory.newProperty(WorldEnvironment.class, "environment", WorldEnvironment.NORMAL)
            .comment("The Minecraft world environment such as NORMAL, NETHER, THE_END.")
            .description(PropertyDescriptions.ENVIRONMENT)
            .build();

    /* TODO remove these when certain they won't matter.
    SimpleProperty<WorldType> TYPE = PropertyFactory.newProperty(WorldType.class, "type", WorldType.NORMAL)
            .comment("The Minecraft world type such as NORMAL, FLAT, LARGE_BIOMES.  DO NOT CHANGE!")
            .description(PropertyDescriptions.TYPE)
            .build();

    SimpleProperty<Boolean> GENERATE_STRUCTURES = PropertyFactory.newProperty(Boolean.class, "generateStructures", true)
            .comment("Whether or not the Minecraft world generates structures.  DO NOT CHANGE!")
            .description(PropertyDescriptions.GENERATE_STRUCTURES)
            .build();
    */

    SimpleProperty<Integer> PLAYER_LIMIT = PropertyFactory.newProperty(Integer.class, "playerLimit", -1)
            .comment("The player limit property limits the number of players in a world at a time.")
            .comment("A value of -1 or lower signifies no player limit.")
            .description(PropertyDescriptions.PLAYER_LIMIT)
            .build();

    SimpleProperty<Boolean> ADJUST_SPAWN = PropertyFactory.newProperty(Boolean.class, "adjustSpawn", true)
            .comment("The adjust spawn property determines whether or not Multiverse will make adjustments to the world's spawn location if it is unsafe.")
            .description(PropertyDescriptions.ADJUST_SPAWN)
            .build();


    SimpleProperty<Boolean> AUTO_LOAD = PropertyFactory.newProperty(Boolean.class, "autoLoad", true)
            .comment("The autoLoad dictates whether this world is loaded automatically on startup or not.")
            .comment("This has no effect on default worlds!")
            .description(PropertyDescriptions.AUTO_LOAD)
            .build();

    SimpleProperty<Boolean> BED_RESPAWN = PropertyFactory.newProperty(Boolean.class, "bedRespawn", true)
            .comment("The bedRespawn property specifies if a player dying in this world should respawn in their bed or not.")
            .description(PropertyDescriptions.BED_RESPAWN)
            .build();

    SimpleProperty<Boolean> HUNGER = PropertyFactory.newProperty(Boolean.class, "hunger", true)
            .comment("The hunger property specifies if hunger is depleted in this world")
            .description(PropertyDescriptions.HUNGER)
            .build();

    ListProperty<String> BLACK_LIST = PropertyFactory.newListProperty(String.class, "worldBlacklist")
            .comment("The worldBlackList property allows you to specify worlds that people cannot go to from this specified world.")
            .description(PropertyDescriptions.BLACK_LIST)
            .build();

    SimpleProperty<Boolean> PVP = PropertyFactory.newProperty(Boolean.class, "pvp", true)
            .comment("The pvp property states whether or not players may harm each other in this world. If set to true, they may.")
            .comment("Bear in mind, many other plugins may have conflicts with this setting.")
            .description(PropertyDescriptions.PVP)
            .build();

    SimpleProperty<Double> SCALE = PropertyFactory.newProperty(Double.class, "scale", 1D)
            .comment("The scale property represents the scaling of worlds when using Multiverse-NetherPortals.")
            .comment("Setting this value will have no effect on anything but Multiverse-NetherPortals.")
            .description(PropertyDescriptions.SCALE)
            .validator(new ScaleValidator())
            .build();

    class ScaleValidator implements PropertyValidator<Double> {

        @Override
        public boolean isValid(@Nullable final Double scale) {
            return scale != null && scale > 0D;
        }

        @Override
        public Message getInvalidMessage() {
            return PropertyDescriptions.INVALID_SCALE;
        }
    }

    SimpleProperty<String> RESPAWN_WORLD = PropertyFactory.newProperty(String.class, "respawnWorld", "")
            .comment("The respawnWorld property is the world you will respawn to if you die in this world.")
            .comment("This value can be the same as this world.")
            .description(PropertyDescriptions.RESPAWN_WORLD)
            .build();

    SimpleProperty<Boolean> ALLOW_WEATHER = PropertyFactory.newProperty(Boolean.class, "allowWeather", true)
            .comment("The allowWeather property specifies whether or not to allow weather events in this world.")
            .description(PropertyDescriptions.ALLOW_WEATHER)
            .build();

    SimpleProperty<Difficulty> DIFFICULTY = PropertyFactory.newProperty(Difficulty.class, "difficulty", Difficulty.EASY)
            .comment("The difficulty property allows you to set the difficulty for the world.")
            .comment("World difficulty affects spawn rates, hunger rates, and other things that make the game more or less difficult.")
            .description(PropertyDescriptions.DIFFICULTY)
            .build();

    SimpleProperty<Boolean> AUTO_HEAL = PropertyFactory.newProperty(Boolean.class, "autoHeal", true)
            .comment("The autoHeal property will specify whether ot not players will regain health in PEACEFUL difficulty only.")
            .comment("This setting has no effect on worlds with a difficulty greater than peaceful or 0.")
            .description(PropertyDescriptions.AUTO_HEAL)
            .build();

    SimpleProperty<PortalType> PORTAL_FORM = PropertyFactory.newProperty(PortalType.class, "portalForm", PortalType.ALL)
            .comment("The portalFrom property allows you to specify which type of portals are allowed to be created in this world.")
            .description(PropertyDescriptions.PORTAL_FORM)
            .build();

    SimpleProperty<GameMode> GAME_MODE = PropertyFactory.newProperty(GameMode.class, "gameMode", GameMode.SURVIVAL)
            .comment("The gameMode property allows you to specify the GameMode for this world.")
            .comment("Players entering this world will automatically be switched to this GameMode unless they are exempted.")
            .description(PropertyDescriptions.GAME_MODE)
            .build();

    SimpleProperty<Boolean> KEEP_SPAWN = PropertyFactory.newProperty(Boolean.class, "keepSpawnInMemory", true)
            .comment("The keepSpawnInMemory property specifies whether or not to keep the spawn chunks loaded in memory when players aren't in the spawn area.")
            .comment("Setting this to false will potentially save you some memory.")
            .description(PropertyDescriptions.KEEP_SPAWN)
            .build();

    SimpleProperty<FacingCoordinates> SPAWN_LOCATION = PropertyFactory.newProperty(FacingCoordinates.class, "spawnLocation", Locations.NULL_FACING)
            .comment("The spawnLocation property specifies where in the world players will spawn.")
            .comment("The world specified here has no effect.")
            .description(PropertyDescriptions.SPAWN_LOCATION)
            .build();

    NestedProperty<EntryFee> ENTRY_FEE = PropertyFactory.newNestedProperty(EntryFee.class, "entryFee")
            .build();

    public static interface EntryFee extends NestedProperties {

        SimpleProperty<Double> AMOUNT = PropertyFactory.newProperty(Double.class, "amount", 0D)
                .comment("The amount property specifies how much a player has to pay to enter this world.")
                .comment("What the player has to pay is specified by the 'currency' property")
                .description(PropertyDescriptions.AMOUNT)
                .build();

        SimpleProperty<Integer> CURRENCY = PropertyFactory.newProperty(Integer.class, "currency", -1)
                .comment("The currency property specifies what type of currency the player must pay (if any) to enter this world.")
                .comment("Currency can be an economy money by specifying -1 or a block type by specifying the block id.")
                .description(PropertyDescriptions.CURRENCY)
                .build();
    }

    NestedProperty<Spawning> SPAWNING = PropertyFactory.newNestedProperty(Spawning.class, "spawning")
            .build();

    public static interface Spawning extends NestedProperties {

        SimpleProperty<Long> ANIMAL_TICKS = PropertyFactory.newProperty(Long.class, "animalTicks", -1L)
                .comment("The animalTicks property specifies the rate in ticks at which animals are spawned.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.ANIMAL_TICKS)
                .alias("animalRate")
                .build();

        SimpleProperty<Long> MONSTER_TICKS = PropertyFactory.newProperty(Long.class, "monstersTicks", -1L)
                .comment("The monsterTicks property specifies the rate in ticks at which monsters are spawned.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.MONSTER_TICKS)
                .alias("monsterRate")
                .build();

        SimpleProperty<Integer> ANIMAL_LIMIT = PropertyFactory.newProperty(Integer.class, "animalLimit", -1)
                .comment("The animalLimit property specifies how many animal entities are allowed to be spawned per chunk.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.ANIMAL_LIMIT)
                .build();

        SimpleProperty<Integer> MONSTER_LIMIT = PropertyFactory.newProperty(Integer.class, "monsterLimit", -1)
                .comment("The monsterLimit property specifies how many monster entities are allowed to be spawned per chunk.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.MONSTER_LIMIT)
                .build();

        SimpleProperty<Integer> AMBIENT_LIMIT = PropertyFactory.newProperty(Integer.class, "ambientLimit", -1)
                .comment("The ambientLimit property specifies how many ambient entities are allowed to be spawned per chunk.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.AMBIENT_LIMIT)
                .build();

        SimpleProperty<Integer> WATER_LIMIT = PropertyFactory.newProperty(Integer.class, "waterLimit", -1)
                .comment("The waterLimit property specifies how many water entities are allowed to be spawned per chunk.")
                .comment("A negative value will indicate the default will be used.")
                .description(PropertyDescriptions.WATER_LIMIT)
                .build();

        NestedProperty<AllowedSpawns> ALLOWED_SPAWNS = PropertyFactory.newNestedProperty(AllowedSpawns.class, "allowedSpawns")
                .build();

        public static interface AllowedSpawns extends NestedProperties {

            SimpleProperty<Boolean> PREVENT_SPAWNS = PropertyFactory.newProperty(Boolean.class, "preventSpawnsList", true)
                    .comment("The preventSpawnsList property determines whether the spawnExceptions list is a blacklist or a whitelist.")
                    .comment("Setting this to false will indicate the creatures listed in spawnExceptions will be the ONLY creatures allowed to spawn.")
                    .comment("Setting this to true will indicate the creatures listed in spawnExceptions will be creatures NOT allowed to spawn.")
                    .description(PropertyDescriptions.PREVENT_SPAWNS)
                    .alias("preventSpawns")
                    .build();

            ListProperty<SpawnException> SPAWN_EXCEPTIONS = PropertyFactory.newListProperty(SpawnException.class, "spawnExceptions")
                    .comment("The spawnExceptions property defines what creatures are allowed/disallowed in this world.")
                    .comment("Whether or not they are allowed or disallowed is based on the value of preventSpawnsList.")
                    .description(PropertyDescriptions.SPAWN_EXCEPTIONS)
                    .alias("exceptions")
                    .build();
        }
    }
}
