package me.gleeming.menu.button;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.gleeming.menu.button.listener.InsertListener;
import me.gleeming.menu.item.Item;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class Button {

    private final Item item;

    private final Map<ClickType, Runnable> actions = new HashMap<>();

    private int slot;

    private ButtonType type = ButtonType.NORMAL;

    private InsertListener insertListener;

    public Button inserted(InsertListener listener) {
        this.insertListener = listener;
        return this;
    }

    public Button slot(int slot) {
        this.slot = slot;
        return this;
    }

    public Button type(ButtonType type) {
        this.type = type;
        return this;
    }

    public Button action(ClickType click, Runnable runnable) {
        actions.put(click, runnable);
        return this;
    }

    public Button action(Runnable runnable, ClickType... clicks) {
        Arrays.asList(clicks).forEach(type -> actions.put(type, runnable));
        return this;
    }

}
