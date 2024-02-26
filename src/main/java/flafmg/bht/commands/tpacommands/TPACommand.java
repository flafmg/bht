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

public class TPACommand {

    //tpa <player>
    private final TPAManager tpaManager;
    private final ConfigManager configManager;

    public TPACommand(TPAManager tpaManager, ConfigManager configManager){
        this.tpaManager = tpaManager;
        this.configManager = configManager;
    }
    public void execute(Player sender, String[] args){

        if (args.length == 0 || sender.getName().equals(args[0])) {
            sender.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        Player target = Utils.getOnlinePlayerFromName(args[0]);
        if(target == null){
            sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestPlayerNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%player", args[0])));
            return;
        }

        tpaManager.newRequest(sender, target);
        sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestSent(), Map.of("%prefix%", configManager.getPrefix(), "%player", target.getName())));
        target.sendMessage(ChatUtils.formatMessage(configManager.getTeleportRequestReceived(), Map.of("%prefix%", configManager.getPrefix(), "%player", sender.getName())));

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
