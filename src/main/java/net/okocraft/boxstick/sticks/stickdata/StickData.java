package net.okocraft.boxstick.sticks.stickdata;

import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.sticks.StickItem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class StickData {

    protected static final BoxStick PLUGIN = JavaPlugin.getPlugin(BoxStick.class);
    protected static final NamespacedKey STICK_TYPE = new NamespacedKey(PLUGIN, "sticktype");

    public abstract StickType getType();

    public void update(StickItem itemHolder) {
        itemHolder.setData(this);

        List<String> lore = new ArrayList<>(getType().getStickDescription());
        lore.add("&f設定&7:");
        getElements().forEach(element -> lore.add("&7- &f" + element.getDescription() + "&7: &b" + element.getValue()));
        lore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
        itemHolder.setLore(lore);
    }
    
    public abstract List<DataElement<?>> getElements();

    PersistentDataContainer deserialize(PersistentDataContainer container) {
        container.set(STICK_TYPE, PersistentDataType.STRING, getType().name());
        getElements().forEach(element -> element.passValueTo(container));
        return container;
    }

    static StickData serialize(PersistentDataContainer container) {
        StickType type;
        try {
            type = StickType.valueOf(container.getOrDefault(STICK_TYPE, PersistentDataType.STRING, "WITHDRAW"));
        } catch (IllegalArgumentException e) {
            type = StickType.WITHDRAW;
        }
        StickData serialized = type.createData();
        serialized.getElements().forEach(element -> element.setValueFrom(container));
        return serialized;
    }
}
