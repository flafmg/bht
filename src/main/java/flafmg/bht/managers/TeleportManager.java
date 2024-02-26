package flafmg.bht.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class TeleportManager {
    private final Plugin plugin;
    private EffectsManager effectsManager;
    private Map<Player, BukkitTask> teleportTasks;

    public TeleportManager(Plugin plugin, EffectsManager effectsManager) {
        this.plugin = plugin;
        this.effectsManager = effectsManager;
        this.teleportTasks = new HashMap<>();
    }

    public void addTeleportation(Player player, Location destination) {
        if(isPlayerPendingTeleport(player)){
            cancelTeleportation(player);
        }

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            effectsManager.playSmokeEffect(player, destination);
            player.teleport(destination);
            effectsManager.cancelParticleTask(player);
            teleportTasks.remove(player);
        }, 100L);
        teleportTasks.put(player, task);
        effectsManager.showTeleportParticles(player, destination);
    }

    public void cancelTeleportation(Player player) {
        BukkitTask task = teleportTasks.remove(player);
        if (task != null) {
            task.cancel();
            effectsManager.cancelParticleTask(player);
        }
        player.resetTitle();
    }

    public boolean isPlayerPendingTeleport(Player player) {
        return teleportTasks.containsKey(player);
    }
}
