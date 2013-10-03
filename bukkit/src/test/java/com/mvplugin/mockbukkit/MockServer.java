package com.mvplugin.mockbukkit;

import com.avaje.ebean.config.ServerConfig;
import com.mvplugin.core.FileLocations;
import com.mvplugin.mockbukkit.help.MockHelpMap;
import com.mvplugin.mockbukkit.plugin.MockPluginManager;
import com.mvplugin.mockbukkit.scheduler.MockBukkitScheduler;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scoreboard.ScoreboardManager;
import pluginbase.logging.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class MockServer implements Server {

    List<World> worlds = new ArrayList<World>();
    List<Player> onlinePlayers = new ArrayList<Player>();
    boolean allowEnd = true;
    boolean allowNether = true;

    GameMode defaultGameMode = GameMode.SURVIVAL;

    private int monsterSpawn = 70;
    private int animalSpawn = 15;
    private int waterAnimalSpawn = 5;
    private int ambientSpawn = 15;
    private int ticksPerMonsterSpawn = 1;
    private int ticksPerAnimalSpawn = 400;

    private MockPluginManager pluginManager = new MockPluginManager(this);
    private MockBukkitScheduler bukkitScheduler = new MockBukkitScheduler();
    private ServicesManager servicesManager = new SimpleServicesManager();
    private HelpMap helpMap = new MockHelpMap();

    public void loadDefaultWorlds() {
        createWorld(new WorldCreator("world").environment(Environment.NORMAL));
        createWorld(new WorldCreator("world_nether").environment(Environment.NETHER));
        createWorld(new WorldCreator("world_the_end").environment(Environment.THE_END));
    }

    @Override
    public String getName() {
        return "MockBukkit";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getBukkitVersion() {
        return "1.6.2-R1.0";
    }

    @Override
    public Player[] getOnlinePlayers() {
        return onlinePlayers.toArray(new Player[onlinePlayers.size()]);
    }

    @Override
    public int getMaxPlayers() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getPort() {
        return 25565;
    }

    @Override
    public int getViewDistance() {
        return 15;
    }

    @Override
    public String getIp() {
        return "127.0.0.1";
    }

    @Override
    public String getServerName() {
        return "MockBukkit";
    }

    @Override
    public String getServerId() {
        return "1";
    }

    @Override
    public String getWorldType() {
        return "DEFAULT";
    }

    @Override
    public boolean getGenerateStructures() {
        return true;
    }

    public void setAllowEnd(boolean allowEnd) {
        this.allowEnd = allowEnd;
    }

    @Override
    public boolean getAllowEnd() {
        return allowEnd;
    }

    public void setAllowNether(boolean allowNether) {
        this.allowNether = allowNether;
    }

    @Override
    public boolean getAllowNether() {
        return allowNether;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setWhitelist(boolean b) { }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return new HashSet<OfflinePlayer>(0);
    }

    @Override
    public void reloadWhitelist() { }

    @Override
    public int broadcastMessage(String s) {
        for (Player player : onlinePlayers) {
            player.sendMessage(s);
        }
        return onlinePlayers.size();
    }

    @Override
    public String getUpdateFolder() {
        return FileLocations.UPDATES_DIRECTORY.toString();
    }

    @Override
    public File getUpdateFolderFile() {
        return FileLocations.UPDATES_DIRECTORY;
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    public void setTicksPerMonsterSpawn(int ticksPerMonsterSpawn) {
        this.ticksPerMonsterSpawn = ticksPerMonsterSpawn;
    }

    public void setTicksPerAnimalSpawn(int ticksPerAnimalSpawn) {
        this.ticksPerAnimalSpawn = ticksPerAnimalSpawn;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return ticksPerAnimalSpawn;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return ticksPerMonsterSpawn;
    }

    @Override
    public Player getPlayer(String s) {
        s = s.toLowerCase();
        for (Player player : onlinePlayers) {
            if (player.getName().toLowerCase().startsWith(s)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public Player getPlayerExact(String s) {
        for (Player player : onlinePlayers) {
            if (player.getName().equals(s)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Player> matchPlayer(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MockPluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public MockBukkitScheduler getScheduler() {
        return bukkitScheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        return worlds;
    }

    @Override
    public World createWorld(WorldCreator worldCreator) {
        World world = new MockWorld(worldCreator);
        worlds.add(world);
        return world;
    }

    @Override
    public boolean unloadWorld(String s, boolean b) {
        World world = getWorld(s);
        return unloadWorld(world, b);
    }

    @Override
    public boolean unloadWorld(World world, boolean b) {
        if (world != null) {
            return worlds.remove(world);
        }
        return false;
    }

    @Override
    public World getWorld(String s) {
        for (World w : worlds) {
            if (w.getName().equals(s)) {
                return w;
            }
        }
        return null;
    }

    @Override
    public World getWorld(UUID uuid) {
        for (World w : worlds) {
            if (w.getUID().equals(uuid)) {
                return w;
            }
        }
        return null;
    }

    @Override
    public MapView getMap(short i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MapView createMap(World world) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reload() {
        // TODO gonna need this
    }

    @Override
    public Logger getLogger() {
        return Logging.getLogger();
    }

    @Override
    public PluginCommand getPluginCommand(String s) {
        //PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class)
        return null;
    }

    @Override
    public void savePlayers() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean dispatchCommand(CommandSender commandSender, String s) throws CommandException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void configureDbConfig(ServerConfig serverConfig) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack itemStack) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearRecipes() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resetRecipes() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getSpawnRadius() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSpawnRadius(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getOnlineMode() {
        return true;
    }

    @Override
    public boolean getAllowFlight() {
        return true;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public boolean useExactLoginLocation() {
        return true;
    }

    @Override
    public void shutdown() {
        // TODO probably implement
    }

    @Override
    public int broadcast(String s, String s2) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return new HashSet<String>();
    }

    @Override
    public void banIP(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unbanIP(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return new HashSet<OfflinePlayer>();
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return new HashSet<OfflinePlayer>();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    @Override
    public void setDefaultGameMode(GameMode gameMode) {
        defaultGameMode = gameMode;
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getWorldContainer() {
        return FileLocations.WORLDS_DIRECTORY;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @Override
    public Messenger getMessenger() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HelpMap getHelpMap() {
        return helpMap;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i, String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMonsterSpawnLimit() {
        return monsterSpawn;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return animalSpawn;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return waterAnimalSpawn;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return ambientSpawn;
    }

    @Override
    public boolean isPrimaryThread() {
        return true;
    }

    @Override
    public String getMotd() {
        return "Hello, world";
    }

    @Override
    public String getShutdownMessage() {
        return "Server shutting down...";
    }

    @Override
    public WarningState getWarningState() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
