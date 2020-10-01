package net.okocraft.boxstick.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.gui.GUI;

@EqualsAndHashCode
public class GUIListener implements Listener {
    
    private final BoxStick plugin;

    public GUIListener(BoxStick plugin) {
        this.plugin = plugin;
    }
    
    /**
     * このリスナーをBukkitに登録し、稼働させる。
     */
    public void start() {
        HandlerList.unregisterAll(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClicked(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        if (inv.getHolder() instanceof GUI) {
            Inventory clickedInv = event.getClickedInventory();
            if (clickedInv != null && clickedInv.getType() != InventoryType.PLAYER) {
                ((GUI) inv.getHolder()).onClick(event);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
