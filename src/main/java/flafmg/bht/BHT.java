package flafmg.bht;

import flafmg.bht.commands.HomeCommand;
import flafmg.bht.gui.GUIs;
import flafmg.bht.listeners.PlayerMoveListener;
import flafmg.bht.managers.*;
import flafmg.bht.util.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class BHT extends JavaPlugin {
    private ConfigManager configManager;
    private EffectsManager effectsManager;
    private TeleportManager teleportManager;
    private HomeManager homeManager;
    private TPAManager tpaManager;
    private GUIs guis;
    @Override
    public void onEnable() {
        ChatUtils.log("&6[bht] &rBHT initializing..");
        loadSystems();
        loadCommands();
        loadEvents();

        ChatUtils.log("&6[bht] &rBHT initialized &asuccessfully");

        showCoolText();
    }

    public void loadSystems(){
        configManager = new ConfigManager(this);
        effectsManager = new EffectsManager(this);
        teleportManager = new TeleportManager(this, effectsManager, configManager);
        homeManager = new HomeManager(this, configManager);
        tpaManager = new TPAManager(this, teleportManager, configManager);
        guis = new GUIs(configManager, this);
    }
    public void loadCommands(){
        getCommand("home").setExecutor(new HomeCommand(this, homeManager, teleportManager, configManager, guis));
    }
    public void loadEvents(){
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(teleportManager, configManager), this);
    }

    //nice function name heh
    public void showCoolText(){
        String coolText =
                "            &c&l ____       _   _                            \n" +
                "            &c&l|  _ \\     | | | |                           \n" +
                "            &c&l| |_) | ___| |_| |_ ___ _ __                 \n" +
                "            &c&l|  _ < / _ \\ __| __/ _ \\ '__|                \n" +
                " &6&l _    _    &c&l| |_) |  __/ |_| ||  __/ |   &6&l_______ _____  \n" +
                " &6&l| |  | |   &c&l|____/ \\___|\\__|\\__\\___|_|  &6&l|__   __|  __ \\ \n" +
                " &6&l| |__| | ___  _ __ ___   ___   ( _ )      | |  | |__) |\n" +
                " &6&l|  __  |/ _ \\| '_ ` _ \\ / _ \\  / _ \\/\\    | |  |  ___/ \n" +
                " &6&l| |  | | (_) | | | | | |  __/ | (_>  <    | |  | |     \n" +
                " &6&l|_|  |_|\\___/|_| |_| |_|\\___|  \\___/\\/    |_|  |_|     \n" +
                "                                                        \n" +
                "  &fVersion: &60.4 &lBETA &r| &fmade by &6flafmg &4<3\n"+
                "&c /!\\ THIS IS A BETA VERSION, NOT INTENDED FOR COMERCIAL USE /!\\";

        List<String> lines = List.of(coolText.split("\n"));
        for (String line : lines){
            ChatUtils.log(line);
        }

    }
}
