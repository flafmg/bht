package flafmg.bht.managers;

import flafmg.bht.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private int teleportWaitTime;
    private int teleportRequestExpire;
    private int teleportCoolDown;
    private int defaultHomeLimit;
    private Map<String, Integer> homeLimits;
    private int homeValue;
    private int tpaValue;
    private String prefix;
    private String invalidCommand;
    private String teleportRequestSent;
    private String teleportRequestReceived;
    private String teleportRequestExpired;
    private String teleportRequestNotFound;
    private String teleportRequestPlayerNotFound;
    private String teleportRequestAccepted;
    private String teleportRequestDenied;
    private String teleportCancelled;
    private String teleportWait;
    private String homeSet;
    private String homeRemove;
    private String homeListHeader;
    private String homeListItem;
    private String homeListEmpty;
    private String homeLimitReached;
    private String homeNotFound;
    private String teleportingHomeTitle;
    private String teleportingHomeSubtitle;
    private String teleportingPlayerTitle;
    private String teleportingPlayerSubtitle;

    public ConfigManager(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
            System.out.println("config.yml is missing, created new config.yml");
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        teleportWaitTime = config.getInt("teleport-wait-time");
        teleportRequestExpire = config.getInt("teleport-request-expire");
        teleportCoolDown = config.getInt("teleport-coolDown");
        defaultHomeLimit = config.getInt("home-limit.default");
        homeLimits = (new HashMap<>());
        ConfigurationSection groupsSection = config.getConfigurationSection("home-limit");
        if (groupsSection != null) {
            for (String groupName : groupsSection.getKeys(false)) {
                if (!groupName.equalsIgnoreCase("default")) {
                    int groupLimit = groupsSection.getInt(groupName);
                    homeLimits.put(groupName, groupLimit);
                }
            }
        }
        homeValue = config.getInt("economy.home-value");
        tpaValue = config.getInt("economy.tpa-value");

        prefix = config.getString("Messages.prefix");
        invalidCommand = config.getString("Messages.invalid-command");
        teleportRequestSent = config.getString("Messages.teleport-request-sent");
        teleportRequestReceived = config.getString("Messages.teleport-request-received");
        teleportRequestExpired = config.getString("Messages.teleport-request-expired");
        teleportRequestNotFound = config.getString("Messages.teleport-request-not-found");
        teleportRequestPlayerNotFound = config.getString("Messages.teleport-request-player-not-found");
        teleportRequestAccepted = config.getString("Messages.teleport-request-accepted");
        teleportRequestDenied = config.getString("Messages.teleport-request-denied");
        teleportCancelled = config.getString("Messages.teleport-cancelled");
        teleportWait = config.getString("Messages.teleport-wait");
        homeSet = config.getString("Messages.home-set");
        homeRemove = config.getString("Messages.home-remove");
        homeListHeader = config.getString("Messages.home-list-header");
        homeListItem = config.getString("Messages.home-list-item");
        homeListEmpty = config.getString("Messages.home-list-empty");
        homeLimitReached = config.getString("Messages.home-limit-reached");
        homeNotFound = config.getString("Messages.home-not-found");

        teleportingHomeTitle = config.getString("Titles.teleporting-home-title");
        teleportingHomeSubtitle = config.getString("Titles.teleporting-home-subtitle");
        teleportingPlayerTitle = config.getString("Titles.teleporting-player-title");
        teleportingPlayerSubtitle = config.getString("Titles.teleporting-player-subtitle");
    }

    public int getTeleportWaitTime() {
        return teleportWaitTime;
    }

    public int getTeleportRequestExpire() {
        return teleportRequestExpire;
    }

    public int getTeleportCoolDown() {
        return teleportCoolDown;
    }

    public int getDefaultHomeLimit() {
        return defaultHomeLimit;
    }

    public int getHomeValue() {
        return homeValue;
    }

    public int getTpaValue() {
        return tpaValue;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTeleportRequestSent() {
        return teleportRequestSent;
    }

    public String getTeleportRequestReceived() {
        return teleportRequestReceived;
    }

    public String getTeleportRequestExpired() {
        return teleportRequestExpired;
    }

    public String getTeleportRequestNotFound() {
        return teleportRequestNotFound;
    }

    public String getTeleportRequestAccepted() {
        return teleportRequestAccepted;
    }

    public String getTeleportRequestDenied() {
        return teleportRequestDenied;
    }

    public String getTeleportCancelled() {
        return teleportCancelled;
    }

    public String getTeleportWait() {
        return teleportWait;
    }

    public String getHomeSet() {
        return homeSet;
    }

    public String getHomeRemove() {
        return homeRemove;
    }

    public String getHomeListHeader() {
        return homeListHeader;
    }

    public String getHomeListItem() {
        return homeListItem;
    }

    public String getHomeLimitReached() {
        return homeLimitReached;
    }

    public String getHomeNotFound() {
        return homeNotFound;
    }

    public String getTeleportingHomeTitle() {
        return teleportingHomeTitle;
    }

    public String getTeleportingHomeSubtitle() {
        return teleportingHomeSubtitle;
    }

    public String getTeleportingPlayerTitle() {
        return teleportingPlayerTitle;
    }

    public String getTeleportingPlayerSubtitle() {
        return teleportingPlayerSubtitle;
    }

    public String getInvalidCommand() {
        return invalidCommand;
    }

    public String getHomeListEmpty() {
        return homeListEmpty;
    }

    public int getHomeLimit(Player player) {
        for (String group : homeLimits.keySet()) {
            if (Utils.isPlayerInGroup(player, group)) {
                return homeLimits.get(group);
            }
        }
        return defaultHomeLimit;
    }

    public String getTeleportRequestPlayerNotFound() {
        return teleportRequestPlayerNotFound;
    }
}
