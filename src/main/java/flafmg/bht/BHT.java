package flafmg.bht;

import flafmg.bht.commands.Commands;
import flafmg.bht.listeners.PlayerMoveListener;
import flafmg.bht.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class BHT extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);
        EffectsManager effectsManager = new EffectsManager(this);
        TeleportManager teleportManager = new TeleportManager(this, effectsManager);
        HomeManager homeManager = new HomeManager(this, configManager);
        TPAManager tpaManager = new TPAManager(this, teleportManager, configManager);
        Commands commands = new Commands(homeManager, tpaManager, teleportManager, configManager);

        getCommand("sethome").setExecutor(commands);
        getCommand("delhome").setExecutor(commands);
        getCommand("listhomes").setExecutor(commands);
        getCommand("home").setExecutor(commands);
        getCommand("tpa").setExecutor(commands);
        getCommand("tpaccept").setExecutor(commands);
        getCommand("tpacancel").setExecutor(commands);

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(teleportManager, configManager), this);
    }


}
