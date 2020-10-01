package net.okocraft.boxstick.sticks.stickdata;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.sticks.StickItem;

@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawStickData extends StickData {

    private final DataElement<Boolean> onPlaceBlock = new DataElement<>(
        new NamespacedKey(PLUGIN, "onplaceblock"),
        "ブロック設置時に自動で補充するか",
        DataTypes.BOOL,
        true
    );

    private final DataElement<Boolean> onEatFood = new DataElement<>(
        new NamespacedKey(PLUGIN, "oneatfood"),
        "食料などのアイテム消費時に自動で補充するか",
        DataTypes.BOOL,
        true
    );

    private final DataElement<Boolean> onBreakItem = new DataElement<>(
        new NamespacedKey(PLUGIN, "onbreakitem"),
        "道具の破損時に自動で補充するか",
        DataTypes.BOOL,
        true
    );

    private final DataElement<Boolean> onThrowPotion = new DataElement<>(
        new NamespacedKey(PLUGIN, "onthrowpotion"),
        "ポーションの投擲時に自動で補充するか",
        DataTypes.BOOL,
        true
    );

    private final List<DataElement<?>> elements = List.of(onPlaceBlock, onEatFood, onBreakItem, onThrowPotion);
    
    @Override
    public void update(StickItem itemHolder) {
        itemHolder.setData(this);

        List<String> lore = new ArrayList<>(List.of(
            "&f種類&7: &eWithdraw Stick",
            "&f効果&7: &e自動でアイテムを手持ちに補充します。",
            "&f設定&7:"
        ));
        elements.forEach(element -> lore.add("&7- &f" + element.getDescription() + "&7: &b" + element.getValue()));
        lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
        itemHolder.setLore(lore);
    }
}