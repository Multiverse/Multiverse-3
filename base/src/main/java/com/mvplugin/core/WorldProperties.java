package com.mvplugin.core;

import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.GameMode;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.util.Language.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Description;
import pluginbase.config.annotation.HandlePropertyWith;
import pluginbase.config.annotation.Immutable;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.DependentField;
import pluginbase.config.field.FieldInstance;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.properties.PropertiesWrapper;
import pluginbase.config.properties.PropertyAliases;
import pluginbase.config.properties.PropertyHandler;
import pluginbase.config.serializers.Serializer;
import pluginbase.messages.ChatColor;
import pluginbase.messages.Message;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvplugin.core.util.PropertyDescriptions.*;

/**
 * Houses all of the properties for a Multiverse world.
 */
class WorldProperties extends PropertiesWrapper {

    static {
        createAlias("curr", "currency");
        createAlias("scaling", "scale");
        createAlias("heal", "autoHeal");
        createAlias("storm", "allowWeather");
        createAlias("weather", "allowWeather");
        createAlias("spawnmemory", "keepSpawnInMemory");
        createAlias("memory", "keepSpawnInMemory");
        createAlias("mode", "gameMode");
        createAlias("diff", "difficulty");
        createAlias("spawn", "spawnLocation");
        createAlias("limit", "playerLimit");
        createAlias("animalrate", "spawning", "animalTicks");
        createAlias("monsterrate", "spawning", "monsterTicks");
        createAlias("preventspawns", "spawning", "allowedSpawns", "preventSpawnsList");
        createAlias("exceptions", "spawning", "allowedSpawns", "spawnExceptions");
        createAlias("flight", "allowFlight");
        createAlias("fly", "allowFlight");
        createAlias("allowfly", "allowFlight");
    }

    private static void createAlias(@NotNull String alias, @NotNull String... propertyName) {
        PropertyAliases.createAlias(WorldProperties.class, alias, propertyName);
    }

