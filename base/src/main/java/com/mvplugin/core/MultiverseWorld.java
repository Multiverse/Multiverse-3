package com.mvplugin.core;

import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.EntityType;
import com.mvplugin.core.minecraft.GameMode;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MultiverseWorld {

    @NotNull
    private final WorldProperties properties;
    @NotNull
    private final WorldLink worldLink;
    @NotNull
    private final Map<EntityType, SpawnException> worldSpawnExceptions = new HashMap<EntityType, SpawnException>();

    MultiverseWorld(@NotNull final WorldProperties worldProperties, @NotNull final WorldLink worldLink) {
        this.properties = worldProperties;
        this.worldLink = worldLink;
        getProperties().linkToWorld(worldLink);

        final List<SpawnException> spawnExceptions = this.getProperties().getSpawning().getAllowedSpawns().getSpawnExceptions();
        for (final SpawnException exception : spawnExceptions) {
            this.worldSpawnExceptions.put(exception.getEntityType(), exception);
        }
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(final Object obj) {
        return obj instanceof MultiverseWorld && ((MultiverseWorld) obj).getName().equals(getName());
    }

    @NotNull
    WorldProperties getProperties() {
        return properties;
    }

    /**
     * Gets the name of this world.  The name cannot be changed.
     * <p>
     * Note for plugin developers: Usually {@link #getAlias()} is what you want to use instead of this method.
     *
     * @return The name of the world as a String.
     */
    @NotNull
    public String getName() {
        return getProperties().getName();
    }

    /**
     * Gets the UUID for this world.
     *
     * This is what Minecraft uses to keep tracks of worlds.
     *
     * @return The UUID for this world.
     */
    @NotNull
    public UUID getWorldUID() {
        return getProperties().getUUID();
    }

    @NotNull
    public WorldType getWorldType() {
        return getProperties().getType();
    }

    @NotNull
    public String getTime() {
        return getProperties().getTime();
    }

    public void setTime(@NotNull final String timeAsString) {
        getProperties().setTime(timeAsString);
    }

    @NotNull
    public Collection<BasePlayer> getPlayers() {
        return this.worldLink.getPlayers();
    }

    @NotNull
    public WorldEnvironment getEnvironment() {
        return getProperties().getEnvironment();
    }

    public void setEnvironment(@NotNull final WorldEnvironment environment) {
        getProperties().setEnvironment(environment);
}

    @NotNull
    public Difficulty getDifficulty() {
        return getProperties().getDifficulty();
    }

    public void setDifficulty(@NotNull final Difficulty difficulty) {
        // TODO Validate?
        getProperties().setDifficulty(difficulty);
    }

    public long getSeed() {
        return getProperties().getSeed();
    }

    public void setSeed(long seed) {
        getProperties().setSeed(seed);
    }

    public String getGenerator() {
        return getProperties().getGenerator();
    }

    public void setGenerator(@Nullable final String generator) {
        getProperties().setGenerator(generator == null ? "" : generator);
    }

    @NotNull
    public static String[] getAllPropertyNames() {
        return new PropertyNameExtractor(WorldProperties.class).extractPropertyNames();
    }

    @Nullable
    public static String getPropertyDescriptionKey(String... name) throws NoSuchFieldException {
        Field field = getField(name);
        return field.getDescription();
    }

    private static Field getField(String... name) throws NoSuchFieldException {
        FieldMap fieldMap = FieldMapper.getFieldMap(WorldProperties.class);
        Field field = null;
        for (String s : name) {
            field = fieldMap.getField(s);
            if (field != null) {
                fieldMap = field;
            } else {
                break;
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("No such property");
        }
        return field;
    }

    @NotNull
    public String getAlias() {
        String alias = getProperties().getAlias();
        return alias.isEmpty() ? getName() : alias;
    }

    public void setAlias(@Nullable final String alias) {
        getProperties().setAlias(alias == null ? "" : alias);
    }

    public boolean isPVPEnabled() {
        return getProperties().isPVPEnabled();
    }

    public void setPVPEnabled(final boolean pvp) {
        getProperties().setPVPEnabled(pvp);
    }

    public boolean isHidden() {
        return getProperties().isHidden();
    }

    public void setHidden(final boolean hidden) {
        getProperties().setHidden(hidden);
    }

    public boolean getPrefixChat() {
        return getProperties().isPrefixChat();
    }

    public void setPrefixChat(final boolean prefixChat) {
        getProperties().setPrefixChat(prefixChat);
    }

    public boolean isWeatherEnabled() {
        return getProperties().isAllowWeather();
    }

    public void setEnableWeather(final boolean enableWeather) {
        getProperties().setAllowWeather(enableWeather);
    }


    public boolean isKeepingSpawnInMemory() {
        return getProperties().isKeepSpawnInMemory();
    }

    public void setKeepSpawnInMemory(final boolean keepSpawnInMemory) {
        getProperties().setKeepSpawnInMemory(keepSpawnInMemory);
    }

    @NotNull
    public FacingCoordinates getSpawnLocation() {
        return getProperties().getSpawnLocation();
    }

    public void setSpawnLocation(@Nullable final FacingCoordinates spawnLocation) {
        getProperties().setSpawnLocation(spawnLocation != null ? spawnLocation : Locations.NULL_FACING);
    }

    public boolean getHunger() {
        return getProperties().isHunger();
    }

    public void setHunger(final boolean hungerEnabled) {
        getProperties().setHunger(hungerEnabled);
    }

    @NotNull
    public GameMode getGameMode() {
        return getProperties().getGameMode();
    }

    public void setGameMode(@NotNull final GameMode gameMode) {
        // Todo validate?
        getProperties().setGameMode(gameMode);
    }

    public double getPrice() {
        return getProperties().getEntryFee().getAmount();
    }

    public void setPrice(final double price) {
        getProperties().getEntryFee().setAmount(price);
    }

    public int getCurrency() {
        return getProperties().getEntryFee().getCurrency();
    }

    public void setCurrency(final int item) {
        getProperties().getEntryFee().setCurrency(item);
    }

    @NotNull
    public String getRespawnToWorld() {
        return getProperties().getRespawnWorld();
    }

    public void setRespawnToWorld(@Nullable final String respawnWorld) {
        getProperties().setRespawnWorld(respawnWorld == null ? "" : respawnWorld);
    }

    public double getScale() {
        return getProperties().getScale();
    }

    public void setScale(final double scale) {
        // TODO validation?
        getProperties().setScale(scale);
    }

    public boolean getAutoHeal() {
        return getProperties().isAutoHeal();
    }

    public void setAutoHeal(final boolean heal) {
        getProperties().setAutoHeal(heal);
    }

    public boolean getAdjustSpawn() {
        return getProperties().isAdjustSpawn();
    }

    public void setAdjustSpawn(final boolean adjust) {
        getProperties().setAdjustSpawn(adjust);
    }

    public boolean getAutoLoad() {
        return getProperties().isAutoLoad();
    }

    public void setAutoLoad(final boolean autoLoad) {
        getProperties().setAutoLoad(autoLoad);
    }

    public boolean getBedRespawn() {
        return getProperties().isBedRespawn();
    }

    public void setBedRespawn(final boolean bedRespawn) {
        getProperties().setBedRespawn(bedRespawn);
    }

    public void setPlayerLimit(final int limit) {
        getProperties().setPlayerLimit(limit);
    }

    public int getPlayerLimit() {
        return getProperties().getPlayerLimit();
    }

    public void allowPortalMaking(@NotNull final PortalType type) {
        getProperties().setPortalForm(type);
    }

    @NotNull
    public PortalType getAllowedPortals() {
        return getProperties().getPortalForm();
    }

    @NotNull
    public List<String> getWorldBlacklist() {
        return getProperties().getWorldBlackList();
    }

    public long getTicksPerAnimalSpawn() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setTicksPerAnimalSpawn(final long ticks) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public long getTicksPerMonsterSpawn() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setTicksPerMonsterSpawn(final long ticks) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getAnimalSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAnimalSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getMonsterSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setMonsterSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getAmbientSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAmbientSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getWaterAnimalSpawnLimit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setWaterAnimalSpawnLimit(final int limit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isPreventingSpawnsList() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPreventingSpawnsList(final boolean prevent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, SpawnException> getSpawnExceptions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addOrUpdateSpawnException(@NotNull final SpawnException spawnException) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeSpawnException(@NotNull final String creatureType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nullable
    public Object getProperty(@NotNull final String... name) throws NoSuchFieldException, IllegalArgumentException {
        return properties.getProperty(name);
    }

    public void setProperty(@Nullable final Object value, @NotNull final String... name) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        properties.setProperty(value, name);
    }

    public void setProperty(@NotNull String propertyName, @NotNull String value) throws MultiverseException {
        // TODO
    }

    @Nullable
    public Object getPropertyUnchecked(@NotNull final String... name) throws IllegalArgumentException {
        return properties.getPropertyUnchecked(name);
    }

    public boolean setPropertyUnchecked(@Nullable final Object value, @NotNull final String... name) throws IllegalArgumentException {
        return properties.setPropertyUnchecked(value, name);
    }

    private static class PropertyNameExtractor {
        Class clazz;
        List<String> allProperties;
        Deque<String> currentPropertyParents;

        PropertyNameExtractor(Class clazz) {
            this.clazz = clazz;
        }

        public String[] extractPropertyNames() {
            prepareBuffers();
            appendNamesFromFieldMap(FieldMapper.getFieldMap(clazz));
            return allProperties.toArray(new String[allProperties.size()]);
        }

        private void prepareBuffers() {
            allProperties = new LinkedList<String>();
            currentPropertyParents = new LinkedList<String>();
        }

        private void appendNamesFromFieldMap(FieldMap fieldMap) {
            for (Field field : fieldMap) {
                String fieldName = field.getName();
                if (field.hasChildFields()) {
                    currentPropertyParents.add(fieldName);
                    appendNamesFromFieldMap(field);
                    currentPropertyParents.pollLast();
                } else {
                    if (!field.isImmutable()) {
                        appendPropertyName(fieldName);
                    }
                }
            }
        }

        private void appendPropertyName(String name) {
            StringBuilder buffer = new StringBuilder();
            for (String parent : currentPropertyParents) {
                buffer.append(parent);
                buffer.append('.');
            }
            buffer.append(name);
            allProperties.add(buffer.toString());
        }
    }
}
