package net.okocraft.boxstick.gui.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;

public final class ButtonIcon {

    private final ItemStack icon;

    public ButtonIcon(ItemStack icon) {
        this.icon = icon;
    }

    public ButtonIcon applyPlaceHolder(Map<String, String> placeholder) {
        setDisplayName(replace(placeholder, getDisplayName()));
        List<String> lore = getLore();
        lore.replaceAll(line -> replace(placeholder, line));
        setLore(lore);
        return this;
    }

    private String replace(Map<String, String> placeholder, String target) {
        if (target != null && !target.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholder.entrySet()) {
                target = target.replaceAll(entry.getKey(), entry.getValue());
            }
            target = ChatColor.translateAlternateColorCodes('&', target);
        }
        return target;
    }

    /**
     * このアイコンを表すアイテムスタックのコピーを取得する。
     * 
     * @return このアイコンのアイテムスタックのコピー
     */
    public ItemStack getItemStack() {
        return icon.clone();
    }

    public ButtonIcon setType(Material material) {
        icon.setType(material);
        return this;
    }

    public Material getType() {
        return icon.getType();
    }

    public ButtonIcon setDisplayName(String displayName) {
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
        }
        icon.setItemMeta(meta);
        return this;
    }

    public String getDisplayName() {
        ItemMeta meta = icon.getItemMeta();
        return meta != null ? meta.getDisplayName() : null;
    }

    public ButtonIcon setLore(List<String> lore) {
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
        }
        icon.setItemMeta(meta);
        return this;
    }

    public List<String> getLore() {
        ItemMeta meta = icon.getItemMeta();
        return meta != null && meta.hasLore() ? meta.getLore() : List.of();
    }

    public ButtonIcon setGlowing(boolean glowing) {
        ItemMeta meta = icon.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (glowing) {
            if (meta.getEnchants().isEmpty()) {
                meta.addEnchant(Enchantment.LURE, 100, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                icon.setItemMeta(meta);
            }
        } else if (icon.getEnchantmentLevel(Enchantment.LURE) == 100) {
            meta.removeEnchant(Enchantment.LURE);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            icon.setItemMeta(meta);
        }
        return this;
    }

    public boolean isGlowing() {
        return icon.getItemMeta() != null && icon.getItemMeta().getEnchants().get(Enchantment.LURE) == 100
                && icon.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    public ButtonIcon setAmount(int amount) {
        icon.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return icon.getAmount();
    }

    public ButtonIcon setHeadOwner(OfflinePlayer player) {
        if (icon.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) icon.getItemMeta();
            meta.setOwningPlayer(player);
            icon.setItemMeta(meta);
        }
        return this;
    }

    public OfflinePlayer getHeadOwner() {
        if (icon.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) icon.getItemMeta();
            return meta.getOwningPlayer();
        }
        return null;
    }
}