    @Immutable
    @NotNull
    private transient Proxy<String> name = new Proxy<String>("") {
        @Override
        public String getValue() {
            return worldLink.getName();
        }

        @Override
        public void setValue(String value) { }
    };
    @Immutable
    @NotNull
    private transient Proxy<UUID> uuid = new Proxy<UUID>(null) {
        @Override
        protected UUID getValue() {
            return worldLink.getUID();
        }

        @Override
        protected void setValue(final UUID value) { }
    };
    @Immutable
    @NotNull
    private transient Proxy<WorldType> type = new Proxy<WorldType>(WorldType.NORMAL) {
        @Override
        public WorldType getValue() {
            return worldLink.getType();
        }

        @Override
        public void setValue(final WorldType value) { }
    };
    @NotNull
    @Comment({"The Minecraft world environment such as NORMAL, NETHER, THE_END."})
    @Description(ENVIRONMENT_KEY)
    private Proxy<WorldEnvironment> environment = new Proxy<WorldEnvironment>(WorldEnvironment.NORMAL) {
        @Override
        public WorldEnvironment getValue() {
            return worldLink.getEnvironment();
        }

        @Override
        public void setValue(final WorldEnvironment value) {
            backupValue = value;
        }
    };
    @Comment({
            "The difficulty property allows you to set the difficulty for the world.",
            "World difficulty affects spawn rates, hunger rates, and other things that make the game more or less difficult."
    })
    @Description(DIFFICULTY_KEY)
    @NotNull
    private Proxy<Difficulty> difficulty = new Proxy<Difficulty>(Difficulty.EASY) {
        @Override
        public Difficulty getValue() {
            return worldLink.getDifficulty();
        }

        @Override
        public void setValue(final Difficulty value) {
            worldLink.setDifficulty(value);
        }
    };
    @NotNull
    private transient Proxy<String> time = new Proxy<String>("0") {
        @Override
        protected String getValue() {
            return TimeHelper.asString(worldLink.getTime());
        }

        @Override
        protected void setValue(final String value) {
            worldLink.setTime(TimeHelper.asLong(value));
        }
    };
    @Comment({
            "The spawnLocation property specifies where in the world players will spawn.",
            "The world specified here has no effect."
    })
    @Description(SPAWN_LOCATION_KEY)
    @NotNull
    @SerializeWith(FacingCoordinatesSerializer.class)
    @HandlePropertyWith(FacingCoordinatesPropertyHandler.class)
    private Proxy<FacingCoordinates> spawnLocation = new Proxy<FacingCoordinates>(Locations.NULL_FACING) {
        @Override
        protected FacingCoordinates getValue() {
            return worldLink.getSpawnLocation();
        }

        @Override
        protected void setValue(final FacingCoordinates value) {
            worldLink.setSpawnLocation(value);
        }
    };
    @Comment({"The seed property allows you to change the world's seed."})
    @Description(SEED_KEY)
    private Proxy<Long> seed = new Proxy<Long>(0L) {
        @Override
        protected Long getValue() {
            return worldLink.getSeed();
        }

        @Override
        protected void setValue(final Long value) {
            backupValue = value;
        }
    };
    @Comment({
            "The pvp property states whether or not players may harm each other in this world. If set to true, they may.",
            "Bear in mind, many other plugins may have conflicts with this setting."
    })
    @Description(PVP_KEY)
    private Proxy<Boolean> pvp = new Proxy<Boolean>(true) {
        @Override
        protected Boolean getValue() {
            return worldLink.getPVP();
        }

        @Override
        protected void setValue(final Boolean value) {
            worldLink.setPVP(value);
        }
    };
    @Comment({
            "The keepSpawnInMemory property specifies whether or not to keep the spawn chunks loaded in memory when players aren't in the spawn area.",
            "Setting this to false will potentially save you some memory."
    })
    @Description(KEEP_SPAWN_KEY)
    private Proxy<Boolean> keepSpawnInMemory = new Proxy<Boolean>(true) {
        @Override
        protected Boolean getValue() {
            return worldLink.getKeepSpawnInMemory();
        }

        @Override
        protected void setValue(final Boolean value) {
            worldLink.setKeepSpawnInMemory(value);
        }
    };
    @Comment({"The allowWeather property specifies whether or not to allow weather events in this world."})
    @Description(ALLOW_WEATHER_KEY)
    private Proxy<Boolean> allowWeather = new Proxy<Boolean>(true) {
        @Override
        protected Boolean getValue() {
            return backupValue;
        }

        @Override
        protected void setValue(final Boolean value) {
            worldLink.setEnableWeather(value);
        }
    };
    @Comment({
            "World aliases allow you to name a world differently than what the folder name is.",
            "This lets you choose fancy names for your worlds while keeping the folders nice and neat.",
            "You may add minecraft color and formatting codes here prepended with a &"
    })
    @Description(ALIAS_KEY)
    @NotNull
    @HandlePropertyWith(AliasPropertyHandler.class)
    private String alias = "";
    @Comment({"The hidden property allows you to have a world that exists but does not show up in lists."})
    @Description(HIDDEN_KEY)
    private boolean hidden = false;
    @Comment({
            "The formattingChat property adds the world's name (or alias) as a prefix to chat messages.",
            "Please note, this property can be disabled globally in the configuration."
    })
    @Description(PREFIX_CHAT_KEY)
    private boolean formattingChat = true;
    @Comment({"The generator property allows you to specify the generator used to generate this world."})
    @Description(GENERATOR_KEY)
    @NotNull
    private String generator = "";
    @Comment({
            "The playerLimit property limits the number of players in a world at a time.",
            "A value of -1 or lower signifies no player limit."
    })
    @Description(PLAYER_LIMIT_KEY)
    private int playerLimit = -1;
    @Comment({"The adjustingSpawn property determines whether or not Multiverse will make adjustments to the world's spawn location if it is unsafe."})
    @Description(ADJUST_SPAWN_KEY)
    private boolean adjustingSpawn = true;
    @Comment({
            "The autoLoad property dictates whether this world is loaded automatically on startup or not.",
            "This has no effect on default worlds!"
    })
    @Description(AUTO_LOAD_KEY)
    private boolean autoLoad = true;
    @Comment({"The bedRespawn property specifies if a player dying in this world should respawn in their bed or not."})
    @Description(BED_RESPAWN_KEY)
    private boolean bedRespawn = true;
    @Comment({"The hunger property specifies if hunger is depleted in this world"})
    @Description(HUNGER_KEY)
    private boolean hunger = true;
    @Comment({"The worldBlackList property allows you to specify worlds that people cannot go to from this specified world."})
    @Description(BLACK_LIST_KEY)
    @NotNull
    private List<String> worldBlackList = new ArrayList<String>();
    @Comment({
            "The scale property represents the scaling of worlds when using Multiverse-NetherPortals.",
            "Setting this value will have no effect on anything but Multiverse-NetherPortals."
    })
    @Description(SCALE_KEY)
    @ValidateWith(ScaleValidator.class)
    private double scale = 1D;
    @Comment({
            "The respawnWorld property is the world you will respawn to if you die in this world.",
            "This value can be the same as this world."
    })
    @Description(RESPAWN_WORLD_KEY)
    @NotNull
    private String respawnWorld = "";
    @Comment({
            "The autoHeal property will specify whether ot not players will regain health in PEACEFUL difficulty only.",
            "This setting has no effect on worlds with a difficulty greater than peaceful or 0."
    })
    @Description(AUTO_HEAL_KEY)
    private boolean autoHeal = true;
    @Comment({"The portalFrom property allows you to specify which type of portals are allowed to be created in this world."})
    @Description(PORTAL_FORM_KEY)
    @NotNull
    private PortalType portalForm = PortalType.ALL;
    @Comment({
            "The gameMode property allows you to specify the GameMode for this world.",
            "Players entering this world will automatically be switched to this GameMode unless they are exempted."
    })
    @Description(GAME_MODE_KEY)
    @NotNull
    private GameMode gameMode = GameMode.SURVIVAL;

