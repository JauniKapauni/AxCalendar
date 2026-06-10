package de.jaunikapauni.axcalendar.command;

import de.jaunikapauni.axcalendar.AxCalendar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CalendarCommand implements CommandExecutor {
    AxCalendar reference;
    public CalendarCommand(AxCalendar reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this commmand!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axcalendar.calendar")){
            p.sendMessage("You don't have the permission!");
            return true;
        }
        reference.openCalendarGUI(p);
        return true;
    }
}
