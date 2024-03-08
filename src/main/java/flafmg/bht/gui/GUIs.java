package flafmg.bht.gui;

import flafmg.bht.Data.HomeData;
import flafmg.bht.gui.GUI;
import flafmg.bht.managers.ConfigManager;
import flafmg.bht.managers.HomeManager;
import flafmg.bht.managers.TeleportManager;
import flafmg.bht.util.ChatUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIs {
    private final Plugin plugin;
    private final ConfigManager configManager;

    public GUIs(ConfigManager configManager, Plugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    public void openHomeGui(Player player, String targetPlayer, TeleportManager teleportManager, HomeManager homeManager, int page) {

        List<HomeData> homes = player.getName().equals(targetPlayer) || player.hasPermission("bht.bypass-access")? homeManager.getPlayerHomes(targetPlayer) : homeManager.getPublicPlayerHomes(targetPlayer);
        int maxPages = (int) Math.ceil((double) homes.size() / 7);

        String guiName = ChatUtils.formatMessage(configManager.getHomesMenuTitle(), Map.of("%player%", targetPlayer, "%page%", "" + page, "%pages%", ""+maxPages));
        GUI homeGui = new GUI(guiName, 9, player, plugin);

        ItemStack prevPageButton = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta prevPageButtonMeta = prevPageButton.getItemMeta();
        prevPageButtonMeta.setDisplayName(ChatUtils.formatMessage(configManager.getHomesMenuPreviousPageItem(), Map.of("%page%", ""+(page - 1))));
        prevPageButton.setItemMeta(prevPageButtonMeta);

        ItemStack nextPageButton = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta nextPageButtonMeta = nextPageButton.getItemMeta();
        nextPageButtonMeta.setDisplayName(ChatUtils.formatMessage(configManager.getHomesMenuNextPageItem(), Map.of("%page%", ""+(page - 1))));
        nextPageButton.setItemMeta(nextPageButtonMeta);

        ItemStack pageNull = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta pageNullMeta = pageNull.getItemMeta();
        pageNullMeta.setDisplayName("");
        pageNull.setItemMeta(pageNullMeta);

        if (page > 1) {
            homeGui.addItem(0, prevPageButton, p -> {
                homeGui.close();
                openHomeGui(p, targetPlayer, teleportManager, homeManager, page - 1);
            });
        } else {
            homeGui.addItem(0, pageNull, null);
        }

        if (page < maxPages) {
            homeGui.addItem(8, nextPageButton, p -> {
                homeGui.close();
                openHomeGui(p, targetPlayer, teleportManager, homeManager, page + 1);
            });
        } else {
            homeGui.addItem(8, pageNull, null);
        }

        if(homes != null){
            int startIndex = (page - 1) * 7;
            int endIndex = Math.min(startIndex + 7, homes.size());

            int slot = 1;
            for (int i = startIndex; i < endIndex; i++) {
                HomeData home = homes.get(i);
                Location homeLocation = home.getLocation();
                Biome biome = homeLocation.getBlock().getBiome();
                Material biomeBlock = configManager.getBlockForBiome(biome.name());

                ItemStack homeItem = new ItemStack(biomeBlock);
                ItemMeta homeItemMeta = homeItem.getItemMeta();
                if (homeItemMeta != null) {
                    homeItemMeta.setDisplayName(ChatUtils.formatMessage(configManager.getHomesMenuItemName(), Map.of("%home%", home.getHomeName())));
                    List<String> lore = new ArrayList<>();
                    String[] lines = configManager.getHomesMenuItemLore().split("\n");
                    for (String line : lines) {
                        lore.add(ChatUtils.formatMessage(line, Map.of(
                                "%position%", home.getLocation().getBlockX() + " " + home.getLocation().getBlockY() + " " + home.getLocation().getBlockZ(),
                                "%world%", homeLocation.getWorld().getName(),
                                "%biome%", biome.toString(),
                                "%status%", home.isPublic()? configManager.getHomeVisibilityPublic() : configManager.getHomeVisibilityPrivate()
                        )));
                    }
                    homeItemMeta.setLore(lore);
                    homeItem.setItemMeta(homeItemMeta);
                }


                homeGui.addItem(slot, homeItem, p -> {
                    homeGui.close();
                    openConfirmGui(p, home, teleportManager);
                });

                slot++;
            }
        }

        homeGui.open();
    }
    public void openConfirmGui(Player player, HomeData home, TeleportManager teleportManager){
        String guiName = ChatUtils.formatMessage(configManager.getConfirmMenuTitle(), Map.of( "%target%", home.getHomeName()));
        GUI confirmGUI = new GUI(guiName,9, player, plugin);

        ItemStack confirmItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmItemMeta = confirmItem.getItemMeta();
        confirmItemMeta.setDisplayName(ChatUtils.formatMessage(configManager.getConfirmMenuConfirmItemName(), Map.of("%target%", home.getHomeName())));
        confirmItem.setItemMeta(confirmItemMeta);

        ItemStack cancelItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelItemMeta = cancelItem.getItemMeta();
        cancelItemMeta.setDisplayName(ChatUtils.formatMessage(configManager.getConfirmMenuCancelItemName(),  Map.of("%target%", home.getHomeName())));
        cancelItem.setItemMeta(cancelItemMeta);

        confirmGUI.addItem(0, cancelItem, p -> confirmGUI.close());
        confirmGUI.addItem(1, cancelItem, p -> confirmGUI.close());
        confirmGUI.addItem(2, cancelItem, p -> confirmGUI.close());
        confirmGUI.addItem(3, cancelItem, p -> confirmGUI.close());

        Location homeLocation = home.getLocation();
        Biome biome = homeLocation.getBlock().getBiome();
        Material biomeBlock = configManager.getBlockForBiome(biome.name());

        ItemStack homeItem = new ItemStack(biomeBlock);
        ItemMeta homeItemMeta = homeItem.getItemMeta();
        if (homeItemMeta != null) {
            homeItemMeta.setDisplayName(ChatUtils.formatMessage(configManager.getHomesMenuItemName(), Map.of("%home%", home.getHomeName())));
            List<String> lore = new ArrayList<>();
            String[] lines = configManager.getHomesMenuItemLore().split("\n");
            for (String line : lines) {
                lore.add(ChatUtils.formatMessage(line, Map.of(
                        "%position%", home.getLocation().getBlockX() + " " + home.getLocation().getBlockY() + " " + home.getLocation().getBlockZ(),
                        "%world%", homeLocation.getWorld().getName(),
                        "%biome%", biome.toString(),
                        "%status%", home.isPublic()? configManager.getHomeVisibilityPublic() : configManager.getHomeVisibilityPrivate()
                )));
            }
            homeItemMeta.setLore(lore);
            homeItem.setItemMeta(homeItemMeta);
        }


        confirmGUI.addItem(4, homeItem, null);

        confirmGUI.addItem(5, confirmItem, p -> {confirmGUI.close(); teleportManager.addTeleportation(p, home.getLocation(), home.getHomeName());});
        confirmGUI.addItem(6, confirmItem, p -> {confirmGUI.close(); teleportManager.addTeleportation(p, home.getLocation(), home.getHomeName());});
        confirmGUI.addItem(7, confirmItem, p -> {confirmGUI.close(); teleportManager.addTeleportation(p, home.getLocation(), home.getHomeName());});
        confirmGUI.addItem(8, confirmItem, p -> {confirmGUI.close(); teleportManager.addTeleportation(p, home.getLocation(), home.getHomeName());});

        confirmGUI.open();
    }
}