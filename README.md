# Spigot Menu API
Lightweight easy to use spigot menu api.
* Easily create paginated menus
* Create menus with no flicker updates
* Never have to stare at ugly gui code again

```java
// Main plugin class
public class MainClass extends JavaPlugin {
    
    public void onEnable() {
        MenuHandler.registerMenus(DepositCommand.class, this);
        MenuHandler.registerMenus(OnlineCommand.class, this);
    }
}


// Example command class
// Demonstrates how you can have menus linked.
public class ExampleCommand {
    @Command(names = {"example"}, playerOnly = true)
    public void exampleCommand(Player player) {
        MenuHandler.openMenu(player, "example");
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
}

// Deposit command class
// shows how you can insert 
// items into the menus
public class DepositCommand {
    @Command(names = {"deposit"}, playerOnly = true)
    public void depositCommand(Player player) {
        MenuHandler.openMenu(player, "deposit");
    }
    
    @Menu(id = "deposit", title = "Deposit", size = 9)
    public void depositMenu(Player player, MenuConfig config) {
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
}

// Online command class
// Shows how paginated menus work
public class OnlineCommand {
    @Command(names = {"online"}, playerOnly = true)
    public void onlineCommand(Player player) {
        MenuHandler.openMenu(player, "online");
    }
    
    // Creates a menu of all the skulls 
    // of the online players and allows
    // you to teleport to whoever you want.
    
    @Menu(id = "online", title = "Online Players (%p/%mp)", size = 27)
    public void onlineMenu(Player player, MenuConfig config) {
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
```