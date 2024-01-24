package me.gleeming.menu.listener;

import lombok.RequiredArgsConstructor;
import me.gleeming.menu.MenuConfig;
import me.gleeming.menu.MenuHandler;
import me.gleeming.menu.button.Button;
import me.gleeming.menu.button.ButtonType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class MenuListener implements Listener {

    private final Player player;
    private final MenuConfig menu;
    private final Map<Integer, Button> buttonMap;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player) {
            return;
        }

        if (event.getClickedInventory() instanceof PlayerInventory) {
            return;
        }

        event.setCancelled(true);

        Button button = buttonMap.get(event.getSlot());
        if (button == null) {
            return;
        }

        if (button.getType() == ButtonType.EMPTY) {
            event.setCancelled(false);

            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType() == Material.AIR) {
                return;
            }

            if (button.getInsertListener() == null) {
                return;
            }

            event.setCancelled(!button.getInsertListener().inserted(event.getCursor()));
            return;
        }

        if (button.getType() == ButtonType.NEXT_PAGINATION) {
            int currentPage = MenuHandler.getPagination(player.getUniqueId(), menu.getId());
            if (currentPage < menu.getMaxPage()) {
                MenuHandler.setPagination(player.getUniqueId(), menu.getId(), currentPage + 1);
                MenuHandler.getChangingPages().add(player.getUniqueId());
                MenuHandler.openMenu(player, menu.getId());
            }
        }

        if (button.getType() == ButtonType.PREVIOUS_PAGINATION) {
            int currentPage = MenuHandler.getPagination(player.getUniqueId(), menu.getId());
            if (currentPage > 1) {
                MenuHandler.setPagination(player.getUniqueId(), menu.getId(), currentPage - 1);
                MenuHandler.getChangingPages().add(player.getUniqueId());
                MenuHandler.openMenu(player, menu.getId());
            }
        }

        Runnable runnable = button.getActions().get(event.getClick());
        if (runnable == null) {
            return;
        }

        runnable.run();
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() != player) {
            return;
        }

        UUID uuid = player.getUniqueId();
        boolean changingPages = MenuHandler.getChangingPages().contains(uuid);

        if (menu.isPaginated() && !changingPages) {
            MenuHandler.resetPagination(uuid, menu.getId());
        }

        if (changingPages) {
            MenuHandler.getChangingPages().remove(uuid);
        }

        HandlerList.unregisterAll(this);
    }


}
