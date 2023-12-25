package me.gleeming.menu.listener;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class MenuListener implements Listener {

    private final Player player;
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

        HandlerList.unregisterAll(this);
    }


}
