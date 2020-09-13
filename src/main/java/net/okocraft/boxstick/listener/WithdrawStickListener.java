package net.okocraft.boxstick.listener;

import java.util.Objects;

import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.gui.WithdrawStickGUI;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.StickItem;
import net.okocraft.boxstick.sticks.stickdata.StickData;
import net.okocraft.boxstick.sticks.stickdata.WithdrawStickData;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import net.okocraft.box.Box;
import net.okocraft.box.config.Config;
import net.okocraft.box.database.ItemData;
import net.okocraft.box.database.PlayerData;

/**
 * Box stickに関するイベントを処理するリスナー。start()を実行すると、以前にリスナーが登録されていたら解除される。
 */
public class WithdrawStickListener implements Listener {

    private final BoxStick plugin;
    private final Box box = Box.getInstance();
    private final Config config = box.getAPI().getConfig();
    private final PlayerData playerData = box.getAPI().getPlayerData();
    private final ItemData itemData = box.getAPI().getItemData();

    public WithdrawStickListener(BoxStick plugin) {
        this.plugin = plugin;
    }

    /**
     * このリスナーをBukkitに登録し、稼働させる。
     */
    public void start() {
        HandlerList.unregisterAll(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WithdrawStickListener)) {
            return false;
        }
        WithdrawStickListener withdrawStickListener = (WithdrawStickListener) o;
        return Objects.equals(plugin, withdrawStickListener.plugin)
                && Objects.equals(box, withdrawStickListener.box)
                && Objects.equals(config, withdrawStickListener.config)
                && Objects.equals(playerData, withdrawStickListener.playerData)
                && Objects.equals(itemData, withdrawStickListener.itemData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, box, config, playerData, itemData);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void openStickSetting(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND || e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        ItemStack stickItem = e.getPlayer().getInventory().getItemInMainHand();
        if (!StickItem.isStick(stickItem)) {
            return;
        }
        Stick stick = new Stick(new StickItem(stickItem));
        if (!(stick.getData() instanceof WithdrawStickData)) {
            return;
        }
        e.getPlayer().openInventory(new WithdrawStickGUI(stick).getInventory());
        e.setCancelled(true);
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void blockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("boxstick.withdraw.block")) {
            return;
        }
        
        StickData stickData = StickItem.getDataOf(event.getPlayer().getInventory().getItemInOffHand());
        if (!(stickData instanceof WithdrawStickData)) {
            return;
        }
        if (!((WithdrawStickData) stickData).getOnPlaceBlock()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack handItem = event.getItemInHand();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if (handItem.getType() == mainHandItem.getType() && useItemFromDatabase(handItem, player)) {
            mainHandItem.setAmount(mainHandItem.getAmount());
            player.getInventory().setItemInMainHand(mainHandItem);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void itemConsume(PlayerItemConsumeEvent event) {
        if (!event.getPlayer().hasPermission("boxstick.withdraw.food")) {
            return;
        }

        StickData stickData = StickItem.getDataOf(event.getPlayer().getInventory().getItemInOffHand());
        if (!(stickData instanceof WithdrawStickData)) {
            return;
        }
        if (!((WithdrawStickData) stickData).getOnEatFood()) {
            return;
        }

        if (useItemFromDatabase(event.getItem(), event.getPlayer())) {
            event.setItem(event.getItem().clone());
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void itemBreak(PlayerItemBreakEvent event) {
        if (!event.getPlayer().hasPermission("boxstick.withdraw.tool")) {
            return;
        }

        Player player = event.getPlayer();

        StickData stickData = StickItem.getDataOf(player.getInventory().getItemInOffHand());
        if (!(stickData instanceof WithdrawStickData)) {
            return;
        }
        if (!((WithdrawStickData) stickData).getOnBreakItem()) {
            return;
        }

        ItemStack item = event.getBrokenItem();
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(0);
        }
        clone.setItemMeta(meta);
        if (useItemFromDatabase(clone, player)) {
            item.setAmount(2);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1F, 1F);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void potionThrow(ProjectileLaunchEvent event) {

        if (!(event.getEntity() instanceof ThrownPotion)) {
            return;
        }

        ThrownPotion thrownPotion = (ThrownPotion) event.getEntity();
        if (!(thrownPotion.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) thrownPotion.getShooter();
        if (!player.hasPermission("boxstick.withdraw.potion")) {
            return;
        }

        StickData stickData = StickItem.getDataOf(player.getInventory().getItemInOffHand());
        if (!(stickData instanceof WithdrawStickData)) {
            return;
        }
        if (!((WithdrawStickData) stickData).getOnThrowPotion()) {
            return;
        }

        ItemStack handItem = player.getInventory().getItemInMainHand();
        Material handItemType = handItem.getType();
        if (handItemType != Material.SPLASH_POTION && handItemType != Material.LINGERING_POTION) {
            return;
        }

        if (useItemFromDatabase(handItem, player)) {
            handItem.setAmount(handItem.getAmount() + 1);
        }
    }

    private boolean useItemFromDatabase(ItemStack item, Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }
        
        if (config.getDisabledWorlds().contains(player.getWorld().getName())) {
            return false;
        }
        
        ItemStack index = item.clone();
        index.setAmount(1);
        String itemName = itemData.getName(index);
        if (itemName == null) {
            return false;
        }
        
        if (!box.getAPI().getCategories().getAllItems().contains(itemName)) {
            return false;
        }
        
        int stock = playerData.getStock(player, index);
        if (stock < 1) {
            return false;
        }

        playerData.setStock(player, index, stock - 1);
        return true;
    }
}