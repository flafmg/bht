package flafmg.bht.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.logging.Level;

public class ChatUtils {

    public static String formatMessage(String message, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            String value = entry.getValue();
            message = message.replace(placeholder, value);
        }
        return translateColor(message);
    }
    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendTitle(Player player, String title, String subtitle, Map<String, String> placeholders) {
        String formattedTitle = formatMessage(title, placeholders);
        String formattedSubtitle = formatMessage(subtitle, placeholders);
        player.sendTitle(formattedTitle, formattedSubtitle, 20,60,20);
    }
}
