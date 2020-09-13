package net.okocraft.boxstick.sticks.stickdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.okocraft.boxstick.sticks.StickItem;

public class WithdrawStickData extends StickData {

    private static final NamespacedKey PLACE_BLOCK_KEY = new NamespacedKey(PLUGIN, "onplaceblock");
    private static final NamespacedKey EAT_FOOD_KEY = new NamespacedKey(PLUGIN, "oneatfood");
    private static final NamespacedKey BREAK_ITEM_KEY = new NamespacedKey(PLUGIN, "onbreakitem");
    private static final NamespacedKey THROW_POTION_KEY = new NamespacedKey(PLUGIN, "onthrowpotion");

    private boolean onPlaceBlock = true;
    private boolean onEatFood = true;
    private boolean onBreakItem = true;
    private boolean onThrowPotion = true;
    
    WithdrawStickData(PersistentDataContainer container) {
        this.onPlaceBlock = toBool(orElse(container.get(PLACE_BLOCK_KEY, PersistentDataType.BYTE), (byte) 1));
        this.onEatFood = toBool(orElse(container.get(EAT_FOOD_KEY, PersistentDataType.BYTE), (byte) 1));
        this.onBreakItem = toBool(orElse(container.get(BREAK_ITEM_KEY, PersistentDataType.BYTE), (byte) 1));
        this.onThrowPotion = toBool(orElse(container.get(THROW_POTION_KEY, PersistentDataType.BYTE), (byte) 1));
    }
    
    public WithdrawStickData() {
    }
    
    public WithdrawStickData(boolean onPlaceBlock, boolean onEatFood, boolean onBreakItem, boolean onThrowPotion) {
        this.onPlaceBlock = onPlaceBlock;
        this.onEatFood = onEatFood;
        this.onBreakItem = onBreakItem;
        this.onThrowPotion = onThrowPotion;
    }

    public boolean getOnPlaceBlock() {
        return this.onPlaceBlock;
    }

    public void setOnPlaceBlock(boolean onPlaceBlock) {
        this.onPlaceBlock = onPlaceBlock;
    }

    public boolean getOnEatFood() {
        return this.onEatFood;
    }

    public void setOnEatFood(boolean onEatFood) {
        this.onEatFood = onEatFood;
    }

    public boolean getOnBreakItem() {
        return this.onBreakItem;
    }

    public void setOnBreakItem(boolean onBreakItem) {
        this.onBreakItem = onBreakItem;
    }

    public boolean getOnThrowPotion() {
        return this.onThrowPotion;
    }

    public void setOnThrowPotion(boolean onThrowPotion) {
        this.onThrowPotion = onThrowPotion;
    }

    @Override
    PersistentDataContainer deserialize(PersistentDataContainer container) {
        container.set(STICK_TYPE, PersistentDataType.STRING, StickType.WITHDRAW.name());
        container.set(PLACE_BLOCK_KEY, PersistentDataType.BYTE, toByte(onPlaceBlock));
        container.set(EAT_FOOD_KEY, PersistentDataType.BYTE, toByte(onEatFood));
        container.set(BREAK_ITEM_KEY, PersistentDataType.BYTE, toByte(onBreakItem));
        container.set(THROW_POTION_KEY, PersistentDataType.BYTE, toByte(onThrowPotion));
        return container;
    }

    private static final List<String> defLore = List.of(
        "&f種類&7: &eWithdraw Stick",
        "&f効果&7: &e自動でアイテムを手持ちに補充します。",
        "&f設定&7:",
        "&7- &fブロック設置&7: &b%onplaceblock%",
        "&7- &f食料消費&7: &b%oneatfood%",
        "&7- &fポーション投擲&7: &b%onthrowpotion%",
        "&7- &f耐久値全損&7: &b%onbreakitem%"
    );

    @Override
    public void update(StickItem itemHolder) {
        itemHolder.setData(this);
        List<String> lore = new ArrayList<>(defLore);
        lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line
                .replace("%onplaceblock%", String.valueOf(onPlaceBlock))
                .replace("%oneatfood%", String.valueOf(onEatFood))
                .replace("%onthrowpotion%", String.valueOf(onThrowPotion))
                .replace("%onbreakitem%", String.valueOf(onBreakItem))
        ));
        itemHolder.setLore(lore);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WithdrawStickData)) {
            return false;
        }
        WithdrawStickData withdrawStickData = (WithdrawStickData) o;
        return onPlaceBlock == withdrawStickData.onPlaceBlock
                && onEatFood == withdrawStickData.onEatFood
                && onBreakItem == withdrawStickData.onBreakItem
                && onThrowPotion == withdrawStickData.onThrowPotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(onPlaceBlock, onEatFood, onBreakItem, onThrowPotion);
    }


}