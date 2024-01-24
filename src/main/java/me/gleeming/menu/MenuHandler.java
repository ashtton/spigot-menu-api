package me.gleeming.menu;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.listener.MenuListener;
import me.gleeming.menu.method.MenuMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

public class MenuHandler {

    private static Plugin plugin;
    private static final Map<String, MenuMethod> menus = new HashMap<>();
    private static final Map<UUID, Map<String, Integer>> pagination = new HashMap<>();
    @Getter private static final List<UUID> changingPages = new ArrayList<>();

    public static void register(Plugin plugin) {
        MenuHandler.plugin = plugin;
    }

    @SneakyThrows
    public static void openMenu(Player player, String id) {
        MenuMethod method = menus.get(id);

        if (method == null) {
            plugin.getLogger().log(Level.SEVERE, String.format("Invalid menu '%s'.%n", id));
            return;
        }

        MenuConfig menuConfig = MenuConfig.of(id, method.getMenu().size());
        menuConfig.title(method.getMenu().title());

        method.getMethod().invoke(method.getParent(), player, menuConfig);

        if (menuConfig.isPaginated()) {
            menuConfig.title(menuConfig.getTitle()
                    .replaceAll("%p", String.valueOf(getPagination(player.getUniqueId(), id)))
                    .replaceAll("%mp", String.valueOf(menuConfig.getMaxPage())));
        }

        Inventory inventory = Bukkit.createInventory(null, menuConfig.getSize(), menuConfig.getTitle());
        Map<Integer, Button> buttonMap = new HashMap<>();

        menuConfig.getButtons().forEach(button -> buttonMap.put(button.getSlot(), button));

        if (menuConfig.isPaginated()) {
            int page = getPagination(player.getUniqueId(), id);
            if (page > menuConfig.getMaxPage()) {
                if (page == 1) {
                    plugin.getLogger().log(Level.SEVERE, String.format("Invalid page '%s' for '%s'.", page, id));
                    return;
                }

                page = 1;
                setPagination(player.getUniqueId(), id, 1);
            }

            Iterator<Integer> paginatedSlot = menuConfig.getPaginatedSlots().iterator();
            menuConfig.getPagination().get(page).forEach(button -> buttonMap.put(paginatedSlot.next(), button));
        }

        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItem().get()));

        player.openInventory(inventory);
        Bukkit.getPluginManager().registerEvents(new MenuListener(player, menuConfig, buttonMap), plugin);
    }

    public static void registerMenus(Plugin plugin, Object obj) {
        register(plugin);
        Arrays.stream(obj.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Menu.class) != null)
                .forEach(method -> {
                    Menu menu = method.getAnnotation(Menu.class);
                    menus.put(menu.id(), new MenuMethod(menu, method, obj));
                });
    }

    public static int getPagination(UUID uuid, String menu) {
        return pagination.getOrDefault(uuid, new HashMap<>()).getOrDefault(menu, 1);
    }

    public static void setPagination(UUID uuid, String menu, int page) {
        Map<String, Integer> pages = pagination.getOrDefault(uuid, new HashMap<>());
        pages.put(menu, page);
        pagination.put(uuid, pages);
    }

    public static void resetPagination(UUID uuid, String id) {
        pagination.getOrDefault(uuid, new HashMap<>()).remove(id);
    }
}
