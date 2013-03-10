package com.mvplugin.core.util;

import com.dumptruckman.minecraft.pluginbase.messages.Message;

/**
 * Houses localized (english) descriptions of the Multiverse world properties.
 */
public class PropertyDescriptions {

    public static void init() { }

    public static final Message ALIAS = Message.createMessage("world_properties.descriptions.alias",
            "World aliases allow you to name a world differently than what the folder name is."
            + "\nThis lets you choose fancy names for your worlds while keeping the folders nice and neat."
            + "\nYou may add minecraft color and formatting codes here prepended with a &");

    public static final Message HIDDEN = Message.createMessage("world_properties.descriptions.hidden",
            "The hidden property allows you to have a world that exists but does not show up in lists.");

    public static final Message PREFIX_CHAT = Message.createMessage("world_properties.descriptions.prefixChat",
            "The prefixChat property adds the world's name (or alias) as a prefix to chat messages.");

    public static final Message SEED = Message.createMessage("world_properties.descriptions.seed",
            "The seed property allows you to change the world's seed.");

    public static final Message GENERATOR = Message.createMessage("world_properties.descriptions.generator",
            "The generator property allows you to specify the generator used to generate this world.");

    public static final Message ENVIRONMENT = Message.createMessage("world_properties.descriptions.environment",
            "The Minecraft world environment such as NORMAL, NETHER, THE_END.");

    /* TODO remove these when certain they won't matter.
    public static final Message TYPE = Message.createMessage("world_properties.descriptions.type",
            "The Minecraft world type such as NORMAL, FLAT, LARGE_BIOMES.  DO NOT CHANGE!");

    public static final Message GENERATE_STRUCTURES = Message.createMessage("world_properties.descriptions.generateStructures",
            "Whether or not the Minecraft world generates structures.  DO NOT CHANGE!");
    */

    public static final Message PLAYER_LIMIT = Message.createMessage("world_properties.descriptions.playerLimit",
            "The player limit property limits the number of players in a world at a time."
            + "\nA value of -1 or lower signifies no player limit.");

    public static final Message ADJUST_SPAWN = Message.createMessage("world_properties.descriptions.adjustSpawn",
            "The adjust spawn property determines whether or not Multiverse will make adjustments to the world's spawn location if it is unsafe.");

    public static final Message AUTO_LOAD = Message.createMessage("world_properties.descriptions.autoLoad",
            "The autoLoad property dictates whether this world is loaded automatically on startup or not.");

    public static final Message BED_RESPAWN = Message.createMessage("world_properties.descriptions.bedRespawn",
            "The bedRespawn property specifies if a player dying in this world should respawn in their bed or not.");

    public static final Message HUNGER = Message.createMessage("world_properties.descriptions.hunger",
            "The hunger property specifies if hunger is depleted in this world");

    public static final Message BLACK_LIST = Message.createMessage("world_properties.descriptions.worldBlacklist",
            "The worldBlackList property allows you to specify worlds that people cannot go to from this specified world.");

    public static final Message PVP = Message.createMessage("world_properties.descriptions.pvp",
            "The pvp property states whether or not players may harm each other in this world. If set to true, they may.");

    public static final Message SCALE = Message.createMessage("world_properties.descriptions.scale",
            "The scale property represents the scaling of worlds when using Multiverse-NetherPortals."
            + "\nSetting this value will have no effect on anything but Multiverse-NetherPortals.");

    public static final Message RESPAWN_WORLD = Message.createMessage("world_properties.descriptions.respawnWorld",
            "The respawnWorld property is the world you will respawn to if you die in this world."
            + "\nThis value can be the same as this world.");

    public static final Message ALLOW_WEATHER = Message.createMessage("world_properties.descriptions.allowWeather",
            "The allowWeather property specifies whether or not to allow weather events in this world.");

    public static final Message DIFFICULTY = Message.createMessage("world_properties.descriptions.difficulty",
            "The difficulty property allows you to set the difficulty for the world."
            + "\nWorld difficulty affects spawn rates, hunger rates, and other things that make the game more or less difficult.");

    public static final Message AUTO_HEAL = Message.createMessage("world_properties.descriptions.autoHeal",
            "The autoHeal property will specify whether ot not players will regain health in PEACEFUL difficulty only."
            + "\nThis setting has no effect on worlds with a difficulty greater than peaceful or 0.");

    public static final Message PORTAL_FORM = Message.createMessage("world_properties.descriptions.portalForm",
            "The portalFrom property allows you to specify which type of portals are allowed to be created in this world.");

    public static final Message GAME_MODE = Message.createMessage("world_properties.descriptions.gameMode",
            "The gameMode property allows you to specify the GameMode for this world."
            + "\nPlayers entering this world will automatically be switched to this GameMode unless they are exempted.");

    public static final Message KEEP_SPAWN = Message.createMessage("world_properties.descriptions.keepSpawnInMemory",
            "The keepSpawnInMemory property specifies whether or not to keep the spawn chunks loaded in memory when players aren't in the spawn area."
            + "\nSetting this to false will potentially save you some memory.");

    public static final Message SPAWN_LOCATION = Message.createMessage("world_properties.descriptions.spawnLocation",
            "The spawnLocation property specifies where in the world players will spawn.");

    public static final Message AMOUNT = Message.createMessage("world_properties.descriptions.amount",
            "The amount property specifies how much a player has to pay to enter this world."
            + "\nWhat the player has to pay is specified by the 'currency' property");

    public static final Message CURRENCY = Message.createMessage("world_properties.descriptions.currency",
            "The currency property specifies what type of currency the player must pay (if any) to enter this world."
            + "\nCurrency can be an economy money by specifying -1 or a block type by specifying the block id.");

    public static final Message ANIMALS_SPAWN = Message.createMessage("world_properties.descriptions.animals.spawn",
            "The animals spawn property specifies whether or not to spawn animals in this world.");

    public static final Message ANIMALS_SPAWN_RATE = Message.createMessage("world_properties.descriptions.animals.spawnRate",
            "The animals spawnRate property defines how many ticks in between attempting to spawn animals."
            + "\nA value of -1 indicates the default should be used and is recommended unless you know what you are doing.");

    public static final Message ANIMALS_SPAWN_EXCEPTIONS = Message.createMessage("world_properties.descriptions.animals.exceptions",
            "The animals exceptions property defines what animals are exempt from the animals spawn property.");

    public static final Message MONSTERS_SPAWN = Message.createMessage("world_properties.descriptions.monsters.spawn",
            "The monsters spawn property specifies whether or not to spawn monsters in this world.");

    public static final Message MONSTERS_SPAWN_RATE = Message.createMessage("world_properties.descriptions.monsters.spawnRate",
            "The monsters spawnRate property defines how many ticks in between attempting to spawn monsters."
            + "\nA value of -1 indicates the default should be used and is recommended unless you know what you are doing.");

    public static final Message MONSTERS_SPAWN_EXCEPTIONS = Message.createMessage("world_properties.descriptions.monsters.exceptions",
            "The monsters exceptions property defines what monsters are exempt from the monsters spawn property.");


    public static final Message INVALID_SCALE = Message.createMessage("world_properties.validation.scale",
            "Scale must be a number higher than 0!");

    public static final Message INVALID_RESPAWN_WORLD = Message.createMessage("world_properties.validation.respawnWorld",
            "Respawn world must be a world known to Multiverse!");
}
