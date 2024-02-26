package flafmg.bht.commands.tpacommands;

import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.TPAManager;
import flafmg.bht.util.ChatUtils;
import flafmg.bht.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TPAcceptCommand {
    private final TPAManager tpaManager;
    private final ConfigManager configManager;

    public TPAcceptCommand(TPAManager tpaManager, ConfigManager configManager){
        this.tpaManager = tpaManager;
        this.configManager = configManager;
    }

    public void execute(Player sender, String[] args){
        if (args.length > 1) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }
        if(!tpaManager.hasPendingRequest(sender)){
            sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", "'")));
            return;
        }

        if(args.length == 1){
            Player requester = Utils.getOnlinePlayerFromName(args[0]);
            if(requester == null){
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestPlayerNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                return;
            }
            if(!tpaManager.hasPendingRequest(sender, requester)){
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                return;
            }
            if(tpaManager.acceptRequest(sender, requester)){
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestAccepted(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                ChatUtils.sendTitle(requester, configManager.getTeleportingHomeTitle(), configManager.getTeleportingHomeSubtitle(), Map.of("%player%", sender.getName()));
            }else {
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
            }
        }else {
            if (tpaManager.acceptRequest(sender)) {
                List<Player> requesters = tpaManager.getRequesters(sender);
                Player lastRequester = requesters.get(requesters.size() - 1);
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestAccepted(), Map.of("%prefix%", configManager.getPrefix(), "%player%", lastRequester.getName())));
                ChatUtils.sendTitle(lastRequester, configManager.getTeleportingHomeTitle(), configManager.getTeleportingHomeSubtitle(), Map.of("%player%", sender.getName()));
            } else {
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args){
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
