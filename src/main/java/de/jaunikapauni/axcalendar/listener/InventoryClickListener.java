package de.jaunikapauni.axcalendar.listener;

import de.jaunikapauni.axcalendar.AxCalendar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    AxCalendar reference;
    public InventoryClickListener(AxCalendar reference){
        this.reference = reference;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Calendar")){
            e.setCancelled(true);
            if(e.getCurrentItem() != null){
                Player p = (Player) e.getWhoClicked();
                ItemStack reward = e.getInventory().getItem(0);
                if(reward != null){
                    String cmd = reference.getConfig().getString("calendar.day1.command");
                    p.performCommand(cmd);
                }
            }
        }
    }
}
