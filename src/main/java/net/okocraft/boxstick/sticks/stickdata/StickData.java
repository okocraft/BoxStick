package net.okocraft.boxstick.sticks.stickdata;

import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.sticks.StickItem;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class StickData {

    protected static final BoxStick PLUGIN = JavaPlugin.getPlugin(BoxStick.class);
    protected static final NamespacedKey STICK_TYPE = new NamespacedKey(PLUGIN, "sticktype");

    public abstract void update(StickItem itemHolder);
    
    public abstract List<DataElement<?>> getElements();

    PersistentDataContainer deserialize(PersistentDataContainer container) {
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
