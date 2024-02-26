package flafmg.bht.commands;

import flafmg.bht.commands.tpacommands.TPACancelCommand;
import flafmg.bht.commands.tpacommands.TPACommand;
import flafmg.bht.commands.tpacommands.TPAcceptCommand;
import flafmg.bht.managers.TPAManager;
import flafmg.bht.util.ChatUtils;
import flafmg.bht.commands.homecommands.DelHomeCommand;
import flafmg.bht.commands.homecommands.GoHomeCommand;
import flafmg.bht.commands.homecommands.ListHomesCommand;
import flafmg.bht.commands.homecommands.SetHomeCommand;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.HomeManager;
import flafmg.bht.managers.TeleportManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor, TabCompleter {
    private final HomeManager homeManager;
    private final TeleportManager teleportManager;
    private final TPAManager tpaManager;
    private final ConfigManager configManager;
    private final SetHomeCommand setHomeCommand;
    private final DelHomeCommand delHomeCommand;
    private final ListHomesCommand listHomesCommand;
    private final GoHomeCommand goHomeCommand;
    private final TPACommand tpaCommand;
    private final TPAcceptCommand tpAcceptCommand;
    private final TPACancelCommand tpaCancelCommand;
    private final Map<Player, Long> cooldowns;

    public Commands(HomeManager homeManager, TPAManager tpaManager, TeleportManager teleportManager, ConfigManager configManager) {
        this.homeManager = homeManager;
        this.tpaManager = tpaManager;
        this.teleportManager = teleportManager;
        this.configManager = configManager;

        this.setHomeCommand = new SetHomeCommand(homeManager, configManager);
        this.delHomeCommand = new DelHomeCommand(homeManager, configManager);
        this.listHomesCommand = new ListHomesCommand(homeManager, configManager);
        this.goHomeCommand = new GoHomeCommand(homeManager, teleportManager, configManager);

        this.tpaCommand = new TPACommand(tpaManager, configManager);
        this.tpAcceptCommand = new TPAcceptCommand(tpaManager, configManager);
        this.tpaCancelCommand = new TPACancelCommand(tpaManager, configManager);

        this.cooldowns = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }
        Player player = (Player) sender;

        if (cooldowns.containsKey(player)) {
            long secondsLeft = ((cooldowns.get(player) / 1000) + configManager.getTeleportCoolDown()) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                sender.sendMessage(ChatUtils.formatMessage(configManager.getTeleportWait(), Map.of("%prefix%", configManager.getPrefix(), "%time%", String.valueOf(secondsLeft))));
                return true;
            }
            cooldowns.remove(player);
        }

        switch (command.getName().toLowerCase()) {
            case "sethome":
                setHomeCommand.execute(player, args);
                break;
            case "delhome":
                delHomeCommand.execute(player, args);
                break;
            case "listhomes":
                listHomesCommand.execute(player, args);
                break;
            case "home":
                goHomeCommand.execute(player, args);
                break;
            case "tpa":
                tpaCommand.execute(player, args);
                break;
            case "tpaccept":
                tpAcceptCommand.execute(player, args);
                break;
            case "tpacancel":
                tpaCancelCommand.execute(player, args);
                break;
            default:
                return false;
        }

        cooldowns.put(player, System.currentTimeMillis());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("sethome")) {
            return setHomeCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("delhome")) {
            return delHomeCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("listhomes")) {
            return listHomesCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("home")) {
            return goHomeCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("tpa")){
            return  tpaCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("tpaccept")){
            return  tpAcceptCommand.onTabComplete(sender, args);
        } else if (command.getName().equalsIgnoreCase("tpacancel")){
            return  tpaCancelCommand.onTabComplete(sender, args);
        }

        return null;
    }
}
