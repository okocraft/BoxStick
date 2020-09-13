package net.okocraft.boxstick.gui.button;

import java.util.Objects;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import net.okocraft.boxstick.gui.GUI;

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


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ChangeGUIButton)) {
            return false;
        }
        ChangeGUIButton changeGUIButton = (ChangeGUIButton) o;
        return Objects.equals(gui, changeGUIButton.gui);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gui);
    }
}
