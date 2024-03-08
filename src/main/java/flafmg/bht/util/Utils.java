package flafmg.bht.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
    public static List<String> getPlayersWithName(String partialName) {
        List<String> matchingPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            if (playerName.startsWith(partialName)) {
                matchingPlayers.add(playerName);
            }
        }
        return matchingPlayers;
    }
}
