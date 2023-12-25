package me.gleeming.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.type.MenuType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@Getter
public class MenuConfig {

    private final String title;
    private final int size;

    private MenuType menuType = MenuType.NORMAL;

    private final List<Button> buttons = new ArrayList<>();

    private final List<Button> paginatedItems = new ArrayList<>();
    private final List<Integer> paginatedSlots = new ArrayList<>();

    public MenuConfig button(Button button) {
        buttons.add(button);
        return this;
    }

    public MenuConfig buttons(List<Button> buttons) {
        this.buttons.addAll(buttons);
        return this;
    }

    public MenuConfig buttons(Button... buttons) {
        buttons(Arrays.asList(buttons));
        return this;
    }

    public MenuConfig paginate(Integer... slots) {
        paginatedSlots.addAll(Arrays.asList(slots));
        menuType = MenuType.PAGINATED;
        return this;
    }

    public MenuConfig paginateRange(Integer start, Integer end) {
        for(int i = start; i < end; i++) {
            paginatedSlots.add(i);
        }

        menuType = MenuType.PAGINATED;
        return this;
    }

    public MenuConfig paginateExcept(Integer... slots) {
        List<Integer> slotsList = Arrays.asList(slots);
        for(int i = 0; i < size; i++) {
            if (slotsList.contains(i)) {
                continue;
            }

            paginatedSlots.add(i);
        }

        menuType = MenuType.PAGINATED;
        return this;
    }

    public MenuConfig paginated(Button button) {
        paginatedItems.add(button);
        return this;
    }

    public MenuConfig paginated(List<Button> buttons) {
        paginatedItems.addAll(buttons);
        return this;
    }

}
