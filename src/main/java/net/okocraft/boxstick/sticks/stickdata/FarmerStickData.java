package net.okocraft.boxstick.sticks.stickdata;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FarmerStickData extends StickData {
    
    public StickType getType() {
        return StickType.FARMER;
    }

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
}