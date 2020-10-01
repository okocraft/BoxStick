package net.okocraft.boxstick.listener;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.EqualsAndHashCode;
import net.okocraft.box.Box;
import net.okocraft.box.config.Config;
import net.okocraft.box.database.PlayerData;
import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.gui.FarmerStickGUI;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.StickItem;
import net.okocraft.boxstick.sticks.stickdata.FarmerStickData;
import net.okocraft.boxstick.sticks.stickdata.StickData;

/**
 * イベント処理によって、Boxに入っている種を利用した苗の自動植え替えを実現する。
 * start()を実行すると、以前にリスナーが登録されていたら解除される。
 */
@EqualsAndHashCode
public class FarmerStickListener implements Listener {

    private final BoxStick plugin;
    private final Box box = Box.getInstance();
    private final Config config = box.getAPI().getConfig();
    private final PlayerData playerData = box.getAPI().getPlayerData();

    private static final Map<Material, Material> PLANTS = new EnumMap<>(Material.class);
    private static final Map<Material, Material> TREES = new EnumMap<>(Material.class);
    
    static {
        PLANTS.put(Material.WHEAT, Material.WHEAT_SEEDS);
        PLANTS.put(Material.POTATOES, Material.POTATO);
        PLANTS.put(Material.CARROTS, Material.CARROT);
        PLANTS.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
        PLANTS.put(Material.NETHER_WART, Material.NETHER_WART);
        PLANTS.put(Material.SWEET_BERRY_BUSH, Material.SWEET_BERRIES);
        
        TREES.put(Material.ACACIA_LOG, Material.ACACIA_SAPLING);
        TREES.put(Material.ACACIA_WOOD, Material.ACACIA_SAPLING);
        TREES.put(Material.SPRUCE_LOG, Material.SPRUCE_SAPLING);
        TREES.put(Material.SPRUCE_WOOD, Material.SPRUCE_SAPLING);
        TREES.put(Material.BIRCH_LOG, Material.BIRCH_SAPLING);
        TREES.put(Material.BIRCH_WOOD, Material.BIRCH_SAPLING);
        TREES.put(Material.JUNGLE_LOG, Material.JUNGLE_SAPLING);
        TREES.put(Material.JUNGLE_WOOD, Material.JUNGLE_SAPLING);
        TREES.put(Material.DARK_OAK_LOG, Material.DARK_OAK_SAPLING);
        TREES.put(Material.DARK_OAK_WOOD, Material.DARK_OAK_SAPLING);
        TREES.put(Material.OAK_LOG, Material.OAK_SAPLING);
        TREES.put(Material.OAK_WOOD, Material.OAK_SAPLING);
        TREES.put(Material.STRIPPED_ACACIA_LOG, Material.ACACIA_SAPLING);
        TREES.put(Material.STRIPPED_ACACIA_WOOD, Material.ACACIA_SAPLING);
        TREES.put(Material.STRIPPED_SPRUCE_LOG, Material.SPRUCE_SAPLING);
        TREES.put(Material.STRIPPED_SPRUCE_WOOD, Material.SPRUCE_SAPLING);
        TREES.put(Material.STRIPPED_BIRCH_LOG, Material.BIRCH_SAPLING);
        TREES.put(Material.STRIPPED_BIRCH_WOOD, Material.BIRCH_SAPLING);
        TREES.put(Material.STRIPPED_JUNGLE_LOG, Material.JUNGLE_SAPLING);
        TREES.put(Material.STRIPPED_JUNGLE_WOOD, Material.JUNGLE_SAPLING);
        TREES.put(Material.STRIPPED_DARK_OAK_LOG, Material.DARK_OAK_SAPLING);
        TREES.put(Material.STRIPPED_DARK_OAK_WOOD, Material.DARK_OAK_SAPLING);
        TREES.put(Material.STRIPPED_OAK_LOG, Material.OAK_SAPLING);
        TREES.put(Material.STRIPPED_OAK_WOOD, Material.OAK_SAPLING);
    }
    
