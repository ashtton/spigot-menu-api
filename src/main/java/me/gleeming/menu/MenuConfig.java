package me.gleeming.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.type.MenuType;

import java.util.*;

@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@Getter
public class MenuConfig {

    private final String id;
    private final int size;

    private String title;

    private MenuType menuType = MenuType.NORMAL;

    private final List<Button> buttons = new ArrayList<>();

    private final List<Button> paginatedItems = new ArrayList<>();
    private final List<Integer> paginatedSlots = new ArrayList<>();

    public MenuConfig title(String title) {
        this.title = title;
        return this;
    }

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

    public int getMaxPage() {
        return getPagination().size();
    }

    public Map<Integer, List<Button>> getPagination() {
        Map<Integer, List<Button>> pages = new HashMap<>();
        int page = 1;

        for (Button button : paginatedItems) {
            List<Button> buttons = pages.getOrDefault(page, new ArrayList<>());

            if (paginatedSlots.size() == buttons.size()) {
                page++;
                buttons = pages.getOrDefault(page, new ArrayList<>());
            }

            buttons.add(button);
            pages.put(page, buttons);
        }

        return pages;
    }

    public boolean isPaginated() {
        return getPaginatedSlots().size() > 0;
    }
}
