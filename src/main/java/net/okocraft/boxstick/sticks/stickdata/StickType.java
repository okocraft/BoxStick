package net.okocraft.boxstick.sticks.stickdata;

import java.util.function.Supplier;

public enum StickType {
    WITHDRAW(WithdrawStickData::new),
    FARMER(FarmerStickData::new),
    COPY(CopyStickData::new);

    private final Supplier<? extends StickData> constructor;

    StickType(Supplier<? extends StickData> constructor) {
        this.constructor = constructor;
    }

    public StickData createData() {
        return constructor.get();
    }
}