    public FarmerStickListener(BoxStick plugin) {
        this.plugin = plugin;
    }

    /**
     * このリスナーをBukkitに登録し、稼働させる。
     */
    public void start() {
        HandlerList.unregisterAll(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        if (!(stick.getData() instanceof FarmerStickData)) {
            return;
        }
        e.getPlayer().openInventory(new FarmerStickGUI(stick).getInventory());
    }

    private boolean isBulkHarvesting = false;

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isBulkHarvesting) {
            return;
        }
        if (config.getAutoReplantWorlds().contains(event.getBlock().getWorld().getName())) {
            return;
        }
        if (!event.getPlayer().hasPermission("boxstick.farmer")) {
            return;
        }
        ItemStack stick = event.getPlayer().getInventory().getItemInOffHand();
        StickData rawData = StickItem.getDataOf(stick);
        if (!(rawData instanceof FarmerStickData)) {
            return;
        }
        FarmerStickData data = (FarmerStickData) rawData;
        
        Block block = event.getBlock();
        Material type = block.getType();

        if (PLANTS.containsKey(type)) {
            isBulkHarvesting = true;
            replantRangedSeed(event, data);
            isBulkHarvesting = false;
            return;
        }

        if (data.getProtectFarmLand().getValue() && isFarmLand(block)) {
            event.setCancelled(true);
            return;
        }

        if (data.getProtectYoungPlants().getValue() && isYoungPlant(block)) {
            event.setCancelled(true);
            return;
        }

        if (TREES.containsKey(type)) {
            replantSapling(event, data.getProtectWhenNoStock().getValue());
            return;
        }

