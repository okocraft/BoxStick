package net.okocraft.boxstick.gui;

import java.util.ArrayList;
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

        buttonList.putButton(1, 0, createStickButton(Material.CHEST, StickType.WITHDRAW));
        buttonList.putButton(1, 1, createStickButton(Material.IRON_HOE, StickType.FARMER));

        setItems();
    }

    private Button createStickButton(Material iconMaterial, StickType type) {
        List<String> iconLore = new ArrayList<>(type.getStickDescription());
        iconLore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
        ButtonIcon icon = new ButtonIcon(new ItemStack(iconMaterial))
                .setDisplayName(ChatColor.YELLOW + type.getStickName())
                .setLore(iconLore);

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
