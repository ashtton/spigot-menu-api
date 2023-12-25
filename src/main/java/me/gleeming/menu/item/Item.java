package me.gleeming.menu.item;

import lombok.RequiredArgsConstructor;
import me.gleeming.menu.color.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public class Item {

    private final ItemStack itemStack;

    public Item amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public Item name(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Color.translate(name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item lore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore.stream().map(Color::translate).collect(Collectors.toList()));
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item lore(String... lore) {
        lore(Arrays.asList(lore));
        return this;
    }

    public Item skull(OfflinePlayer skullOwner) {
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(skullOwner);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack get() {
        return itemStack;
    }

    public static Item of(Material material) {
        return of(new ItemStack(material));
    }
}
