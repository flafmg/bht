package flafmg.bht.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class EffectsManager {
    private final Plugin plugin;
    private final Map<Player, BukkitTask> particleTasks;

    public EffectsManager(Plugin plugin) {
        this.plugin = plugin;
        this.particleTasks = new HashMap<>();
    }

    public void showTeleportParticles(Player player, Location destination) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancelParticleTask(player);
                    return;
                }

                playParticle(player.getLocation(), Particle.PORTAL);
                if (destination != null) {
                    playParticle(destination , Particle.PORTAL);
                }
            }
        }.runTaskTimer(plugin, 0L, 0L);

        particleTasks.put(player, task);
    }

    private void playParticle(Location location, Particle particle) {
        location.getWorld().spawnParticle(particle, location.getX(), location.getY() + 0.5, location.getZ(), 10, 0, 0.5, 0);
    }

    public void cancelParticleTask(Player player) {
        BukkitTask task = particleTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    public void playSmokeEffect(Player player, Location destination) {
        World destinationWorld = destination.getWorld();
        World playerWorld = player.getWorld();


        playerWorld.spawnParticle(Particle.CLOUD, player.getLocation().add(0,0.5,0), 50, 0.5, 0.5, 0.5, 0.2);
        destinationWorld.spawnParticle(Particle.CLOUD, destination, 50, 0.5, 0.5, 0.5, 0.2);
    }

}
