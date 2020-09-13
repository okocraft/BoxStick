package net.okocraft.boxstick.gui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.okocraft.boxstick.gui.button.AbstractButton;
import net.okocraft.boxstick.gui.button.ButtonIcon;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.StickData;
import net.okocraft.boxstick.sticks.stickdata.FarmerStickData;

public class FarmerStickGUI extends GUI {

    private static final String VALUE_PLACEHOLDER = "%value%";
    private static final String PROTECT_YOUNG_PLANT = ChatColor.YELLOW + "植えたばかりの作物を破壊しない: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String PROTECT_FARM_LAND = ChatColor.YELLOW + "作物が植わっている地面を破壊しない: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String PROTECT_WHEN_NO_STOCK = ChatColor.YELLOW + "在庫がないときに作物を破壊しない: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String HARVESTING_RANGE = ChatColor.YELLOW + "作物の一括破壊の範囲: " + ChatColor.AQUA
            + VALUE_PLACEHOLDER;

    public FarmerStickGUI(Stick stick) {
        super(9, "Farmer Stick - 設定画面");

        StickData stickData = stick.getData();
        if (!(stickData instanceof FarmerStickData)) {
            throw new IllegalArgumentException("Type of stick is not famer.");
        }
        FarmerStickData data = (FarmerStickData) stickData;

        ButtonIcon protectYoungPlantIcon = new ButtonIcon(new ItemStack(Material.WHEAT_SEEDS))
                .setDisplayName(PROTECT_YOUNG_PLANT.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectYoungPlants())))
                .setGlowing(data.getProtectYoungPlants());
        ButtonIcon protectFarmLandIcon = new ButtonIcon(new ItemStack(Material.FARMLAND))
                .setDisplayName(PROTECT_FARM_LAND.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectYoungPlants())))
                .setGlowing(data.getProtectYoungPlants());
        ButtonIcon protectWhenNoStockIcon = new ButtonIcon(new ItemStack(Material.BARRIER))
                .setDisplayName(PROTECT_WHEN_NO_STOCK.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectWhenNoStock())))
                .setGlowing(data.getProtectWhenNoStock());
        ButtonIcon harvestingRangeIcon = new ButtonIcon(new ItemStack(Material.TNT))
                .setDisplayName(HARVESTING_RANGE.replace(VALUE_PLACEHOLDER, String.valueOf(data.getHarvestingRange())));

        ButtonIcon otherStickIcon = new ButtonIcon(new ItemStack(Material.STICK))
                .setDisplayName(ChatColor.RED + "別の種類の棒に変更")
                .setLore(List.of(ChatColor.RED + "注意. 現在の設定は破棄されます"));

        buttonList.putButton(1, 0, new AbstractButton(protectYoungPlantIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setProtectYoungPlants(!data.getProtectYoungPlants());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(PROTECT_YOUNG_PLANT.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectYoungPlants())))
                        .setGlowing(data.getProtectYoungPlants());
                setItem(0);
            }
        });

        buttonList.putButton(1, 2, new AbstractButton(protectFarmLandIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setProtectFarmLand(!data.getProtectFarmLand());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(PROTECT_FARM_LAND.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectFarmLand())))
                        .setGlowing(data.getProtectFarmLand());
                setItem(2);
            }
        });

        buttonList.putButton(1, 4, new AbstractButton(protectWhenNoStockIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setProtectWhenNoStock(!data.getProtectWhenNoStock());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(PROTECT_WHEN_NO_STOCK.replace(VALUE_PLACEHOLDER, String.valueOf(data.getProtectWhenNoStock())))
                        .setGlowing(data.getProtectWhenNoStock());
                setItem(4);
            }
        });

        buttonList.putButton(1, 6, new AbstractButton(harvestingRangeIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                if (e.isRightClick()) {
                    data.setHarvestingRange(Math.min(4, data.getHarvestingRange() + 1));
                } else {
                    data.setHarvestingRange(Math.max(0, data.getHarvestingRange() - 1));
                }
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(HARVESTING_RANGE.replace(VALUE_PLACEHOLDER, String.valueOf(data.getHarvestingRange())));
                setItem(6);
            }
        });

        buttonList.putButton(1, 8, new AbstractButton(otherStickIcon) {

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new ChangeStickTypeGUI(stick).getInventory());
            }

            @Override
            public void update() {
                // 何もしない。
            }
        });

        setItems();
    }
}
