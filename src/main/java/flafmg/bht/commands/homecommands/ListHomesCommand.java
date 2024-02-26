package flafmg.bht.commands.homecommands;

import flafmg.bht.Data.HomeData;
import flafmg.bht.util.ChatUtils;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.HomeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListHomesCommand {
    private final HomeManager homeManager;
    private final ConfigManager configManager;

    public ListHomesCommand(HomeManager homeManager, ConfigManager configManager) {
        this.homeManager = homeManager;
        this.configManager = configManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        String targetPlayer = (args.length == 1) ? args[0] : sender.getName();

        List<HomeData> homes = homeManager.getPlayerHomes(targetPlayer);
        boolean hasPublic = sender.getName().equals(targetPlayer);

        for(HomeData homeData: homes){
            if(homeData.isPublic()){
                hasPublic = true;
                break;
            }
        }

        if (homes.isEmpty() || !hasPublic && !sender.hasPermission("bht.bypass-access")) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeListEmpty(), Map.of("%prefix%", configManager.getPrefix(), "%player%", targetPlayer)));
            return;
        }

        sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeListHeader(), Map.of("%prefix%", configManager.getPrefix(), "%player%", targetPlayer)));

        for (HomeData homeData : homes) {
            String location = homeData.getLocation().getBlockX() + ", " + homeData.getLocation().getBlockY() + ", " + homeData.getLocation().getBlockZ();
            String world = homeData.getLocation().getWorld().getName();
            String message = ChatUtils.formatMessage(configManager.getHomeListItem(), Map.of(
                    "%prefix%", configManager.getPrefix(),
                    "%home%", homeData.getHomeName(),
                    "%location%", location,
                    "%world%", world
            ));
            sender.sendMessage(message);
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : sender.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }
}
