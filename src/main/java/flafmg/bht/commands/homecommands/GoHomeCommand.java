package flafmg.bht.commands.homecommands;

import flafmg.bht.Data.HomeData;
import flafmg.bht.util.ChatUtils;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.HomeManager;
import flafmg.bht.managers.TeleportManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoHomeCommand {
    private final HomeManager homeManager;
    private final TeleportManager teleportManager;
    private final ConfigManager configManager;

    public GoHomeCommand(HomeManager homeManager, TeleportManager teleportManager, ConfigManager configManager) {
        this.homeManager = homeManager;
        this.teleportManager = teleportManager;
        this.configManager = configManager;
    }

    public void execute(Player sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        if (args.length == 1) {
            teleportToHome(sender, sender.getName(), args[0]);
        } else if (args.length == 2) {
            teleportToHome(sender, args[0], args[1]);
        }
    }

    private void teleportToHome(Player sender, String playerName, String homeName) {
        HomeData homeData = homeManager.getHomeData(playerName, homeName);
        if (homeData != null && (sender.hasPermission("bht.bypass-home-access") || homeData.isPublic() || sender.getName().equals(playerName))) {
            Location destination = homeData.getLocation();
            ChatUtils.sendTitle(sender, configManager.getTeleportingHomeTitle(), configManager.getTeleportingHomeSubtitle(), Map.of("%home%", homeName));
            teleportManager.addTeleportation(sender, destination);
        } else {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%home%", homeName)));
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (HomeData homeData : homeManager.getPlayerHomes(sender.getName())) {
                completions.add(homeData.getHomeName());
            }
            for (Player player : sender.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2) {
            for (HomeData homeData : homeManager.getPlayerHomes(args[0])) {
                if (!sender.hasPermission("bht.bypass-home-access") && !homeData.isPublic()) {
                    continue;
                }
                completions.add(homeData.getHomeName());
            }
        }

        return completions;
    }
}