        if (type == Material.CHORUS_FLOWER || type == Material.CHORUS_PLANT) {
            replantChorus(event, data.getProtectWhenNoStock().getValue());
            return;
        }
    }

    private void replantRangedSeed(BlockBreakEvent event, FarmerStickData data) {
        event.setCancelled(true);
        int range = data.getHarvestingRange().getValue();
        Location point = event.getBlock().getLocation().clone();
        point.add(-1 * range, 0, -1 * range);
        int minX = point.getBlockX();
        int minZ = point.getBlockZ();
        int maxX = minX + range + range;
        int maxZ = minZ + range + range;
        Block block;
        BlockBreakEvent sub;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                block = point.getBlock();
                if (!PLANTS.containsKey(block.getType()) || data.getProtectYoungPlants().getValue() && isYoungPlant(block)) {
                    point.add(0, 0, 1);
                    continue;
                }
                // 保護プラグインを考慮して、イベント処理を行う
                sub = new BlockBreakEvent(block, event.getPlayer());
                Bukkit.getPluginManager().callEvent(sub);
                if (!sub.isCancelled()) {
                    replantSeed(sub, data.getProtectWhenNoStock().getValue());
                }
                point.add(0, 0, 1);
            }
            point.setZ(minZ);
            point.add(1, 0, 0);
        }
    }

    private void replantSeed(BlockBreakEvent event, boolean protectWhenNoStock) {
        Block brokenBlock = event.getBlock();
        Material brokenBlockType = brokenBlock.getType();
        Player player = event.getPlayer();
        Material seed = PLANTS.get(brokenBlockType);

        if (!hasSeed(player, seed)) {
            if (protectWhenNoStock) {
                event.setCancelled(true);
                return;
            }
            brokenBlock.breakNaturally();
            return;
        }

        brokenBlock.breakNaturally();

        // mcmmoの自動植え替え互換
        new BukkitRunnable() {

            @Override
            public void run() {
                Block block = brokenBlock.getLocation().getBlock();
                if (!block.getType().equals(Material.AIR)) {
                    return;
                }

                block.setType(brokenBlockType);
                takeSeed(player, seed);
            }
        }.runTaskLater(plugin, 3L);
    }

    private void replantSapling(BlockBreakEvent event, boolean protectWhenNoStock) {
        Block block = event.getBlock();
        Material sapling = TREES.get(block.getType());
        Material blockBelow = block.getRelative(BlockFace.DOWN).getBlockData().getMaterial();
        
        if (blockBelow != Material.DIRT && blockBelow != Material.GRASS_BLOCK && blockBelow != Material.PODZOL) {
            return;
        }

        if (!hasSeed(event.getPlayer(), sapling)) {
            if (protectWhenNoStock) {
                event.setCancelled(true);
            }
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                block.getLocation().getBlock().setType(sapling);
                takeSeed(event.getPlayer(), sapling);
            }
        }.runTaskLater(plugin, 3L);
    }

    private boolean isYoungPlant(Block block) {
        BlockData blockData = block.getBlockData();
        if (block.getType() == Material.SUGAR_CANE) {
            return block.getRelative(BlockFace.DOWN).getType() != Material.SUGAR_CANE;
        }

        if (block.getType() == Material.CHORUS_FLOWER) {
            return block.getRelative(BlockFace.DOWN).getType() == Material.END_STONE;
        }

        if (blockData instanceof Sapling) {
            Sapling sapling = (Sapling) blockData;
            return sapling.getStage() < sapling.getMaximumStage();
        }

        if (blockData instanceof Ageable) {
            Ageable ageable = (Ageable) blockData;
            return ageable.getAge() < ageable.getMaximumAge();
        }

        return false;
    }

    private boolean isFarmLand(Block block) {

        Material ground = block.getType();
        Material plant = block.getRelative(BlockFace.UP).getType();

        if (TREES.containsKey(plant)
                && (ground == Material.DIRT || ground == Material.GRASS_BLOCK || ground == Material.PODZOL)) {
            return true;
        }

        if (PLANTS.containsKey(plant) && ground == Material.FARMLAND) {
            return true;
        }

        if ((plant == Material.CHORUS_FLOWER || plant == Material.CHORUS_PLANT) && ground == Material.END_STONE) {
            return true;
        }

        // 作物がネザーウォートなら下は必ずソウルサンドなのでチェック不要
        return plant == Material.NETHER_WART;
    }

    private void replantChorus(BlockBreakEvent event, boolean protectWhenNoStock) {
        if (protectWhenNoStock && !hasSeed(event.getPlayer(), Material.CHORUS_FLOWER)) {
            event.setCancelled(true);
            return;
        }

        takeSeed(event.getPlayer(), Material.CHORUS_FLOWER);
        breakChorus(event.getBlock());
    }

    private void breakChorus(Block chorus) {
        if (chorus.getType() != Material.CHORUS_PLANT && chorus.getType() != Material.CHORUS_FLOWER) {
            return;
        }

        new BukkitRunnable(){
            
            @Override
            public void run() {
                chorus.breakNaturally();
                if (chorus.getRelative(BlockFace.DOWN).getType() == Material.END_STONE) {
                    chorus.setType(Material.CHORUS_FLOWER);
                    return;
                }
                breakChorus(chorus.getRelative(BlockFace.NORTH));
                breakChorus(chorus.getRelative(BlockFace.EAST));
                breakChorus(chorus.getRelative(BlockFace.WEST));
                breakChorus(chorus.getRelative(BlockFace.SOUTH));
                breakChorus(chorus.getRelative(BlockFace.DOWN));
            }
        }.runTaskLater(plugin, 1L);
    }

    private void takeSeed(Player player, Material seed) {
        if (!PLANTS.containsValue(seed)) {
            return;
        }

        ItemStack seedItem = new ItemStack(seed);
        int stock = playerData.getStock(player, seedItem);

        if (stock >= 1) {
            playerData.setStock(player, seedItem, stock - 1);
        } else {
            player.getInventory().removeItem(new ItemStack(seed));
        }
    }

    private boolean hasSeed(Player player, Material seed) {
        if (!PLANTS.containsValue(seed)) {
            return false;
        }

        if (player.getInventory().contains(seed)) {
            return true;
        }

        return playerData.getStock(player, new ItemStack(seed)) > 0;
    }
}