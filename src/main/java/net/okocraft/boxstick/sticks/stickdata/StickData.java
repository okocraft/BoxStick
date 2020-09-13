package net.okocraft.boxstick.sticks.stickdata;

import net.okocraft.boxstick.BoxStick;
import net.okocraft.boxstick.sticks.StickItem;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class StickData {

    
    protected static final BoxStick PLUGIN = JavaPlugin.getPlugin(BoxStick.class);
    
    protected static final NamespacedKey STICK_TYPE = new NamespacedKey(PLUGIN, "sticktype");

    public abstract void update(StickItem itemHolder);

    abstract PersistentDataContainer deserialize(PersistentDataContainer container);

    static StickData serialize(PersistentDataContainer container) {
        return StickType.valueOf(container.get(STICK_TYPE, PersistentDataType.STRING))
                .serialize(container);
    }

    protected static <T> T orElse(T value, T def) {
        return value != null ? value : def;
    }

    protected static boolean toBool(byte value) {
        return value == (byte) 1;
    }

    protected static byte toByte(boolean value) {
        return (byte) (value ? 1 : 0);
    }
}
