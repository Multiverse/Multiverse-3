package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.minecraft.GameMode;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Our base implementation of MultiverseWorld.
 *
 * This class shall implement as much as possible, leaving only server specific operations to the server
 * specific implementation.
 */
class DefaultMultiverseWorld implements MultiverseWorld {

    @NotNull
    private final WorldProperties worldProperties;
    @NotNull
    private final WorldLink worldLink;
    @NotNull
    private final Map<EntityType, SpawnException> worldSpawnExceptions = new HashMap<EntityType, SpawnException>();

    DefaultMultiverseWorld(@NotNull final WorldProperties worldProperties, @NotNull final WorldLink worldLink) {
        this.worldProperties = worldProperties;
        this.worldLink = worldLink;

        final List<SpawnException> spawnExceptions = this.worldProperties.get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.ALLOWED_SPAWNS)
                .get(WorldProperties.Spawning.AllowedSpawns.SPAWN_EXCEPTIONS);
        for (final SpawnException exception : spawnExceptions) {
            this.worldSpawnExceptions.put(exception.getEntityType(), exception);
        }
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MultiverseWorld && ((MultiverseWorld) obj).getName().equals(getName());
    }

    @NotNull
    @Override
    public String getName() {
        return this.worldLink.getName();
    }

    @NotNull
    @Override
    public UUID getWorldUID() {
        return this.worldLink.getUID();
    }

    @NotNull
    @Override
    public WorldType getWorldType() {
        return this.worldLink.getType();
    }

    @NotNull
    @Override
    public String getTime() {
        return TimeHelper.asString(this.worldLink.getTime());
    }

    @Override
    public boolean setTime(@NotNull final String timeAsString) {
        this.worldLink.setTime(TimeHelper.asLong(timeAsString));
        return true;
    }

    private static class TimeHelper {
        private static final String TIME_REGEX = "(\\d\\d?):?(\\d\\d)(a|p)?m?";
        private static final  Map<String, String> TIME_ALIASES;
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

    @NotNull
    @Override
    public Collection<BasePlayer> getPlayers() {
        return this.worldLink.getPlayers();
    }

    @NotNull
    @Override
    public WorldEnvironment getEnvironment() {
        return this.worldLink.getEnvironment();
    }

    @Override
    public void setEnvironment(@NotNull final WorldEnvironment environment) {
        getProperties().set(WorldProperties.ENVIRONMENT, environment);
    }

    @NotNull
    @Override
    public Difficulty getDifficulty() {
        return getProperties().get(WorldProperties.DIFFICULTY);
    }

    @Override
    public boolean setDifficulty(@NotNull final Difficulty difficulty) {
        // TODO Validate?
        this.worldLink.setDifficulty(difficulty);
        return getProperties().set(WorldProperties.DIFFICULTY, difficulty);
    }

    @Override
    public long getSeed() {
        return getProperties().get(WorldProperties.SEED);
    }

    @Override
    public void setSeed(long seed) {
        getProperties().set(WorldProperties.SEED, seed);
    }

    @Override
    public String getGenerator() {
        return getProperties().get(WorldProperties.GENERATOR);
    }

    @Override
    public void setGenerator(@Nullable final String generator) {
        getProperties().set(WorldProperties.GENERATOR, generator != null ? generator : "");
    }

    @NotNull
    @Override
    public WorldProperties getProperties() {
        return worldProperties;
    }

    @NotNull
    @Override
    public String getAllPropertyNames() {
        return null;
    }

    @NotNull
    @Override
    public String getAlias() {
        final String alias = getProperties().get(WorldProperties.ALIAS);
        return alias.isEmpty() ? getName() : alias;
    }

    @Override
    public void setAlias(@Nullable final String alias) {
        getProperties().set(WorldProperties.ALIAS, alias != null ? alias : "");
    }

    @Override
    public boolean isPVPEnabled() {
        return getProperties().get(WorldProperties.PVP);
    }

    @Override
    public void setPVPMode(final boolean pvpMode) {
        this.worldLink.setPVP(pvpMode);
        getProperties().set(WorldProperties.PVP, pvpMode);
    }

    @Override
    public boolean isHidden() {
        return getProperties().get(WorldProperties.HIDDEN);
    }

    @Override
    public void setHidden(final boolean hidden) {
        getProperties().set(WorldProperties.HIDDEN, hidden);
    }

    @Override
    public boolean getPrefixChat() {
        return getProperties().get(WorldProperties.PREFIX_CHAT);
    }

    @Override
    public void setPrefixChat(final boolean prefixChat) {
        getProperties().set(WorldProperties.PREFIX_CHAT, prefixChat);
    }

    @Override
    public boolean isWeatherEnabled() {
        return getProperties().get(WorldProperties.ALLOW_WEATHER);
    }

    @Override
    public void setEnableWeather(final boolean enableWeather) {
        this.worldLink.setEnableWeather(enableWeather);
        getProperties().set(WorldProperties.ALLOW_WEATHER, enableWeather);
    }

    @Override
    public boolean isKeepingSpawnInMemory() {
        return getProperties().get(WorldProperties.KEEP_SPAWN);
    }

    @Override
    public void setKeepSpawnInMemory(final boolean keepSpawnInMemory) {
        getProperties().set(WorldProperties.KEEP_SPAWN, keepSpawnInMemory);
    }

    @NotNull
    @Override
    public FacingCoordinates getSpawnLocation() {
        return getProperties().get(WorldProperties.SPAWN_LOCATION);
    }

    @Override
    public void setSpawnLocation(@NotNull final FacingCoordinates spawnLocation) {
        this.worldLink.setSpawnLocation(spawnLocation);
        getProperties().set(WorldProperties.SPAWN_LOCATION, spawnLocation);
    }

    @Override
    public boolean getHunger() {
        return getProperties().get(WorldProperties.HUNGER);
    }

    @Override
    public void setHunger(final boolean hungerEnabled) {
        getProperties().set(WorldProperties.HUNGER, hungerEnabled);
    }

    @NotNull
    @Override
    public GameMode getGameMode() {
        return getProperties().get(WorldProperties.GAME_MODE);
    }

    @Override
    public boolean setGameMode(@NotNull final GameMode gameMode) {

        // Todo validate?
        return getProperties().set(WorldProperties.GAME_MODE, gameMode);
    }

    @Override
    public double getPrice() {
        return getProperties().get(WorldProperties.ENTRY_FEE).get(WorldProperties.EntryFee.AMOUNT);
    }

    @Override
    public void setPrice(final double price) {
        getProperties().get(WorldProperties.ENTRY_FEE).set(WorldProperties.EntryFee.AMOUNT, price);
    }

    @Override
    public int getCurrency() {
        return getProperties().get(WorldProperties.ENTRY_FEE).get(WorldProperties.EntryFee.CURRENCY);
    }

    @Override
    public void setCurrency(final int item) {
        getProperties().get(WorldProperties.ENTRY_FEE).set(WorldProperties.EntryFee.CURRENCY, item);
    }

    @NotNull
    @Override
    public String getRespawnToWorld() {
        return getProperties().get(WorldProperties.RESPAWN_WORLD);
    }

    @Override
    public boolean setRespawnToWorld(@NotNull final String respawnWorld) {
        // TODO validation?
        return getProperties().set(WorldProperties.RESPAWN_WORLD, respawnWorld);
    }

    @Override
    public double getScaling() {
        return getProperties().get(WorldProperties.SCALE);
    }

    @Override
    public boolean setScaling(final double scaling) {
        // TODO validation?
        return getProperties().set(WorldProperties.SCALE, scaling);
    }

    @Override
    public boolean getAutoHeal() {
        return getProperties().get(WorldProperties.AUTO_HEAL);
    }

    @Override
    public void setAutoHeal(final boolean heal) {
        getProperties().set(WorldProperties.AUTO_HEAL, heal);
    }

    @Override
    public boolean getAdjustSpawn() {
        return getProperties().get(WorldProperties.ADJUST_SPAWN);
    }

    @Override
    public void setAdjustSpawn(final boolean adjust) {
        getProperties().set(WorldProperties.ADJUST_SPAWN, adjust);
    }

    @Override
    public boolean getAutoLoad() {
        return getProperties().get(WorldProperties.AUTO_LOAD);
    }

    @Override
    public void setAutoLoad(final boolean autoLoad) {
        getProperties().set(WorldProperties.AUTO_LOAD, autoLoad);
    }

    @Override
    public boolean getBedRespawn() {
        return getProperties().get(WorldProperties.BED_RESPAWN);
    }

    @Override
    public void setBedRespawn(final boolean bedRespawn) {
        getProperties().set(WorldProperties.BED_RESPAWN, bedRespawn);
    }

    @Override
    public void setPlayerLimit(final int limit) {
        getProperties().set(WorldProperties.PLAYER_LIMIT, limit);
    }

    @Override
    public int getPlayerLimit() {
        return getProperties().get(WorldProperties.PLAYER_LIMIT);
    }

    @Override
    public void allowPortalMaking(@NotNull final PortalType type) {
        getProperties().set(WorldProperties.PORTAL_FORM, type);
    }

    @NotNull
    @Override
    public PortalType getAllowedPortals() {
        return getProperties().get(WorldProperties.PORTAL_FORM);
    }

    @NotNull
    @Override
    public List<String> getWorldBlacklist() {
        return getProperties().get(WorldProperties.BLACK_LIST);
    }

    @Override
    public void save() throws PluginBaseException {
        getProperties().flush();
    }

    @Override
    public long getTicksPerAnimalSpawn() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTicksPerAnimalSpawn(final long ticks) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getTicksPerMonsterSpawn() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTicksPerMonsterSpawn(final long ticks) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAnimalSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMonsterSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAmbientSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setWaterAnimalSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPreventingSpawnsList() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPreventingSpawnsList(final boolean prevent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, SpawnException> getSpawnExceptions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addOrUpdateSpawnException(@NotNull final SpawnException spawnException) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeSpawnException(@NotNull final String creatureType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
