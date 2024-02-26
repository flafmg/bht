package flafmg.bht.managers;

import flafmg.bht.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TPAManager {
    private final Plugin plugin;
    private final TeleportManager teleportManager;
    private final ConfigManager configManager;
    private HashMap<Player, List<Player>> tpaRequests = new HashMap<>();
    private HashMap<Player, BukkitTask> tpaTasks = new HashMap<>();

    public TPAManager(Plugin plugin, TeleportManager teleportManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.teleportManager = teleportManager;
        this.configManager = configManager;
    }

    public void newRequest(Player requester, Player target) {
        List<Player> requesters = tpaRequests.getOrDefault(target, new ArrayList<>());
        requesters.add(requester);
        tpaRequests.put(target, requesters);

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            List<Player> currentRequesters = tpaRequests.get(target);
            if (currentRequesters != null && currentRequesters.contains(requester)) {
                currentRequesters.remove(requester);
            }
            if (currentRequesters != null && currentRequesters.isEmpty()) {
                tpaRequests.remove(target);
            }
            tpaTasks.remove(target);
            requester.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestExpired(), Map.of("%prefix%", configManager.getPrefix(), "%player%", target.getName())));

        }, 20 * configManager.getTeleportRequestExpire());
        tpaTasks.put(target, task);
    }

    public boolean acceptRequest(Player player) {
        List<Player> requesters = tpaRequests.get(player);
        if (requesters != null && !requesters.isEmpty()) {
            Player requester = requesters.get(requesters.size() - 1);
            teleportManager.addTeleportation(requester, player.getLocation());
            requesters.remove(requester);
            if (requesters.isEmpty()) {
                tpaRequests.remove(player);
            }
            BukkitTask task = tpaTasks.remove(player);
            if (task != null) {
                task.cancel();
            }
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Player player, Player requester) {
        List<Player> requesters = tpaRequests.get(player);
        if (requesters != null && requesters.contains(requester)) {
            teleportManager.addTeleportation(requester, player.getLocation());
            requesters.remove(requester);
            if (requesters.isEmpty()) {
                tpaRequests.remove(player);
            }
            BukkitTask task = tpaTasks.remove(player);
            if (task != null) {
                task.cancel();
            }
            return true;
        }
        return false;
    }

    public boolean rejectRequest(Player target) {
        List<Player> requesters = tpaRequests.get(target);
        if (requesters != null && !requesters.isEmpty()) {
            Player requester = requesters.get(requesters.size() - 1);
            requesters.remove(requester);
            if (requesters.isEmpty()) {
                tpaRequests.remove(target);
            }
            BukkitTask task = tpaTasks.remove(target);
            if (task != null) {
                task.cancel();
            }
            return true;
        }
        return false;
    }

    public boolean hasPendingRequest(Player player) {
        return tpaRequests.containsKey(player) && !tpaRequests.get(player).isEmpty();
    }
    public boolean hasPendingRequest(Player sender, Player target) {
        List<Player> requesters = tpaRequests.get(target);
        return requesters != null && requesters.contains(sender);
    }


    public boolean rejectRequest(Player target, Player requester) {
        List<Player> requesters = tpaRequests.get(target);
        if (requesters != null && requesters.contains(requester)) {
            requesters.remove(requester);
            if (requesters.isEmpty()) {
                tpaRequests.remove(target);
            }
            BukkitTask task = tpaTasks.remove(target);
            if (task != null) {
                task.cancel();
            }
            return true;
        }
        return false;
    }

    public List<Player> getRequesters(Player target) {
        return tpaRequests.getOrDefault(target, new ArrayList<>());
    }
}