    @NotNull
    @Immutable
    private final EntryFee entryFee = new EntryFee();
    @Comment({
            "These settings will control the rates at which creatures spawn and also the limits of how many are allowed.",
            "A negative value in any of these settings will indicate the default will be used."
    })
    @NotNull
    @Immutable
    private final Spawning spawning = new Spawning();

    @Immutable
    private transient WorldLink worldLink;

    public WorldProperties() { }

    public WorldProperties(String name) {
        this.name.set(name);
    }

    public void linkToWorld(@Nullable WorldLink worldLink) {
        this.worldLink = worldLink;
    }

    @NotNull
    protected EntryFee getEntryFee() {
        return entryFee;
    }

    @NotNull
    protected Spawning getSpawning() {
        return spawning;
    }

    @NotNull
    public WorldEnvironment getEnvironment() {
        return environment.get();
    }

    public void setEnvironment(@NotNull WorldEnvironment environment) {
        this.environment.set(environment);
    }

    @NotNull
    public Difficulty getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(@NotNull Difficulty difficulty) {
        this.difficulty.set(difficulty);
    }

    @NotNull
    public String getTime() {
        return time.get();
    }

    public void setTime(@NotNull String time) {
        this.time.set(time);
    }

    @NotNull
    public FacingCoordinates getSpawnLocation() {
        return spawnLocation.get();
    }

    public void setSpawnLocation(@Nullable FacingCoordinates spawnLocation) {
        this.spawnLocation.set(spawnLocation != null ? spawnLocation : Locations.NULL_FACING);
    }

    public long getSeed() {
        return seed.get();
    }

    public void setSeed(long seed) {
        this.seed.set(seed);
    }

