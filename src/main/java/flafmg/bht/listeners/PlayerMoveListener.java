package flafmg.bht.listeners;

import flafmg.bht.util.ChatUtils;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.TeleportManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerMoveListener implements Listener {
    private final TeleportManager teleportManager;
    private final ConfigManager configManager;

    public PlayerMoveListener(TeleportManager teleportManager, ConfigManager configManager) {
        this.teleportManager = teleportManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (teleportManager.isPlayerPendingTeleport(player)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                teleportManager.cancelTeleportation(player);
                String cancelMessage = configManager.getTeleportCancelled();
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%prefix%", configManager.getPrefix());
                cancelMessage = ChatUtils.formatMessage(cancelMessage, placeholders);
                player.sendMessage(cancelMessage);
            }
        }
    }
}
