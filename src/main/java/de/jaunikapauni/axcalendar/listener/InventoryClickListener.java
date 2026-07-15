package de.jaunikapauni.axcalendar.listener;

import de.jaunikapauni.axcalendar.AxCalendar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryClickListener implements Listener {
    AxCalendar reference;

    public InventoryClickListener(AxCalendar reference) {
        this.reference = reference;
    }
    Set<UUID> claiming = ConcurrentHashMap.newKeySet();

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
        if(!claiming.add(p.getUniqueId())){
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            boolean claimed = reference.getPlayerManager().hasClaimedToday(p.getUniqueId());
            if (claimed) {
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("Sorry wait until the start of the next day!");
                });
                return;
            }
            reference.getPlayerManager().claim(p.getUniqueId());
            Bukkit.getScheduler().runTask(reference, () -> {
                String cmd = reference.getConfig().getString("calendar.day1.command");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
            });
        });
        claiming.remove(p.getUniqueId());
    }
}

