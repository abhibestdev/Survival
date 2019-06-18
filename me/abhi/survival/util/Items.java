package me.abhi.survival.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Items {

    CLAIM_WAND(new ItemBuilder(Material.GOLD_HOE).setName(ChatColor.GOLD + "Claim Wand").setLore("&aLeft Click Block: Set first point", "&aRight Click Block: Set second point", "&aShift + Right Click Air: Clear Claim", "&aShift + Left Click Air: Claim Land").toItemStack());

    private ItemStack item;

    private Items(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
