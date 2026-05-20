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
        Player p = (Player) sender;
        reference.openCalendarGUI(p);
        return true;
    }
}
