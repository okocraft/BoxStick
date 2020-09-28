package net.okocraft.boxstick.gui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.gui.button.AbstractButton;
import net.okocraft.boxstick.gui.button.ButtonIcon;
import net.okocraft.boxstick.gui.button.SettingButton;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.DataElement;

@EqualsAndHashCode(callSuper = true)
public class StickSettingGUI extends GUI {

    protected final Stick stick;
    protected final AbstractButton otherStickButton;

    public StickSettingGUI(int size, String title, Stick stick) {
        super(size, title);
        this.stick = stick;

        otherStickButton = createOtherStickButton(stick);
    }

    private static AbstractButton createOtherStickButton(Stick target) {
        return new AbstractButton(new ButtonIcon(new ItemStack(Material.STICK))
                .setDisplayName(ChatColor.RED + "別の種類の棒に変更").setLore(List.of(ChatColor.RED + "注意. 現在の設定は破棄されます"))) {

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new ChangeStickTypeGUI(target).getInventory());
            }

            @Override
            public void update() {
                // 何もしない。
            }
        };
    }

    protected static <T> SettingButton<T> createUnclickableSettingButton(DataElement<T> setting, Material icon,
            Stick target) {
        return new SettingButton<>(setting, icon, target) {

            @Override
            public void onClick(InventoryClickEvent e) {
                e.setCancelled(true);
            };
        };
    }

    protected static SettingButton<Boolean> createBoolSettingButton(DataElement<Boolean> setting, Material icon,
            Stick target) {
        return new SettingButton<>(setting, icon, target) {

            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                setValue(!setting.getValue());
                super.onClick(e);
            }
        };
    }

}
