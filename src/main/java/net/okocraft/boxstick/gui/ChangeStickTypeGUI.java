package net.okocraft.boxstick.gui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.gui.button.AbstractButton;
import net.okocraft.boxstick.gui.button.Button;
import net.okocraft.boxstick.gui.button.ButtonIcon;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.StickType;

@EqualsAndHashCode(callSuper = true)
public class ChangeStickTypeGUI extends GUI {

    private final Stick target;

    public ChangeStickTypeGUI(Stick target) {
        super(9, "Box Stick 種類変更");

        this.target = target;

        ButtonIcon withdrawStickIcon = new ButtonIcon(new ItemStack(Material.CHEST))
                .setDisplayName(ChatColor.YELLOW + "Withdraw Stick")
                .setLore(List.of(
                        ChatColor.WHITE + "アイテムを使うと自動でBoxから",
                        ChatColor.WHITE + "補充してくれるBox Stick。"
                ));

        ButtonIcon farmerStickIcon = new ButtonIcon(new ItemStack(Material.IRON_HOE))
                .setDisplayName(ChatColor.YELLOW + "Farmer Stick")
                .setLore(List.of(
                        ChatColor.WHITE + "オフハンドに持つと自動植え直しと",
                        ChatColor.WHITE + "作物の範囲破壊を行えるBox Stick。"
                ));

        buttonList.putButton(1, 0, createStickButton(withdrawStickIcon, StickType.WITHDRAW));
        buttonList.putButton(1, 1, createStickButton(farmerStickIcon, StickType.FARMER));

        setItems();
    }

    private Button createStickButton(ButtonIcon icon, StickType type) {
        return new AbstractButton(icon) {
            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                target.setData(type.createData());
                target.updateHandItem((Player) e.getWhoClicked());
                e.getWhoClicked().closeInventory();
            }

            @Override
            public void update() {
                // 何もしない。
            }
        };
    }
}
