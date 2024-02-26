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

public class TPACancelCommand {
    private final TPAManager tpaManager;
    private final ConfigManager configManager;

    public TPACancelCommand(TPAManager tpaManager, ConfigManager configManager){
        this.tpaManager = tpaManager;
        this.configManager = configManager;
    }

    public void execute(Player player, String[] args){
        if (args.length > 1) {
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        if(!tpaManager.hasPendingRequest(player)){
            player.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", "''")));
            return;
        }

        if(args.length == 1){
            Player target = Utils.getOnlinePlayerFromName(args[0]);
            if(target == null){
                player.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestPlayerNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                return;
            }
            if(!tpaManager.hasPendingRequest(player, target)){
                player.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                return;
            }

            if(tpaManager.rejectRequest(player, target)){
                player.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestDenied(), Map.of("%prefix%", configManager.getPrefix(), "%player%", args[0])));
                return;
            }
        }else{
            if(tpaManager.rejectRequest(player)){
                player.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestDenied(), Map.of("%prefix%", configManager.getPrefix(), "%player%", tpaManager.getRequesters(player).get(tpaManager.getRequesters(player).size()-1).getName())));
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
