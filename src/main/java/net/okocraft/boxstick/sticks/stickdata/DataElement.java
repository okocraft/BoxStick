package net.okocraft.boxstick.sticks.stickdata;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class DataElement<T> {

    @NotNull
    private final NamespacedKey key;
    
    @NotNull
    private final String description;
    
    @Getter(AccessLevel.PACKAGE)
    @NotNull
    private final PersistentDataType<?, T> dataType;
    
    @NotNull
    private T value;

    void setValueFrom(PersistentDataContainer container) {
        setValue(container.getOrDefault(key, dataType, value));
    }

    void passValueTo(PersistentDataContainer container) {
        container.set(key, dataType, value);
    }

}
