package de.jaunikapauni.axcalendar;

import de.jaunikapauni.axcalendar.command.CalendarCommand;
import de.jaunikapauni.axcalendar.listener.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxCalendar extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getCommand("calendar").setExecutor(new CalendarCommand(this));
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void openCalendarGUI(Player p){
        Inventory gui = Bukkit.createInventory(null, 9, "Inventory");
        ItemStack reward = new ItemStack(Material.COAL_BLOCK);
        ItemMeta meta = reward.getItemMeta();
        meta.setDisplayName(getConfig().getString("calendar.day1.name"));
        reward.setItemMeta(meta);
        gui.setItem(0, reward);
        p.openInventory(gui);
    }
}
