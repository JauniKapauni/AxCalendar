package de.jaunikapauni.axcalendar;

import de.jaunikapauni.axcalendar.command.CalendarCommand;
import de.jaunikapauni.axcalendar.listener.InventoryClickListener;
import de.jaunikapauni.axcalendar.manager.DatabaseManager;
import de.jaunikapauni.axcalendar.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxCalendar extends JavaPlugin {
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    PlayerManager playerManager;
    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        try{
            databaseManager = new DatabaseManager(this);
            playerManager = new PlayerManager(this);
            if(databaseManager.initDatabaseTable1() == false){
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getCommand("calendar").setExecutor(new CalendarCommand(this));
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getLogger().info("");
        getLogger().info("----------------------------------------");
        getLogger().info("Name: " + getName());
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info(String.join("Authors: " + ", ", getDescription().getAuthors()));
        getLogger().info("----------------------------------------");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(databaseManager != null){
            databaseManager.close();
        }
    }

    public void openCalendarGUI(Player p){
        int day = playerManager.getDay(p.getUniqueId());
        if(!getConfig().contains("calendar.days." + day)){
            day = 1;
        }
        String path = "calendar.days." + day;
        Material material = Material.valueOf(getConfig().getString(path + ".material"));
        Inventory gui = Bukkit.createInventory(null, 9, getConfig().getString("calendar.title"));
        ItemStack reward = new ItemStack(material);
        ItemMeta meta = reward.getItemMeta();
        meta.setDisplayName(getConfig().getString(path + ".name"));
        reward.setItemMeta(meta);
        gui.setItem(0, reward);
        p.openInventory(gui);
    }
}
