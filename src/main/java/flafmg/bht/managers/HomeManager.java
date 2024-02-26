package flafmg.bht.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import flafmg.bht.Data.HomeData;
import flafmg.bht.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeManager {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private Map<String, List<HomeData>> playerHomes; // playerUUID : List<HomeData>

    public HomeManager(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        load();
    }

    public boolean addHome(Player player, String homeName, Location location, boolean status) {
        String playerName = player.getName();
        List<HomeData> homes = playerHomes.computeIfAbsent(playerName, h -> new ArrayList<>());

        homes.removeIf(home -> home.getHomeName().equalsIgnoreCase(homeName));

        if(homes.size() >= configManager.getHomeLimit(player)){
            return false;
        }

        homes.add(new HomeData(homeName, location, status));
        save();
        return true;
    }

    public boolean removeHome(String playerName, String homeName) {
        List<HomeData> homes = playerHomes.get(playerName);
        if (homes != null) {
            homes.removeIf(home -> home.getHomeName().equalsIgnoreCase(homeName));
            save();
            return true;
        }
        return false;
    }

    public boolean setHomeStatus(String playerName, String homeName, boolean status) {
        List<HomeData> homes = playerHomes.get(playerName);
        if (homes != null) {
            for (HomeData home : homes) {
                if (home.getHomeName().equalsIgnoreCase(homeName)) {
                    home.changeStatus(status);
                    save();
                    return true;
                }
            }
        }
        return false;
    }

    public HomeData getHomeData(String playerName, String homeName) {
        List<HomeData> homes = playerHomes.get(playerName);
        if (homes != null) {
            for (HomeData home : homes) {
                if (home.getHomeName().equalsIgnoreCase(homeName)) {
                    return home;
                }
            }
        }
        return null;
    }

    public List<HomeData> getPlayerHomes(String playerName) {
        return playerHomes.getOrDefault(playerName, new ArrayList<>());
    }

    private void save() {
        File dataFile = new File(plugin.getDataFolder(), "homes.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile), StandardCharsets.UTF_8))) {
            gson.toJson(playerHomes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        File dataFile = new File(plugin.getDataFolder(), "homes.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playerHomes = new HashMap<String, List<HomeData>>();
            return;
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8))) {
            Type type = new TypeToken<HashMap<String, List<HomeData>>>() {}.getType();
            playerHomes = gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
