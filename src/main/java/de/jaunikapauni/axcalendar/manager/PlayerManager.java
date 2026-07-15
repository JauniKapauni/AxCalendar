package de.jaunikapauni.axcalendar.manager;

import de.jaunikapauni.axcalendar.AxCalendar;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class PlayerManager {

    AxCalendar reference;
    public PlayerManager(AxCalendar reference){
        this.reference = reference;
    }

    public boolean hasClaimedToday(UUID uuid){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT last FROM players WHERE uuid = ?")){
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    Date last = rs.getDate("last");
                    return last != null && last.equals(Date.valueOf(LocalDate.now()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void claim(UUID uuid){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("UPDATE players SET last = NOW() WHERE uuid = ?")){
                ps.setString(1, uuid.toString());
                int updated = ps.executeUpdate();
                if(updated == 0){
                    try(PreparedStatement ps1 = conn.prepareStatement("INSERT INTO players (uuid, last) VALUES (?, NOW())")){
                        ps1.setString(1, uuid.toString());
                        ps1.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
