package net.okocraft.boxstick.sticks.stickdata;

import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.persistence.PersistentDataContainer;

public enum StickType {
    WITHDRAW(WithdrawStickData::new, WithdrawStickData::new),
    FARMER(FarmerStickData::new, FarmerStickData::new);

    private final Supplier<? extends StickData> defaultSupplier;
    private final Function<PersistentDataContainer, ? extends StickData> serializer;

    StickType(Supplier<? extends StickData> defaultSupplier, Function<PersistentDataContainer, ? extends StickData> serializer) {
        this.defaultSupplier = defaultSupplier;
        this.serializer = serializer;
    }

    StickData serialize(PersistentDataContainer container) {
        return serializer.apply(container);
    }

    public StickData createData() {
        return defaultSupplier.get();
    }
}
