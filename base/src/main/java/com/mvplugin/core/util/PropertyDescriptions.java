package com.mvplugin.core.util;

import pluginbase.messages.Message;

/**
 * Houses localized (english) descriptions of the Multiverse world properties.
 */
public enum PropertyDescriptions {
    ;

    public static void init() { }

    public static final String ALIAS_KEY = "world_properties.descriptions.alias";
    public static final Message ALIAS = Message.createMessage(ALIAS_KEY,
            "World aliases allow you to name a world differently than what the folder name is."
            + "\nThis lets you choose fancy names for your worlds while keeping the folders nice and neat."
            + "\nYou may add minecraft color and formatting codes here prepended with a &");

    public static final String HIDDEN_KEY = "world_properties.descriptions.hidden";
    public static final Message HIDDEN = Message.createMessage(HIDDEN_KEY,
            "The hidden property allows you to have a world that exists but does not show up in lists.");

    public static final String PREFIX_CHAT_KEY = "world_properties.descriptions.prefixChat";
    public static final Message PREFIX_CHAT = Message.createMessage(PREFIX_CHAT_KEY,
            "The prefixChat property adds the world's name (or alias) as a prefix to chat messages.");

    public static final String SEED_KEY = "world_properties.descriptions.seed";
    public static final Message SEED = Message.createMessage(SEED_KEY,
            "The seed property allows you to change the world's seed.");

    public static final String GENERATOR_KEY = "world_properties.descriptions.generator";
    public static final Message GENERATOR = Message.createMessage(GENERATOR_KEY,
            "The generator property allows you to specify the generator used to generate this world.");

    public static final String ENVIRONMENT_KEY = "world_properties.descriptions.environment";
    public static final Message ENVIRONMENT = Message.createMessage(ENVIRONMENT_KEY,
            "The Minecraft world environment such as NORMAL, NETHER, THE_END.");

    /* TODO remove these when certain they won't matter.
    public static final Message TYPE = Message.createMessage("world_properties.descriptions.type",
            "The Minecraft world type such as NORMAL, FLAT, LARGE_BIOMES.  DO NOT CHANGE!");

    public static final Message GENERATE_STRUCTURES = Message.createMessage("world_properties.descriptions.generateStructures",
            "Whether or not the Minecraft world generates structures.  DO NOT CHANGE!");
    */

    public static final String PLAYER_LIMIT_KEY = "world_properties.descriptions.playerLimit";
    public static final Message PLAYER_LIMIT = Message.createMessage(PLAYER_LIMIT_KEY,
            "The player limit property limits the number of players in a world at a time."
            + "\nA value of -1 or lower signifies no player limit.");

    public static final String ADJUST_SPAWN_KEY = "world_properties.descriptions.adjustSpawn";
    public static final Message ADJUST_SPAWN = Message.createMessage(ADJUST_SPAWN_KEY,
            "The adjust spawn property determines whether or not Multiverse will make adjustments to the world's spawn location if it is unsafe.");

    public static final String AUTO_LOAD_KEY = "world_properties.descriptions.autoLoad";
    public static final Message AUTO_LOAD = Message.createMessage(AUTO_LOAD_KEY,
            "The autoLoad property dictates whether this world is loaded automatically on startup or not.");

    public static final String BED_RESPAWN_KEY = "world_properties.descriptions.bedRespawn";
    public static final Message BED_RESPAWN = Message.createMessage(BED_RESPAWN_KEY,
            "The bedRespawn property specifies if a player dying in this world should respawn in their bed or not.");

    public static final String HUNGER_KEY = "world_properties.descriptions.hunger";
    public static final Message HUNGER = Message.createMessage(HUNGER_KEY,
            "The hunger property specifies if hunger is depleted in this world");

    public static final String BLACK_LIST_KEY = "world_properties.descriptions.worldBlackList";
    public static final Message BLACK_LIST = Message.createMessage(BLACK_LIST_KEY,
            "The worldBlackList property allows you to specify worlds that people cannot go to from this specified world.");

    public static final String PVP_KEY = "world_properties.descriptions.pvp";
    public static final Message PVP = Message.createMessage(PVP_KEY,
            "The pvp property states whether or not players may harm each other in this world. If set to true, they may.");

    public static final String SCALE_KEY = "world_properties.descriptions.scale";
    public static final Message SCALE = Message.createMessage(SCALE_KEY,
            "The scale property represents the scaling of worlds when using Multiverse-NetherPortals."
            + "\nSetting this value will have no effect on anything but Multiverse-NetherPortals.");

    public static final String RESPAWN_WORLD_KEY = "world_properties.descriptions.respawnWorld";
    public static final Message RESPAWN_WORLD = Message.createMessage(RESPAWN_WORLD_KEY,
            "The respawnWorld property is the world you will respawn to if you die in this world."
            + "\nThis value can be the same as this world.");

    public static final String ALLOW_WEATHER_KEY = "world_properties.descriptions.allowWeather";
    public static final Message ALLOW_WEATHER = Message.createMessage(ALLOW_WEATHER_KEY,
            "The allowWeather property specifies whether or not to allow weather events in this world.");

