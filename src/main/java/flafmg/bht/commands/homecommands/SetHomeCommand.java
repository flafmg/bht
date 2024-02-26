package flafmg.bht.commands.homecommands;

import flafmg.bht.managers.HomeManager;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.util.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetHomeCommand {
    private final HomeManager homeManager;
    private final ConfigManager configManager;

    public SetHomeCommand(HomeManager homeManager, ConfigManager configManager) {
        this.homeManager = homeManager;
        this.configManager = configManager;
    }

    public void execute(Player sender, String[] args) {
        if (args.length > 2 || args.length < 1) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        boolean isPublic = false;
        if (args.length == 2 && args[1].equalsIgnoreCase("public")) {
            isPublic = true;
        }

        if (!homeManager.addHome(sender, args[0], sender.getLocation(), isPublic)){
            sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeLimitReached(), Map.of("%prefix%", configManager.getPrefix(), "%player%", sender.getName(), "%home%", args[0])));
            return;
        }
        sender.sendMessage(ChatUtils.formatMessage(configManager.getHomeSet(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[0])));
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("public");
            completions.add("private");
        }

        return completions;
    }
}
