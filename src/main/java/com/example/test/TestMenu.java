package com.example.test;

import me.gleeming.menu.Menu;
import me.gleeming.menu.MenuConfig;
import me.gleeming.menu.MenuHandler;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.button.ButtonType;
import me.gleeming.menu.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class TestMenu extends JavaPlugin implements Listener {

    public void onEnable() {
        MenuHandler.registerMenus(this, this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Menu(id = "test", title = "Test Menu (%p/%mp)", size = 36)
    public void testMen5u(Player player, MenuConfig config) {
        config.button(Button.of(Item.of(Material.ARROW)
                .name("&cPrevious Page")
                .lore(
                        "",
                        "&eGo to the previous page",
                        ""
                )
        ).slot(0).type(ButtonType.PREVIOUS_PAGINATION));

        config.button(Button.of(Item.of(Material.ARROW)
                .name("&aNext Page")
                .lore(
                        "",
                        "&eGo to the next page",
                        ""
                )
        ).slot(8).type(ButtonType.NEXT_PAGINATION));

        for (int slot = 1; slot < 8; slot++) {
            config.button(Button.of(Item.of(Material.BLACK_STAINED_GLASS_PANE).name(" ")).slot(slot));
        }

        config.paginateRange(9, 36);
        for (int i = 0; i < 58; i++) {
            config.paginated(Bukkit.getOnlinePlayers().stream()
                    .map(skull -> Button.of(Item.of(Material.PLAYER_HEAD)
                            .skull(skull)
                            .name(String.format("&a%s", skull.getName()))
                            .lore(
                                    "",
                                    String.format("&eClick to teleport to &a%s&e.", skull.getName()),
                                    ""
                            )).action(ClickType.LEFT, () -> player.teleport(skull)))
                    .collect(Collectors.toList()));
        }
    }

    @Menu(id = "example", title = "Test Menu", size = 36)
    public void exampleMenu(Player player, MenuConfig config) {
        config.button(Button.of(Item.of(Material.BAMBOO)
                .name("&f&lHELLO")
                .lore(
                        "",
                        "&chello",
                        ""
                )
        ).slot(2).action(() -> MenuHandler.openMenu(player, "example2"), ClickType.DROP, ClickType.RIGHT));
    }

    @Menu(id = "example2", title = "Test Menu", size = 36)
    public void exampleMenu2(Player player, MenuConfig config) {
        config.button(Button.of(Item.of(Material.BAMBOO)
                .name("&f&lHELLO")
                .lore(
                        "",
                        "&chello",
                        ""
                )
        ).slot(5).action(() -> MenuHandler.openMenu(player, "example"), ClickType.DROP, ClickType.RIGHT));
    }

    @Menu(id = "test2", title = "Test Menu", size = 36)
    public void depositCommand(Player player, MenuConfig config) {
        config.button(Button.of(Item.of(Material.BAMBOO)
                .name("&f&lInsert Your Note")
                .lore(
                        "",
                        "&ePut your note in the slot to the right.",
                        ""
                )
        ).slot(3));

        config.button(Button.of(Item.of(Material.AIR))
                .type(ButtonType.EMPTY).slot(4)
                .inserted(item -> {
                    if (item.getType() != Material.PAPER) {
                        // prevents the item from being put in the gui
                        return false;
                    }

                    if (!item.getItemMeta().getDisplayName().contains("Bank Note")) {
                        return false;
                    }

                    // item#whateverYouWant
                    return true;
                })
        );
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getMessage().equals("menu")) {
            return;
        }

        Bukkit.getScheduler().runTask(this, () -> MenuHandler.openMenu(event.getPlayer(), "test"));

    }
}
