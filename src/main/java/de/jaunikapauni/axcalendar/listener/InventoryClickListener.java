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
        if (!e.getView().getTitle().equals(reference.getConfig().getString("calendar.title"))) {
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
        UUID uuid = p.getUniqueId();
        String name = p.getName();
        if(!claiming.add(p.getUniqueId())){
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            try {
                boolean claimed = reference.getPlayerManager().hasClaimedToday(uuid);
                if (claimed) {
                    Bukkit.getScheduler().runTask(reference, () -> {
                        p.sendMessage("Sorry wait until the start of the next day!");
                    });
                    return;
                }
                int day = reference.getPlayerManager().getDay(uuid);
                reference.getPlayerManager().claim(uuid);
                Bukkit.getScheduler().runTask(reference, () -> {
                    String cmd = reference.getConfig().getString("calendar.days." + day + ".command");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", name));
                });
            } finally {
                claiming.remove(uuid);
            }
        });
    }
}

