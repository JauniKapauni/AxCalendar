package de.jaunikapauni.axcalendar.listener;

import de.jaunikapauni.axcalendar.AxCalendar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.time.LocalDate;

public class InventoryClickListener implements Listener {
    AxCalendar reference;

    public InventoryClickListener(AxCalendar reference) {
        this.reference = reference;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("Calendar")) {
            return;
        }
        e.setCancelled(true);
        if (e.getSlot() != 0) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        boolean claimed = reference.getPlayerManager().hasClaimedToday(p.getUniqueId());
        if (claimed) {
            p.sendMessage("Sorry wait until the start of the next day!");
            return;
        }
        reference.getPlayerManager().claim(p.getUniqueId());
        String cmd = reference.getConfig().getString("calendar.day1.command");
        p.performCommand(cmd);
    }
}

