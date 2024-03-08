package flafmg.bht.commands;

import flafmg.bht.Data.HomeData;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.HomeManager;
import flafmg.bht.managers.TeleportManager;
import flafmg.bht.util.ChatUtils;
import flafmg.bht.gui.GUIs;
import flafmg.bht.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeCommand implements CommandExecutor, TabCompleter {
    private Plugin plugin;
    private HomeManager homeManager;
    private TeleportManager teleportManager;
    private ConfigManager configManager;
    private GUIs guis;

    public HomeCommand(Plugin plugin, HomeManager homeManager, TeleportManager teleportManager, ConfigManager configManager, GUIs guis){
        this.plugin = plugin;
        this.homeManager = homeManager;
        this.teleportManager = teleportManager;
        this.configManager = configManager;
        this.guis = guis;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }
        Player player = (Player) sender;

        if(args.length != 0) {
            switch (args[0].toLowerCase()) {
                case "set":
                    handleSetCommand(player, args);
                    return true;
                case "del":
                    handleDelCommand(player, args);
                    return true;
                case "change":
                    handleChangeCommand(player, args);
                    return true;
                case "go":
                    handleHomeGoCommand(player, args);
                    return true;
            }
        }

        handleHomeCommand(player, args);
        return true;
    }
    private void handleHomeCommand(Player player, String[] args){
        if(args.length > 1){
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }
        String playerName = args.length == 1 ?  args[0] : player.getName();
        guis.openHomeGui(player, playerName, teleportManager, homeManager,1);
    }

    private void handleSetCommand(Player player, String[] args) {
        if(args.length < 2){
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }
        boolean isPublic = args.length == 3 && args[2].equalsIgnoreCase("public");

        if(!homeManager.addHome(player, args[1], player.getLocation(), isPublic)){
            player.sendMessage(ChatUtils.formatMessage(configManager.getHomeLimitReached(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[1], "%limit%", ""+configManager.getHomeLimit(player))));
            return;
        }

        player.sendMessage(ChatUtils.formatMessage(configManager.getHomeSet(), Map.of("%prefix%", configManager.getPrefix(),"%home%", args[1])));
    }

    private void handleDelCommand(Player player, String[] args) {
        if(args.length < 2){
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        if(!homeManager.removeHome(player.getName(), args[1])){
            player.sendMessage(ChatUtils.formatMessage(configManager.getHomeNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[1])));
            return;
        }
        player.sendMessage(ChatUtils.formatMessage(configManager.getHomeRemove(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[1])));

    }

    private void handleChangeCommand(Player player, String[] args) {
        if(args.length < 3 && (!args[2].equalsIgnoreCase("public") || !args[2].equalsIgnoreCase("private"))){
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }

        boolean isPublic = args[2].equalsIgnoreCase("public");
        if(!homeManager.setHomeStatus(player.getName(), args[1], isPublic)){
            player.sendMessage(ChatUtils.formatMessage(configManager.getHomeNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[1])));
        }
        player.sendMessage(ChatUtils.formatMessage(configManager.getHomeVisibilityChanged(), Map.of("%prefix%", configManager.getPrefix(), "%home%", args[1], "%status%", isPublic? configManager.getHomeVisibilityPublic() : configManager.getHomeVisibilityPrivate())));


    }
    private void handleHomeGoCommand(Player player, String[] args){
        if(args.length < 2){
            player.sendMessage(ChatUtils.formatMessage(configManager.getInvalidCommand(), Map.of("%prefix%", configManager.getPrefix())));
            return;
        }
        String playerName = args.length == 3 ?  args[1] : player.getName();
        String homeName = args.length == 3 ?  args[2] : args[1];

        HomeData homeData = homeManager.getHomeData(playerName, homeName);
        if(homeData == null || !(homeData.isPublic() || player.getName().equals(playerName) || player.hasPermission("bht.bypass-access"))){
            player.sendMessage(ChatUtils.formatMessage(configManager.getHomeNotFound(), Map.of("%prefix%", configManager.getPrefix(), "%home%", homeName)));
            return;
        }

        guis.openConfirmGui(player, homeData, teleportManager);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Add subcommands as tab completions
            completions.add("set");
            completions.add("del");
            completions.add("change");
            completions.add("go");

            completions.addAll(Utils.getPlayersWithName(args[0]));
        }


        switch (args[0]){
            case "set":
                return getSetTabCompletion(player, args);
            case "del":
                return getDelCompletion(player, args);
            case "change":
                return getChangeTabCompletion(player, args);
            case "go":
                return getHomeGoTabCompletion(player, args);


        }

        return completions;
    }

    private List<String> getHomeGoTabCompletion(Player player, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 2){
            completions.addAll(homeManager.getHomeNamesWithPartialName(player.getName(), args[1]));
            completions.addAll(Utils.getPlayersWithName(args[1]));
        }
        else if(args.length == 3){
           completions.addAll(homeManager.getHomeNamesWithPartialName(args[1],args[2]));
        }

        return completions;
    }
    private List<String> getSetTabCompletion(Player player, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 2){
            completions.add("<name>");
        }
        else if(args.length == 3){
            completions.add("public");
            completions.add("private");
        }

        return completions;
    }
    private List<String> getDelCompletion(Player player, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 2){
            completions.addAll(homeManager.getHomeNamesWithPartialName(player.getName(),args[1]));
        }
        return completions;
    }
    private List<String> getChangeTabCompletion(Player player, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 2){
            completions.addAll(homeManager.getHomeNamesWithPartialName(player.getName(), args[1]));
        }else if(args.length == 3){
            completions.add("public");
            completions.add("private");
        }
        return completions;
    }

    private List<String> getInfoTabCompletion(Player player, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 3){
            completions.addAll(homeManager.getHomeNamesWithPartialName(args[1],args[2]));
        }
        return completions;

    }
}
