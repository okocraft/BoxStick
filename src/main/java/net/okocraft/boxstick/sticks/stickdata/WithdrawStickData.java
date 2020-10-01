package net.okocraft.boxstick.sticks.stickdata;

import java.util.List;

import org.bukkit.NamespacedKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawStickData extends StickData {

    public StickType getType() {
        return StickType.WITHDRAW;
    }

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
}