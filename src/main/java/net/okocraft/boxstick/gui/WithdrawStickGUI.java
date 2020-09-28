package net.okocraft.boxstick.gui;

import org.bukkit.Material;

import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.StickData;
import net.okocraft.boxstick.sticks.stickdata.WithdrawStickData;

public class WithdrawStickGUI extends StickSettingGUI {
    
    public WithdrawStickGUI(Stick stick) {
        super(9, "Withdraw Stick - 設定画面", stick);
        StickData stickData = stick.getData();
        if (!(stickData instanceof WithdrawStickData)) {
            throw new IllegalArgumentException("Type of stick is not withdraw.");
        }
        WithdrawStickData data = (WithdrawStickData) stickData;

        buttonList.putButton(1, 0, createBoolSettingButton(data.getOnPlaceBlock(), Material.BRICKS, stick));
        buttonList.putButton(1, 2, createBoolSettingButton(data.getOnEatFood(), Material.COOKED_BEEF, stick));
        buttonList.putButton(1, 4, createBoolSettingButton(data.getOnThrowPotion(), Material.POTION, stick));
        buttonList.putButton(1, 6, createBoolSettingButton(data.getOnBreakItem(), Material.IRON_PICKAXE, stick));
        buttonList.putButton(1, 8, otherStickButton);

        setItems();
    }
}
