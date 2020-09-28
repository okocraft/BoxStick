package net.okocraft.boxstick.sticks;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.sticks.stickdata.DataTypes;
import net.okocraft.boxstick.sticks.stickdata.StickData;

/**
 * 基本構造
 * 　アイテムスタックとのアダプターになるクラス。マテリアルは棒で固定する。
 * 実装
 * 　getItemStack()
 * 　　　格納しているアイテムを取得する。
 * 　createItem(StickData data)
 * 　　　データに基づいてアイテムスタックを作る。
 * 　isStick(ItemStack item)
 * 　　　itemがStickか判定する。PersistentDataContainerの有無で確認。
 * 　　　なお、種類の判別はgetData().method() (名称未定) で行うことにする。
 * 
 */
public class StickItem {

    protected static final BoxStick PLUGIN = JavaPlugin.getPlugin(BoxStick.class);
    private static final NamespacedKey BOX_STICK_KEY = new NamespacedKey(PLUGIN, "boxstick");

    private final ItemStack item;

    public StickItem(@NotNull StickData data) {
        this.item = new ItemStack(Material.STICK);
        data.update(this);
    }

    public StickItem(@NotNull ItemStack item) throws IllegalArgumentException {
        this.item = item;
        if (!isStick(item)) {
            throw new IllegalArgumentException("This item is not boxstick: " + item.toString());
        }
    }

    @Nullable
    public static StickData getDataOf(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer()
                .get(BOX_STICK_KEY, DataTypes.STICK_DATA);
    }

    public static boolean isStick(ItemStack item) {
        return item.getType() == Material.STICK && getDataOf(item) != null;
    }

    public ItemStack getItemStack() {
        return item.clone();
    }

    public void setData(StickData data) {
        useSetter(meta -> meta.getPersistentDataContainer().set(BOX_STICK_KEY, DataTypes.STICK_DATA, data));
    }

    public void setLore(List<String> lore) {
        useSetter(meta -> meta.setLore(lore));
    }

    public void setDisplayName(@Nullable String displayName) {
        useSetter(meta -> meta.setDisplayName(displayName));
    }

    private void useSetter(Consumer<ItemMeta> setter) {
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Stored ItemStack may not be stick (ItemMeta was null).");
        setter.accept(meta);
        item.setItemMeta(meta);
    }
}