    public boolean isPVPEnabled() {
        return pvp.get();
    }

    public void setPVPEnabled(boolean pvp) {
        this.pvp.set(pvp);
    }

    public boolean isKeepSpawnInMemory() {
        return keepSpawnInMemory.get();
    }

    public void setKeepSpawnInMemory(boolean keepSpawnInMemory) {
        this.keepSpawnInMemory.set(keepSpawnInMemory);
    }

    public boolean isAllowWeather() {
        return allowWeather.get();
    }

    public void setAllowWeather(boolean allowWeather) {
        this.allowWeather.set(allowWeather);
    }

    @NotNull
    public String getAlias() {
        return alias;
    }

    public void setAlias(@NotNull String alias) {
        this.alias = alias;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isFormattingChat() {
        return formattingChat;
    }

    public void setFormattingChat(boolean formattingChat) {
        this.formattingChat = formattingChat;
    }

    @NotNull
    public String getGenerator() {
        return generator;
    }

    public void setGenerator(@Nullable String generator) {
        this.generator = generator != null ? generator : "";
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
    }

    public boolean isAdjustingSpawn() {
        return adjustingSpawn;
    }

    public void setAdjustingSpawn(boolean adjustingSpawn) {
        this.adjustingSpawn = adjustingSpawn;
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    public boolean isBedRespawn() {
        return bedRespawn;
    }

    public void setBedRespawn(boolean bedRespawn) {
        this.bedRespawn = bedRespawn;
    }

    public boolean isHunger() {
        return hunger;
    }

    public void setHunger(boolean hunger) {
        this.hunger = hunger;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @NotNull
    public String getRespawnWorld() {
        return respawnWorld;
    }

    public void setRespawnWorld(@NotNull String respawnWorld) {
        this.respawnWorld = respawnWorld;
    }

    public boolean isAutoHeal() {
        return autoHeal;
    }

    public void setAutoHeal(boolean autoHeal) {
        this.autoHeal = autoHeal;
    }

    @NotNull
    public PortalType getPortalForm() {
        return portalForm;
    }

    public void setPortalForm(@NotNull PortalType portalForm) {
        this.portalForm = portalForm;
    }

    @NotNull
    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(@NotNull GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @NotNull
    public String getName() {
        return name.get();
    }

    @NotNull
    public UUID getUUID() {
        return uuid.get();
    }

    @NotNull
    public WorldType getType() {
        return type.get();
    }

    @NotNull
    public List<String> getWorldBlackList() {
        return worldBlackList;
    }

    private static class ScaleValidator implements Validator<Double> {
        @Nullable
        @Override
        public Double validateChange(@Nullable Double newValue, @Nullable Double oldValue) throws PropertyVetoException {
            if (newValue == null || newValue <= 0D) {
                throw new PropertyVetoException(Message.bundleMessage(INVALID_SCALE));
            }
            return newValue;
        }
    }

    public static final class EntryFee {

        @Comment({
                "The amount property specifies how much a player has to pay to enter this world.",
                "What the player has to pay is specified by the 'currency' property"
        })
        @Description(AMOUNT_KEY)
        private double amount = 0D;
        @Comment({
                "The currency property specifies what type of currency the player must pay (if any) to enter this world.",
                "Currency can be an economy money by specifying -1 or a block type by specifying the block id."
        })
        @Description(CURRENCY_KEY)
        private int currency = -1;

        private EntryFee() { }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCurrency() {
            return currency;
        }

        public void setCurrency(int currency) {
            this.currency = currency;
        }
    }

    public static final class Spawning {

        @Comment({"The animalTicks property specifies the rate in ticks at which animals are spawned."})
        @Description(ANIMAL_TICKS_KEY)
        private long animalTicks = -1L;
        @Comment({"The monsterTicks property specifies the rate in ticks at which monsters are spawned."})
        @Description(MONSTER_TICKS_KEY)
        private long monsterTicks = -1L;
        @Comment({"The animalLimit property specifies how many animal entities are allowed to be spawned per chunk."})
        @Description(ANIMAL_LIMIT_KEY)
        private int animalLimit = -1;
        @Comment({"The monsterLimit property specifies how many monster entities are allowed to be spawned per chunk."})
        @Description(MONSTER_LIMIT_KEY)
        private int monsterLimit = -1;
        @Comment({"The ambientLimit property specifies how many ambient entities are allowed to be spawned per chunk."})
        @Description(AMBIENT_LIMIT_KEY)
        private int ambientLimit = -1;
        @Comment({"The waterLimit property specifies how many water entities are allowed to be spawned per chunk."})
        @Description(WATER_LIMIT_KEY)
        private int waterLimit = -1;

        private Spawning() { }

        public long getAnimalTicks() {
            return animalTicks;
        }

        public void setAnimalTicks(long animalTicks) {
            this.animalTicks = animalTicks;
        }

        public long getMonsterTicks() {
            return monsterTicks;
        }

        public void setMonsterTicks(long monsterTicks) {
            this.monsterTicks = monsterTicks;
        }

        public int getAnimalLimit() {
            return animalLimit;
        }

        public void setAnimalLimit(int animalLimit) {
            this.animalLimit = animalLimit;
        }

        public int getMonsterLimit() {
            return monsterLimit;
        }

        public void setMonsterLimit(int monsterLimit) {
            this.monsterLimit = monsterLimit;
        }

        public int getAmbientLimit() {
            return ambientLimit;
        }

        public void setAmbientLimit(int ambientLimit) {
            this.ambientLimit = ambientLimit;
        }

        public int getWaterLimit() {
            return waterLimit;
        }

        public void setWaterLimit(int waterLimit) {
            this.waterLimit = waterLimit;
        }
    }

    private abstract class Proxy<T> extends DependentField<T, WorldLink> {

        private Proxy(T initialValue) {
            super(initialValue);
        }

        @Nullable
        @Override
        protected WorldLink getDependency() {
            return worldLink;
        }

        @Override
        protected T getDependentValue() {
            return getValue();
        }

        @Override
        protected void setDependentValue(@Nullable T t) {
            setValue(t);
        }

        protected abstract T getValue();

        protected abstract void setValue(T value);
    }

    private static class TimeHelper {
        private static final String TIME_REGEX = "(\\d\\d?):?(\\d\\d)(a|p)?m?";
        private static final Map<String, String> TIME_ALIASES;
        static {
            Map<String, String> staticTimes = new HashMap<String, String>();
            staticTimes.put("morning", "8:00");
            staticTimes.put("day", "12:00");
            staticTimes.put("noon", "12:00");
            staticTimes.put("midnight", "0:00");
            staticTimes.put("night", "20:00");

            // now set TIME_ALIASES to a "frozen" map
            TIME_ALIASES = Collections.unmodifiableMap(staticTimes);
        }

        public static String asString(final long from) {
            // I'm tired, so they get time in 24 hour for now.
            // Someone else can add 12 hr format if they want :P

            int hours = (int) ((from / 1000 + 8) % 24);
            int minutes = (int) (60 * (from % 1000) / 1000);

            return String.format("%d:%02d", hours, minutes);
        }

        public static long asLong(@NotNull String serialized) {
            if (TIME_ALIASES.containsKey(serialized.toLowerCase())) {
                serialized = TIME_ALIASES.get(serialized.toLowerCase());
            }
            // Regex that extracts a time in the following formats:
            // 11:11pm, 11:11, 23:11, 1111, 1111p, and the aliases at the top of this file.
            Pattern pattern = Pattern.compile(TIME_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(serialized);
            matcher.find();
            int hour = 0;
            double minute = 0;
            int count = matcher.groupCount();
            if (count >= 2) {
                hour = Integer.parseInt(matcher.group(1));
                minute = Integer.parseInt(matcher.group(2));
            }
            // If there were 4 matches (all, hour, min, am/pm)
            if (count == 4) {
                // We want 24 hour time for calcs, but if they
                // added a p[m], turn it into a 24 hr one.
                if (matcher.group(3).equals("p")) {
                    hour += 12;
                }
            }
            // Translate 24th hour to 0th hour.
            if (hour == 24) {
                hour = 0;
            }

            // The hour should be between 0-23.
            while (hour > 23) {
                hour -= 24;
            }
            while (hour < 0) {
                hour += 24;
            }
            // The minute should be between 0-59
            while (minute > 59) {
                minute -= 60;
            }
            while (minute < 0) {
                minute += 60;
            }
            // 60 seconds in a minute, time needs to be in hrs * 1000, per
            // the bukkit docs.
            double totaltime = (hour + (minute / 60.0)) * 1000;
            // Somehow there's an 8 hour offset...
            totaltime -= 8000;
            if (totaltime < 0) {
                totaltime = 24000 + totaltime;
            }

            return (long) totaltime;
        }
    }

    private static class FacingCoordinatesSerializer implements Serializer<FacingCoordinates> {
        @Nullable
        @Override
        public FacingCoordinates deserialize(@Nullable Object o, @NotNull Class<FacingCoordinates> clazz) {
            double x = 0D;
            double y = 0D;
            double z = 0D;
            float pitch = 0F;
            float yaw = 0F;
            if (o instanceof Map) {
                Map map = (Map) o;
                try {
                    x = Double.valueOf(map.get("x").toString());
                } catch (Exception ignore) { }
                try {
                    y = Double.valueOf(map.get("y").toString());
                } catch (Exception ignore) { }
                try {
                    z = Double.valueOf(map.get("z").toString());
                } catch (Exception ignore) { }
                try {
                    pitch = Float.valueOf(map.get("pitch").toString());
                } catch (Exception ignore) { }
                try {
                    yaw = Float.valueOf(map.get("yaw").toString());
                } catch (Exception ignore) { }
            } else {
                throw new IllegalArgumentException("Cannot deserialize coordinates from data: " + o);
            }
            return Locations.getFacingCoordinates(x, y, z, pitch, yaw);
        }

        @Nullable
        @Override
        public Object serialize(@Nullable final FacingCoordinates facingCoordinates) {
            if (facingCoordinates == null) {
                return Locations.NULL_FACING;
            }
            Map<String, Object> result = new LinkedHashMap<String, Object>(6);
            result.put("x", facingCoordinates.getX());
            result.put("y", facingCoordinates.getY());
            result.put("z", facingCoordinates.getZ());
            result.put("pitch", facingCoordinates.getPitch());
            result.put("yaw", facingCoordinates.getYaw());
            return result;
        }
    }

    private static class FacingCoordinatesPropertyHandler implements PropertyHandler {
        @Override
        public void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException {
            String[] locParts = newValue.split(",");
            if (locParts.length != 3) {
                throw new PropertyVetoException(Message.bundleMessage(Properties.VALID_SPAWN_STRING));
            }
            int x = 0, y = 0, z = 0;
            try {
                x = Integer.valueOf(locParts[0]);
                y = Integer.valueOf(locParts[1]);
                z = Integer.valueOf(locParts[2]);
            } catch (NumberFormatException e) {
                throw new PropertyVetoException(Message.bundleMessage(Properties.VALID_SPAWN_STRING));
            }
            field.setValue(Locations.getFacingCoordinates(x, y, z, 0, 0));
        }

        @Override
        public void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    private static class AliasPropertyHandler implements PropertyHandler {
        @Override
        public void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException {
            newValue = ChatColor.translateAlternateColorCodes('&', newValue);
            field.setValue(newValue);
        }

        @Override
        public void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException {
            field.setValue("");
        }
    }

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
}
