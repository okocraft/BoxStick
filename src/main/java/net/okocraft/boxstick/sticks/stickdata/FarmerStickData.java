package net.okocraft.boxstick.sticks.stickdata;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.okocraft.boxstick.sticks.StickItem;

@Data
@EqualsAndHashCode(callSuper = true)
public class FarmerStickData extends StickData {

    private final DataElement<Boolean> protectYoungPlants = new DataElement<>(
        new NamespacedKey(PLUGIN, "protectyoungplants"),
        "育ちきっていない作物を保護するか",
        DataTypes.BOOL,
        false
    );

    private final DataElement<Boolean> protectFarmLand = new DataElement<>(
        new NamespacedKey(PLUGIN, "protectfarmland"),
        "作物が植わっている地面を保護するか",
        DataTypes.BOOL,
        false
    );

    private final DataElement<Boolean> protectWhenNoStock = new DataElement<>(
        new NamespacedKey(PLUGIN, "protectwhennostock"),
        "種がないときに収穫しないようにするか",
        DataTypes.BOOL,
        false
    );

    private final DataElement<Integer> harvestingRange = new DataElement<>(
        new NamespacedKey(PLUGIN, "harvestingrange"),
        "作物の破壊範囲",
        PersistentDataType.INTEGER,
        0
    );

    private final List<DataElement<?>> elements = List.of(protectYoungPlants, protectFarmLand, protectWhenNoStock, harvestingRange);

    @Override
    public void update(StickItem itemHolder) {
        itemHolder.setData(this);

        List<String> lore = new ArrayList<>(List.of(
            "&f種類&7: &eFarmer Stick",
            "&f効果&7: &e自動で作物を植え直したり、作物を効率よく収穫します。",
            "      &e範囲破壊は平面の耕地に植わった作物のみ有効です。",
            "&f設定&7:"
        ));
        elements.forEach(element -> lore.add("&7- &f" + element.getDescription() + "&7: &b" + element.getValue()));
        lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
        itemHolder.setLore(lore);
    }
}