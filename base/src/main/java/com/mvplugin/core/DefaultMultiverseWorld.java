package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.mvplugin.core.minecraft.Difficulty;
import com.mvplugin.core.minecraft.GameMode;
import com.mvplugin.core.minecraft.PortalType;
import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import com.mvplugin.core.world.MultiverseWorld;
import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    DefaultMultiverseWorld(@NotNull final WorldProperties worldProperties, @NotNull final WorldLink worldLink) {
        this.worldProperties = worldProperties;
        this.worldLink = worldLink;
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean setTime(@NotNull final String timeAsString) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
    public boolean canAnimalsSpawn() {
        return getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.ANIMALS)
                .get(WorldProperties.Spawning.Animals.SPAWN);
    }

    @Override
    public void setAllowAnimalSpawn(final boolean allowAnimalSpawn) {
        getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.ANIMALS)
                .set(WorldProperties.Spawning.Animals.SPAWN, allowAnimalSpawn);
    }

    @NotNull
    @Override
    public List<String> getAnimalList() {
        return getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.ANIMALS)
                .get(WorldProperties.Spawning.Animals.EXCEPTIONS);
    }

    @Override
    public boolean canMonstersSpawn() {
        return getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.MONSTERS)
                .get(WorldProperties.Spawning.Monsters.SPAWN);
    }

    @Override
    public void setAllowMonsterSpawn(final boolean allowMonsterSpawn) {
        getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.MONSTERS)
                .set(WorldProperties.Spawning.Monsters.SPAWN, allowMonsterSpawn);
    }

    @NotNull
    @Override
    public List<String> getMonsterList() {
        return getProperties().get(WorldProperties.SPAWNING)
                .get(WorldProperties.Spawning.ANIMALS)
                .get(WorldProperties.Spawning.Monsters.EXCEPTIONS);
    }

    @Override
    public boolean isPVPEnabled() {
        return getProperties().get(WorldProperties.PVP);
    }

    @Override
    public void setPVPMode(final boolean pvpMode) {
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
}
