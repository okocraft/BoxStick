package net.okocraft.boxstick.sticks.stickdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.okocraft.boxstick.sticks.StickItem;

public class FarmerStickData extends StickData {

    private static final NamespacedKey PROTECT_YOUNG_PLANT_KEY = new NamespacedKey(PLUGIN, "protectyoungplants");
    private static final NamespacedKey PROTECT_FARM_LAND_KEY = new NamespacedKey(PLUGIN, "protectfarmland");
    private static final NamespacedKey PROTECT_WHEN_NO_STOCK_KEY = new NamespacedKey(PLUGIN, "protectwhennostock");
    private static final NamespacedKey WIDE_HARVESTING_KEY = new NamespacedKey(PLUGIN, "harvestingrange");

    private boolean protectYoungPlants = true;
    private boolean protectFarmLand = true;
    private boolean protectWhenNoStock = true;
    private int harvestingRange = 1;

    FarmerStickData(PersistentDataContainer container) {
        this.protectYoungPlants = toBool(orElse(container.get(PROTECT_YOUNG_PLANT_KEY, PersistentDataType.BYTE), (byte) 1));
        this.protectFarmLand = toBool(orElse(container.get(PROTECT_FARM_LAND_KEY, PersistentDataType.BYTE), (byte) 1));
        this.protectWhenNoStock = toBool(orElse(container.get(PROTECT_WHEN_NO_STOCK_KEY, PersistentDataType.BYTE), (byte) 1));
        this.harvestingRange = orElse(container.get(WIDE_HARVESTING_KEY, PersistentDataType.INTEGER), 1);
    }

    public FarmerStickData() {
    }

    public FarmerStickData(boolean protectYoungPlants, boolean protectFarmLand, boolean protectWhenNoStock, int harvestingRange) {
        this.protectYoungPlants = protectYoungPlants;
        this.protectFarmLand = protectFarmLand;
        this.protectWhenNoStock = protectWhenNoStock;
        this.harvestingRange = harvestingRange;
    }

    public boolean getProtectYoungPlants() {
        return protectYoungPlants;
    }

    public void setProtectYoungPlants(boolean protectYoungPlants) {
        this.protectYoungPlants = protectYoungPlants;
    }

    public boolean getProtectFarmLand() {
        return protectFarmLand;
    }

    public void setProtectFarmLand(boolean protectFarmLand) {
        this.protectFarmLand = protectFarmLand;
    }

    public boolean getProtectWhenNoStock() {
        return protectWhenNoStock;
    }

    public void setProtectWhenNoStock(boolean protectWhenNoStock) {
        this.protectWhenNoStock = protectWhenNoStock;
    }
    
    public int getHarvestingRange() {
        return harvestingRange;
    }
    
    public void setHarvestingRange(int harvestingRange) {
        this.harvestingRange = harvestingRange;
    }

    @Override
    PersistentDataContainer deserialize(PersistentDataContainer container) {
        container.set(STICK_TYPE, PersistentDataType.STRING, StickType.FARMER.name());
        container.set(PROTECT_YOUNG_PLANT_KEY, PersistentDataType.BYTE, toByte(protectYoungPlants));
        container.set(PROTECT_FARM_LAND_KEY, PersistentDataType.BYTE, toByte(protectFarmLand));
        container.set(PROTECT_WHEN_NO_STOCK_KEY, PersistentDataType.BYTE, toByte(protectWhenNoStock));
        container.set(WIDE_HARVESTING_KEY, PersistentDataType.INTEGER, harvestingRange);
        return container;
    }

    private static final List<String> defLore = List.of(
        "&f種類&7: &eFarmer Stick",
        "&f効果&7: &e自動で作物を植え直したり、作物を効率よく収穫します。",
        "      &e範囲破壊は平面の耕地に植わった作物のみ有効です。",
        "&f設定&7:",
        "&7- &f若い作物の保護&7: &b%protectyoungplants%",
        "&7- &f作物がある地面の保護&7: &b%protectfarmland%",
        "&7- &f在庫不足で保護&7: &b%protectwhennostock%",
        "&7- &f作物破壊範囲&7: &b%harvestingrange%"
    );

    @Override
    public void update(StickItem itemHolder) {
        itemHolder.setData(this);
        List<String> lore = new ArrayList<>(defLore);
        lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&',
                line.replace("%protectyoungplants%", String.valueOf(protectYoungPlants))
                        .replace("%protectfarmland%", String.valueOf(protectFarmLand))
                        .replace("%protectwhennostock%", String.valueOf(protectWhenNoStock))
                        .replace("%harvestingrange%", String.valueOf(harvestingRange))));
        itemHolder.setLore(lore);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FarmerStickData)) {
            return false;
        }
        FarmerStickData withdrawStickData = (FarmerStickData) o;
        return protectYoungPlants == withdrawStickData.protectYoungPlants
                && protectWhenNoStock == withdrawStickData.protectWhenNoStock
                && harvestingRange == withdrawStickData.harvestingRange;
    }

    @Override
    public int hashCode() {
        return Objects.hash(protectYoungPlants, protectWhenNoStock, harvestingRange);
    }


}