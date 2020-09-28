package net.okocraft.boxstick.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.gui.GUI;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.DataElement;

@EqualsAndHashCode(callSuper = true)
public class SettingButton<T> extends AbstractButton {

    protected final String iconName;
    protected final Stick target;
    protected final DataElement<T> setting;

    public SettingButton(@NotNull DataElement<T> setting, @NotNull Material iconMaterial, @NotNull Stick target) {
        super(new ButtonIcon(new ItemStack(iconMaterial)));
        this.iconName = setting.getDescription() + ": %value"; // 例: 選択している領域のワールド: %world%
        this.target = target;
        this.setting = setting;
        
        setValue(setting.getValue());
    }

    public void setValue(T value) {
        setting.setValue(value);
        icon.setDisplayName(iconName.replace("%value%", String.valueOf(value)));
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        target.updateHandItem((Player) e.getWhoClicked());
        ((GUI) e.getClickedInventory().getHolder()).setItem(e.getSlot());
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }
}
