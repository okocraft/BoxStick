package net.okocraft.boxstick.gui.button;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.okocraft.boxstick.gui.GUI;

public class PreviousPageButton extends AbstractButton {

    private int guiPage = 1;

    public PreviousPageButton() {
        super(
            new ButtonIcon(new ItemStack(Material.ARROW))
                    .setDisplayName(ChatColor.GOLD + "前のページ")
                    .setLore(List.of())
                    .setGlowing(false) // default value
        );

        //TODO: ぷれほる
        icon.applyPlaceHolder(null);
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        GUI holder = (GUI) e.getInventory().getHolder();
        guiPage--;
        holder.setPage(guiPage);
        guiPage = holder.getPage();
        update();
    }

    @Override
    public void update() {
        // TODO: set lore or name depending on gui page.
    }
    
}
