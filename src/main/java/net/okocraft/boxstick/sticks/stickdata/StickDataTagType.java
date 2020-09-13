package net.okocraft.boxstick.sticks.stickdata;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class StickDataTagType implements PersistentDataType<PersistentDataContainer, StickData> {

    public static final StickDataTagType STICK_DATA = new StickDataTagType();

    private StickDataTagType() {
    }

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<StickData> getComplexType() {
        return StickData.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(StickData stickData, PersistentDataAdapterContext persistentDataAdapterContext) {
        return stickData.deserialize(persistentDataAdapterContext.newPersistentDataContainer());
    }

    @Override
    public StickData fromPrimitive(PersistentDataContainer persistentDataContainer, PersistentDataAdapterContext persistentDataAdapterContext) {
        return StickData.serialize(persistentDataContainer);
    }
}