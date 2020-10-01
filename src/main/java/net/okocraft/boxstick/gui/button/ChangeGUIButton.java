package net.okocraft.boxstick.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.gui.GUI;

@EqualsAndHashCode(callSuper = true)
public class ChangeGUIButton extends AbstractButton {
    
    private final GUI gui;
    
    public ChangeGUIButton(ButtonIcon icon, GUI gui) {
        super(icon);
        this.gui = gui;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);
        e.getWhoClicked().openInventory(gui.getInventory());
    }

    @Override
    public void update() {
        // 何もしない。
    }
}
