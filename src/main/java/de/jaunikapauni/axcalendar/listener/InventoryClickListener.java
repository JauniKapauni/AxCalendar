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
    public InventoryClickListener(AxCalendar reference){
        this.reference = reference;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Calendar")){
            e.setCancelled(true);
            if(e.getCurrentItem() != null){
                if(e.getSlot() != 0){
                    return;
                }
                if(!(e.getWhoClicked() instanceof Player)){
                    return;
                }
                Player p = (Player) e.getWhoClicked();
                ItemStack reward = e.getCurrentItem();
                if(reward != null){
                    Date last = null;
                    reference.getPlayerManager().hasClaimedToday(p.getUniqueId());
                    if(last == null){
                        try(Connection conn = reference.getDatabaseManager().getConnection()){
                            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO players (uuid, last) VALUES (?, NOW())")){
                                ps.setString(1, p.getUniqueId().toString());
                                ps.executeUpdate();
                                String cmd = reference.getConfig().getString("calendar.day1.command");
                                p.performCommand(cmd);
                                return;
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    Date now = Date.valueOf(LocalDate.now());
                    if(last != null && last.equals(now)){
                        p.sendMessage("Sorry wait until the start of the next day!");
                    } else {
                        String cmd = reference.getConfig().getString("calendar.day1.command");
                        p.performCommand(cmd);
                        try(Connection conn2 = reference.getDatabaseManager().getConnection()){
                            try(PreparedStatement ps2 = conn2.prepareStatement("UPDATE players SET last = NOW() WHERE uuid = ?")){
                                ps2.setString(1, p.getUniqueId().toString());
                                ps2.executeUpdate();
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
    }
}
