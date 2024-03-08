package flafmg.bht.managers;

import flafmg.bht.util.ChatUtils;
import flafmg.bht.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final Plugin plugin;
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
    private String homeVisibilityChanged;
    private String homeVisibilityPublic;
    private String homeVisibilityPrivate;
    private String homeLimitReached;
    private String homeNotFound;
    private String teleportingTitle;
    private String teleportingSubtitle;

    private String confirmMenuTitle;
    private String confirmMenuConfirmItemName;
    private String confirmMenuCancelItemName;

    private String homesMenuTitle;
    private String homesMenuItemName;
    private String homesMenuItemLore;
    private String homesMenuPageItem;
    private String homesMenuNextPageItem;
    private String homesMenuPreviousPageItem;
    private Map<String, Material> biomeBlocks; // Mapa de biomas para blocos correspondentes



    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;

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
        homeVisibilityChanged = config.getString("Messages.home-visibility-changed");
        homeVisibilityPublic = config.getString("Messages.home-visibility-public");
        homeVisibilityPrivate = config.getString("Messages.home-visibility-private");
        homeLimitReached = config.getString("Messages.home-limit-reached");
        homeNotFound = config.getString("Messages.home-not-found");

        teleportingTitle = config.getString("Titles.teleporting-title");
        teleportingSubtitle = config.getString("Titles.teleporting-subtitle");

        confirmMenuTitle = config.getString("Menus.confirm-menu.menu-title");
        confirmMenuConfirmItemName = config.getString("Menus.confirm-menu.menu-confirm-item-name");
        confirmMenuCancelItemName = config.getString("Menus.confirm-menu.menu-cancel-item-name");


        homesMenuTitle = config.getString("Menus.homes-menu.menu-title");
        homesMenuItemName = config.getString("Menus.homes-menu.menu-home-item-name");
        homesMenuItemLore = config.getString("Menus.homes-menu.menu-home-item-lore");
        homesMenuPageItem = config.getString("Menus.homes-menu.menu-page-item");
        homesMenuNextPageItem = config.getString("Menus.homes-menu.menu-next-page-item");
        homesMenuPreviousPageItem = config.getString("Menus.homes-menu.menu-previous-page-item");
        biomeBlocks = new HashMap<>();
        ConfigurationSection biomeSection = config.getConfigurationSection("Menus.homes-menu.menu-item-biomes-list");
        if (biomeSection != null) {
            for (String biomeName : biomeSection.getKeys(false)) {
                Material blockMaterial = Material.matchMaterial(biomeSection.getString(biomeName));
                if (blockMaterial != null) {
                    biomeBlocks.put(biomeName, blockMaterial);
                }
            }
        }


        sanityCheck();
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


    public String getHomeLimitReached() {
        return homeLimitReached;
    }

    public String getHomeNotFound() {
        return homeNotFound;
    }

    public String getTeleportingTitle() {
        return teleportingTitle;
    }

    public String getTeleportingSubtitle() {
        return teleportingSubtitle;
    }

    public String getInvalidCommand() {
        return invalidCommand;
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

    public String getHomesMenuTitle() {
        return homesMenuTitle;
    }

    public String getHomesMenuItemName() {
        return homesMenuItemName;
    }

    public String getHomesMenuItemLore() {
        return homesMenuItemLore;
    }

    public String getHomesMenuPageItem() {
        return homesMenuPageItem;
    }

    public String getHomesMenuNextPageItem() {
        return homesMenuNextPageItem;
    }

    public String getHomesMenuPreviousPageItem() {
        return homesMenuPreviousPageItem;
    }

    public Material getBlockForBiome(String biomeName) {
        return biomeBlocks.getOrDefault(biomeName, Material.STONE);
    }

    public String getHomeVisibilityChanged() {
        return homeVisibilityChanged;
    }

    public String getHomeVisibilityPublic() {
        return homeVisibilityPublic;
    }

    public String getHomeVisibilityPrivate() {
        return homeVisibilityPrivate;
    }


    private void sanityCheck() {
        checkForNull(confirmMenuTitle, "Menus.confirm-menu.menu-title");
        checkForNull(confirmMenuConfirmItemName, "Menus.confirm-menu.menu-confirm-item-name");
        checkForNull(confirmMenuCancelItemName, "Menus.confirm-menu.menu-cancel-item-name");

        checkForNull(homesMenuTitle, "Menus.homes-menu.menu-title");
        checkForNull(homesMenuItemName, "Menus.homes-menu.menu-home-item-name");
        checkForNull(homesMenuItemLore, "Menus.homes-menu.menu-home-item-lore");
        checkForNull(homesMenuPageItem, "Menus.homes-menu.menu-page-item");
        checkForNull(homesMenuNextPageItem, "Menus.homes-menu.menu-next-page-item");
        checkForNull(homesMenuPreviousPageItem, "Menus.homes-menu.menu-previous-page-item");

        checkForNull(prefix, "Messages.prefix");
        checkForNull(invalidCommand, "Messages.invalid-command");
        checkForNull(teleportRequestSent, "Messages.teleport-request-sent");
        checkForNull(teleportRequestReceived, "Messages.teleport-request-received");
        checkForNull(teleportRequestExpired, "Messages.teleport-request-expired");
        checkForNull(teleportRequestNotFound, "Messages.teleport-request-not-found");
        checkForNull(teleportRequestPlayerNotFound, "Messages.teleport-request-player-not-found");
        checkForNull(teleportRequestAccepted, "Messages.teleport-request-accepted");
        checkForNull(teleportRequestDenied, "Messages.teleport-request-denied");
        checkForNull(teleportCancelled, "Messages.teleport-cancelled");
        checkForNull(teleportWait, "Messages.teleport-wait");
        checkForNull(homeSet, "Messages.home-set");
        checkForNull(homeRemove, "Messages.home-remove");
        checkForNull(homeVisibilityChanged, "Messages.home-visibility-changed");
        checkForNull(homeVisibilityPublic, "messages.home-visibility-public");
        checkForNull(homeVisibilityPrivate, "messages.home-visibility-private");
        checkForNull(homeLimitReached, "Messages.home-limit-reached");
        checkForNull(homeNotFound, "Messages.home-not-found");
        checkForNull(teleportingTitle, "Titles.teleporting-title");
        checkForNull(teleportingSubtitle, "Titles.teleporting-subtitle");

        checkForNull(teleportWaitTime, "teleport-wait-time");
        checkForNull(teleportRequestExpire, "teleport-request-expire");
        checkForNull(teleportCoolDown, "teleport-coolDown");
        checkForNull(defaultHomeLimit, "home-limit.default");
        checkForNull(homeLimits, "home-limit");
        checkForNull(homeValue, "economy.home-value");
        checkForNull(tpaValue, "economy.tpa-value");
        checkForNull(biomeBlocks, "Menus.homes-menu.menu-item-biomes-list");

        ChatUtils.log("&6[bht] &rConfig's sanity check &apassed!");
    }
    private <T> void checkForNull(T value, String configPath) {
        if (value == null) {
            System.err.println("Config's sanity check error, disabling plugin!");
            System.err.println("in: " + configPath + ", please fix the issue");

            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    public String getConfirmMenuTitle() {
        return confirmMenuTitle;
    }

    public String getConfirmMenuConfirmItemName() {
        return confirmMenuConfirmItemName;
    }

    public String getConfirmMenuCancelItemName() {
        return confirmMenuCancelItemName;
    }
}
