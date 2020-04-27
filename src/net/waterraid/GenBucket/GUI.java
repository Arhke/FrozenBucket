package net.waterraid.GenBucket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class GUI implements InventoryHolder {
    private static Inventory gui;
    public static ItemStack[] itemStacks;
    public static Inventory getInven() {
        if (gui == null) {
            gui = Bukkit.createInventory(new GUI(), 27, ChatColor.BOLD +""+ ChatColor.WHITE + "Frozen " + ChatColor.AQUA+"Buckets");
            gui.setItem(10,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3));
            gui.setItem(17,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3));
            gui.setItem(13,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3));
            for (int i = 0; i <= 8; i+=2) {
                gui.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0));
                gui.setItem(i+1,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3));
            }for (int i = 17; i <= 26; i+=2) {
                gui.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3));
                gui.setItem(i+1,new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0));
            }
            for (int i = 0; i < itemStacks.length; i++)
                if (itemStacks[i] != null)
                    gui.setItem(i, itemStacks[i]);
        }
        return gui;
    }

    public static ItemStack craftItem(String name, String lore, String block, String genBucketType, int cost) {
        ItemStack is = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name == null?ChatColor.RED+"GenBucket":name.replace('&', ChatColor.COLOR_CHAR));
        List<String> lores = new ArrayList<>();
        lore = lore == null?"A "+genBucketType+ " GenBucket of " + block + " (cost:$"+cost+")":lore.replace('&', ChatColor.COLOR_CHAR);
        String[] addlore = lore.split("\n");
        for (String add:addlore)
            lores.add(add);
        lores.add(ChatColor.BLACK + block +","+ genBucketType + "," + cost);
        im.setLore(lores);
        is.setItemMeta(im);
        return is;
    }
    @Override
    public Inventory getInventory() {
        return gui;
    }
}
