package flafmg.bht.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class GUI implements Listener {
    private final String name;
    private final int size;
    private final Map<Integer, ClickEvent> clickEvents;
    private final Plugin plugin;
    private final Inventory inventory;
    private final Player player;

    public GUI(String name, int size, Player player, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.player = player;
        this.clickEvents = new HashMap<>();
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, size, name);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addItem(int position, ItemStack itemStack, ClickEvent clickEvent) {
        if (position >= 0 && position < size) {
            clickEvents.put(position, clickEvent);
            inventory.setItem(position, itemStack);
        }
    }

    public void removeItem(int position) {
        if (clickEvents.containsKey(position)) {
            clickEvents.remove(position);
            inventory.setItem(position, null);
        }
    }

    public void open() {
        player.openInventory(inventory);
    }

    public void close() {
        clickEvents.clear();
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        player.closeInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(name) && event.getWhoClicked() == player) {
            int slot = event.getSlot();
            if (clickEvents.containsKey(slot)) {
                ClickEvent clickEvent = clickEvents.get(slot);
                if(clickEvent != null)
                    clickEvent.onClick(player);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(name) && event.getPlayer() == player) {
            close();
        }
    }

    public interface ClickEvent {
        void onClick(Player player);
    }
}
