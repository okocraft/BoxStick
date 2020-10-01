package net.okocraft.boxstick.sticks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.okocraft.boxstick.sticks.stickdata.StickData;
import net.okocraft.boxstick.sticks.stickdata.StickType;

/**
 * 基本構造
 * 　アイテムスタックとスティックデータを格納。
 * 　スティックの種類はデータで決定する。
 * 　実装メソッド
 * 　　ok updateItem()
 * 　　　データに合わせて格納したスタックをアプデ。
 * 　　ok getItem()
 * 　　　格納したアイテムスタックのラッパーを取得。ラッパーにより、アイテムスタックの変更を制限。
 * 　　ok getData()
 * 　　　格納したStickDataを取得。
 * 　　ok setData()
 * 　　　StickDataを設定する。これを実装するかは悩み中...。あってもいいとは思う。
 * 　　　実行時にアイテムをアプデする。
 * 　　ok Stick(StickData data) コンストラクタ
 * 　　　データを格納したStickクラスのインスタンスを作成
 * 　　ok Stick(StickItem stick) コンストラクタ
 * 　　　アイテムスタックから自動的にデータを取得し、インスタンスを作成
 * 　　　アイテムがisStick() == falseとなる場合、例外を投げる。
 * 
 * 　備考
 * 　　StickDataの変更時に強制的にアイテムのUpdateを実行するとなると
 * 　　StickDataにもStickのインスタンスが必要となり、現実的でない。
 * 　　getItem()実行時にアプデが良いか。　　
 */
public class Stick {

    protected final StickItem stickItem;
    protected StickData stickData;

    public Stick(StickItem item) {
        this.stickItem = item;
        this.stickData = StickItem.getDataOf(stickItem.getItemStack());
    }

    public Stick(StickData data) {
        this.stickData = data;
        this.stickItem = new StickItem(stickData);
    }

    public Stick(StickType stickType) {
        this(stickType.createData());
    }

    public void updateHandItem(Player player, EquipmentSlot hand) {
        stickData.update(stickItem);
        player.getInventory().setItem(hand, stickItem.getItemStack());
    }

    public void updateItemAndInventory(Inventory inv) {
        ItemStack from = stickItem.getItemStack();
        stickData.update(stickItem);
        ItemStack to = stickItem.getItemStack();
        replaceItem(inv, from, to);
    }

    private static int firstSimilar(Inventory inv, ItemStack item) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = inv.getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void replaceItem(Inventory inv, @NotNull ItemStack from, @NotNull ItemStack to) {
        if (from.isSimilar(to)) {
            return;
        }
        to = to.clone();

        int first;
        while ((first = firstSimilar(inv, from)) != -1) {
            to.setAmount(inv.getItem(first).getAmount());
            inv.setItem(first, to);
        }
    }

    public StickItem getItem() {
        return stickItem;
    }

    public StickData getData() {
        return stickData;
    }

    public void setData(StickData data) {
        this.stickData = data;
    }
}
