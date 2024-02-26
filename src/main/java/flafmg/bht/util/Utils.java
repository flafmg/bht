package flafmg.bht.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }
    public static Player getOnlinePlayerFromName(String playerName){
        for(Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }
}
