package de.jaunikapauni.axcalendar;

import de.jaunikapauni.axcalendar.command.CalendarCommand;
import de.jaunikapauni.axcalendar.listener.InventoryClickListener;
import de.jaunikapauni.axcalendar.manager.DatabaseManager;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        try{
            databaseManager = new DatabaseManager(this);
            if(databaseManager.initDatabaseTable1() == false){
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getCommand("calendar").setExecutor(new CalendarCommand(this));
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void openCalendarGUI(Player p){
        Inventory gui = Bukkit.createInventory(null, 9, "Calendar");
        ItemStack reward = new ItemStack(Material.COAL_BLOCK);
        ItemMeta meta = reward.getItemMeta();
        meta.setDisplayName(getConfig().getString("calendar.day1.name"));
        reward.setItemMeta(meta);
        gui.setItem(0, reward);
        p.openInventory(gui);
    }
}
