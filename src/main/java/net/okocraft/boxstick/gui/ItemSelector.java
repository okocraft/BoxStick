package net.okocraft.boxstick.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.okocraft.box.Box;
import net.okocraft.box.config.Categories;
import net.okocraft.boxstick.gui.button.AbstractButton;
import net.okocraft.boxstick.gui.button.Button;
import net.okocraft.boxstick.gui.button.ButtonIcon;
import net.okocraft.boxstick.gui.button.ChangeGUIButton;

public class ItemSelector extends GUI {

    private static final Button FRAME = createFrame();

    public ItemSelector(Consumer<ItemStack> itemConsumer) {
        super(54, "アイテム選択");

        List<Integer> frameSlots = new ArrayList<>();
        List<Integer> nonFrameSlots = new ArrayList<>();
        for (int column = 0; column <= 8; column++) {
            for (int row = 1; row <= 6; row++) {
                ((column == 0 || column == 8 || row == 1 || row == 6) ? frameSlots : nonFrameSlots).add(column * row);
            }
        }
        frameSlots.forEach(slot -> buttonList.putButton(1, slot, FRAME));

        Iterator<Integer> categoryButtonSlots = nonFrameSlots.iterator();
        Categories categories = Box.getInstance().getAPI().getCategories();
        categories.getCategories().forEach(category -> {
            if (!categoryButtonSlots.hasNext()) {
                return;
            }

            GUI categoryGUI = new GUI(54, category) {
            };
            categoryGUI.putElementAndPageArrow(categories.getItems(category).stream()
                    .map(item -> createItemConsumerButton(item, itemConsumer)).collect(Collectors.toList()));
            buttonList.putButton(1, categoryButtonSlots.next(),
                    new ChangeGUIButton(new ButtonIcon(categories.getIcon(category)), categoryGUI));
        });

        setItems();
    }

    private static Button createFrame() {
        return new AbstractButton(new ButtonIcon(new ItemStack(Material.GRAY_STAINED_GLASS_PANE))) {
            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                // 何もしない。
            }

            @Override
            public void update() {
                // 何もしない。
            }
        };
    }

    private static Button createItemConsumerButton(ItemStack item, Consumer<ItemStack> consumer) {
        return new AbstractButton(new ButtonIcon(item)) {
            @Override
            public void onClick(@NotNull InventoryClickEvent e) {
                consumer.accept(item);
            }

            @Override
            public void update() {
                // 何もしない。
            }
        };
    }
}
