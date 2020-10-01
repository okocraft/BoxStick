package net.okocraft.boxstick.sticks.stickdata;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum StickType {
    WITHDRAW(WithdrawStickData::new, List.of(
        "&f種類&7: &eWithdraw Stick",
        "&f効果&7: &e自動でアイテムを手持ちに補充します。"
    )),
    FARMER(FarmerStickData::new, List.of(
        "&f種類&7: &eFarmer Stick",
        "&f効果&7: &e自動で作物を植え直したり、作物を効率よく収穫します。",
        "      &e範囲破壊は平面の耕地に植わった作物のみ有効です。"
    )),
    COPY(CopyStickData::new, List.of(
        "&f種類&7: &eCopy Stick",
        "&f効果&7: &e選択範囲をコピー＆ペーストします。",
        "      &eオフハンドに持ってクリックでpos1、",
        "      &e右クリックでpos2を選択します。",
        "      &eオフハンドに持ってシフトを押しながら右クリで貼り付けます。",
        "      &e※1 消滅したブロックはBoxに入りません。",
        "      &e※2 Boxにないブロックは空気になります。"
    ));

    private final Supplier<? extends StickData> constructor;

    private final List<String> stickDescription;

    StickType(Supplier<? extends StickData> constructor, List<String> stickDescription) {
        this.constructor = constructor;
        this.stickDescription = stickDescription;
    }

    public StickData createData() {
        return constructor.get();
    }

    public String getStickName() {
        return Character.toUpperCase(name().charAt(0)) + name().substring(1).toLowerCase(Locale.ROOT) + " Stick";
    }

    public List<String> getStickDescription() {
        return stickDescription;
    }
}
