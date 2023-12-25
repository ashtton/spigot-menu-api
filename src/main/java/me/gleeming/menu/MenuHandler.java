package me.gleeming.menu;

import lombok.SneakyThrows;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.listener.MenuListener;
import me.gleeming.menu.method.MenuMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MenuHandler {

    private static Plugin plugin;
    private static final Map<String, MenuMethod> menus = new HashMap<>();

    public static void register(Plugin plugin) {
        MenuHandler.plugin = plugin;
    }

    @SneakyThrows
    public static void openMenu(Player player, String id) {
        MenuMethod method = menus.get(id);

        if (method == null) {
            System.out.printf("Invalid menu '%s'.%n", id);
            return;
        }

        MenuConfig menuConfig = MenuConfig.of(method.getMenu().title(), method.getMenu().size());
        method.getMethod().invoke(method.getParent(), player, menuConfig);

        Inventory inventory = Bukkit.createInventory(null, menuConfig.getSize(), menuConfig.getTitle());
        Map<Integer, Button> buttonMap = new HashMap<>();

        menuConfig.getButtons().forEach(button -> buttonMap.put(button.getSlot(), button));

        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItem().get()));

        player.openInventory(inventory);
        Bukkit.getPluginManager().registerEvents(new MenuListener(player, buttonMap), plugin);
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
}
