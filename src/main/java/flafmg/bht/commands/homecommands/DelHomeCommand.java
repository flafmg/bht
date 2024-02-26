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

public class DelHomeCommand {
    private final HomeManager homeManager;
    private final ConfigManager configManager;

    public DelHomeCommand(HomeManager homeManager, ConfigManager configManager) {
        this.homeManager = homeManager;
        this.configManager = configManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        if (args.length == 1) {
            deleteHome(sender, sender.getName(), args[0]);
        } else {
            if (sender.hasPermission("bht.admin")) {
                deleteHome(sender, args[0], args[1]);
            } else {
                sender.sendMessage(ChatUtils.formatMessage("&cno permission", null));
            }
        }
    }

    private void deleteHome(CommandSender sender, String playerName, String homeName) {
        if (homeManager.removeHome(playerName, homeName)) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeRemove(), Map.of("%prefix%", configManager.getPrefix(), "%home%", homeName)));
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
            if(sender.hasPermission("bht.admin")){
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2 && sender.hasPermission("bht.admin")) {
             for(HomeData homeData : homeManager.getPlayerHomes(args[0])){
                 completions.add(homeData.getHomeName());
             }
        }

        return completions;
    }
}
