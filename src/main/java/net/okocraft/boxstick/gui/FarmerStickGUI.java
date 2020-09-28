package net.okocraft.boxstick.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.okocraft.boxstick.gui.button.SettingButton;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.StickData;
import net.okocraft.boxstick.sticks.stickdata.FarmerStickData;

public class FarmerStickGUI extends StickSettingGUI {

    public FarmerStickGUI(Stick stick) {
        super(9, "Farmer Stick - 設定画面", stick);

        StickData stickData = stick.getData();
        if (!(stickData instanceof FarmerStickData)) {
            throw new IllegalArgumentException("Type of stick is not famer.");
        }
        FarmerStickData data = (FarmerStickData) stickData;

        buttonList.putButton(1, 0, createBoolSettingButton(data.getProtectYoungPlants(), Material.WHEAT_SEEDS, stick));
        buttonList.putButton(1, 2, createBoolSettingButton(data.getProtectFarmLand(), Material.FARMLAND, stick));
        buttonList.putButton(1, 4, createBoolSettingButton(data.getProtectWhenNoStock(), Material.BARRIER, stick));
        buttonList.putButton(1, 6, new SettingButton<Integer>(data.getHarvestingRange(), Material.TNT, stick) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.isRightClick()) {
                    setting.setValue(Math.min(4, setting.getValue() + 1));
                } else {
                    setting.setValue(Math.max(0, setting.getValue() - 1));
                }
                super.onClick(e);
            }
        });
        buttonList.putButton(1, 8, otherStickButton);

        setItems();
    }
}