    public static final String DIFFICULTY_KEY = "world_properties.descriptions.difficulty";
    public static final Message DIFFICULTY = Message.createMessage(DIFFICULTY_KEY,
            "The difficulty property allows you to set the difficulty for the world."
            + "\nWorld difficulty affects spawn rates, hunger rates, and other things that make the game more or less difficult.");

    public static final String AUTO_HEAL_KEY = "world_properties.descriptions.autoHeal";
    public static final Message AUTO_HEAL = Message.createMessage(AUTO_HEAL_KEY,
            "The autoHeal property will specify whether ot not players will regain health in PEACEFUL difficulty only."
            + "\nThis setting has no effect on worlds with a difficulty greater than peaceful or 0.");

    public static final String PORTAL_FORM_KEY = "world_properties.descriptions.portalForm";
    public static final Message PORTAL_FORM = Message.createMessage(PORTAL_FORM_KEY,
            "The portalFrom property allows you to specify which type of portals are allowed to be created in this world.");

    public static final String GAME_MODE_KEY = "world_properties.descriptions.gameMode";
    public static final Message GAME_MODE = Message.createMessage(GAME_MODE_KEY,
            "The gameMode property allows you to specify the GameMode for this world."
            + "\nPlayers entering this world will automatically be switched to this GameMode unless they are exempted.");

    public static final String KEEP_SPAWN_KEY = "world_properties.descriptions.keepSpawnInMemory";
    public static final Message KEEP_SPAWN = Message.createMessage(KEEP_SPAWN_KEY,
            "The keepSpawnInMemory property specifies whether or not to keep the spawn chunks loaded in memory when players aren't in the spawn area."
            + "\nSetting this to false will potentially save you some memory.");

    public static final String SPAWN_LOCATION_KEY = "world_properties.descriptions.spawnLocation";
    public static final Message SPAWN_LOCATION = Message.createMessage(SPAWN_LOCATION_KEY,
            "The spawnLocation property specifies where in the world players will spawn.");

    public static final String AMOUNT_KEY = "world_properties.descriptions.amount";
    public static final Message AMOUNT = Message.createMessage(AMOUNT_KEY,
            "The amount property specifies how much a player has to pay to enter this world."
            + "\nWhat the player has to pay is specified by the 'currency' property");

    public static final String CURRENCY_KEY = "world_properties.descriptions.currency";
    public static final Message CURRENCY = Message.createMessage(CURRENCY_KEY,
            "The currency property specifies what type of currency the player must pay (if any) to enter this world."
            + "\nCurrency can be an economy money by specifying -1 or a block type by specifying the block id.");

    public static final String ANIMAL_TICKS_KEY = "world_properties.descriptions.spawning.animalTicks";
    public static final Message ANIMAL_TICKS = Message.createMessage(ANIMAL_TICKS_KEY,
            "The animalTicks property specifies the rate in ticks at which animals are spawned."
            + "\nA negative value will indicate the default will be used.");

    public static final String MONSTER_TICKS_KEY = "world_properties.descriptions.spawning.monsterTicks";
    public static final Message MONSTER_TICKS = Message.createMessage(MONSTER_TICKS_KEY,
            "The monsterTicks property specifies the rate in ticks at which monsters are spawned."
            + "\nA negative value will indicate the default will be used.");

    public static final String ANIMAL_LIMIT_KEY = "world_properties.descriptions.spawning.animalLimit";
    public static final Message ANIMAL_LIMIT = Message.createMessage(ANIMAL_LIMIT_KEY,
            "The animalLimit property specifies how many animal entities are allowed to be spawned per chunk."
            + "\nA negative value will indicate the default will be used.");

    public static final String MONSTER_LIMIT_KEY = "world_properties.descriptions.spawning.monsterLimit";
    public static final Message MONSTER_LIMIT = Message.createMessage(MONSTER_LIMIT_KEY,
            "The monsterLimit property specifies how many monster entities are allowed to be spawned per chunk."
            + "\nA negative value will indicate the default will be used.");

    public static final String AMBIENT_LIMIT_KEY = "world_properties.descriptions.spawning.ambientLimit";
    public static final Message AMBIENT_LIMIT = Message.createMessage(AMBIENT_LIMIT_KEY,
            "The ambientLimit property specifies how many ambient entities are allowed to be spawned per chunk."
            + "\nA negative value will indicate the default will be used.");

    public static final String WATER_LIMIT_KEY = "world_properties.descriptions.spawning.waterLimit";
    public static final Message WATER_LIMIT = Message.createMessage(WATER_LIMIT_KEY,
            "The waterLimit property specifies how many water entities are allowed to be spawned per chunk."
            + "\nA negative value will indicate the default will be used.");

    //public static final String INVALID_SCALE_KEY = "world_properties.validation.scale";
    public static final Message INVALID_SCALE = Message.createMessage("world_properties.validation.scale",
            "Scale must be a number higher than 0!");

    public static final Message INVALID_RESPAWN_WORLD = Message.createMessage("world_properties.validation.respawnWorld",
            "Respawn world must be a world known to Multiverse!");
}
