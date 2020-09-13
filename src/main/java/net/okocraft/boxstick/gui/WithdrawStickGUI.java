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
import net.okocraft.boxstick.sticks.stickdata.WithdrawStickData;

public class WithdrawStickGUI extends GUI {

    private static final String VALUE_PLACEHOLDER = "%value%";
    private static final String PLACE_BLOCK = ChatColor.YELLOW + "ブロック設置で補充を行うか: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String EAT_FOOD = ChatColor.YELLOW + "アイテム消費で補充を行うか: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String THROW_POTION = ChatColor.YELLOW + "ポーション投擲で補充を行うか: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    private static final String BREAK_ITEM = ChatColor.YELLOW + "武器・道具類の破損時に補充を行うか: " + ChatColor.AQUA + VALUE_PLACEHOLDER;
    
    public WithdrawStickGUI(Stick stick) {
        super(9, "Withdraw Stick - 設定画面");

        StickData stickData = stick.getData();
        if (!(stickData instanceof WithdrawStickData)) {
            throw new IllegalArgumentException("Type of stick is not withdraw.");
        }
        WithdrawStickData data = (WithdrawStickData) stickData;

        ButtonIcon placeBlockIcon = new ButtonIcon(new ItemStack(Material.BRICKS))
                .setDisplayName(PLACE_BLOCK.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnPlaceBlock())))
                .setGlowing(data.getOnPlaceBlock());
        ButtonIcon eatFoodIcon = new ButtonIcon(new ItemStack(Material.COOKED_BEEF))
                .setDisplayName(EAT_FOOD.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnEatFood())))
                .setGlowing(data.getOnEatFood());
        ButtonIcon throwPotionIcon = new ButtonIcon(new ItemStack(Material.POTION))
                .setDisplayName(THROW_POTION.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnThrowPotion())))
                .setGlowing(data.getOnThrowPotion());
        ButtonIcon breakItemIcon = new ButtonIcon(new ItemStack(Material.IRON_PICKAXE))
                .setDisplayName(BREAK_ITEM.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnBreakItem())))
                .setGlowing(data.getOnBreakItem());

        ButtonIcon otherStickIcon = new ButtonIcon(new ItemStack(Material.STICK))
                .setDisplayName(ChatColor.RED + "別の種類の棒に変更")
                .setLore(List.of(ChatColor.RED + "注意. 現在の設定は破棄されます"));

        buttonList.putButton(1, 0, new AbstractButton(placeBlockIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setOnPlaceBlock(!data.getOnPlaceBlock());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(PLACE_BLOCK.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnPlaceBlock())))
                        .setGlowing(data.getOnPlaceBlock());
                setItem(0);
            }
        });

        buttonList.putButton(1, 2, new AbstractButton(eatFoodIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setOnEatFood(!data.getOnEatFood());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(EAT_FOOD.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnEatFood())))
                        .setGlowing(data.getOnEatFood());
                setItem(2);
            }
        });

        buttonList.putButton(1, 4, new AbstractButton(throwPotionIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setOnThrowPotion(!data.getOnThrowPotion());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(THROW_POTION.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnThrowPotion())))
                        .setGlowing(data.getOnThrowPotion());
                setItem(4);
            }
        });

        buttonList.putButton(1, 6, new AbstractButton(breakItemIcon){

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                data.setOnBreakItem(!data.getOnBreakItem());
                stick.updateHandItem((Player) e.getWhoClicked());
                update();
            }
            
            @Override
            public void update() {
                icon.setDisplayName(BREAK_ITEM.replace(VALUE_PLACEHOLDER, String.valueOf(data.getOnBreakItem())))
                        .setGlowing(data.getOnBreakItem());
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
