package net.okocraft.boxstick.gui.button;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import lombok.EqualsAndHashCode;
import net.okocraft.box.Box;

@EqualsAndHashCode
public abstract class AbstractButton implements Button {

    protected static final Box PLUGIN = Box.getInstance();

    protected ButtonIcon icon;

    public AbstractButton(@NotNull ButtonIcon icon) {
        this.icon = icon;
    }

    @NotNull
    @Override
    public ButtonIcon getIcon() {
        return icon;
    }

    @Override
    public void setIcon(ButtonIcon icon) {
        this.icon = icon;
    }

    protected static ButtonIcon createHeadIcon(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return new ButtonIcon(head);
    }
}
