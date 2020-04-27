package net.waterraid.GenBucket;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Events implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.isCancelled()) {
            if (event.getItem() != null && event.getItem().getType() == Material.LAVA_BUCKET) {
                if (event.getItem().getItemMeta().getLore() != null) {
                    String[] specs = event.getItem().getItemMeta().getLore().get(event.getItem().getItemMeta().getLore().size() - 1).replaceAll(ChatColor.BLACK.toString(), "").split(",");
                    if (specs != null && specs.length == 3) {
                        Material mat;
                        try {
                            mat = Material.valueOf(specs[1]);
                        } catch (IllegalArgumentException e) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Invalid Block Material Check Config");
                            return;
                        }
                        event.setCancelled(true);
                        if (!mat.isBlock()) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Invalid Block Material Check Config");
                            return;
                        }
                        Integer cost = Integer.parseInt(specs[2]);
                        cost = cost == null ? 0 : cost;
                        if (Main.getEconomy() == null || !Main.getEconomy().withdrawPlayer(event.getPlayer(), cost).transactionSuccess()) {
                            event.getPlayer().sendMessage("Not Enough Funds to Use This Item!");
                            return;
                        }
                        String type = specs[0];
                        event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!] -$" + cost);
                        if (type == null) {
                            event.getPlayer().sendMessage(ChatColor.RED + "No Valid Direction Type Check Config");
                            return;
                        } else if (type.equalsIgnoreCase("horizontal")) {
                            Vector vector = event.getPlayer().getEyeLocation().getDirection();
                            int xorz = Math.abs(vector.getX()) > Math.abs(vector.getZ()) ? 1 : 0;
                            new BucketScheduling(mat, getWouldbe(event.getClickedBlock().getLocation(), event.getBlockFace()), 1 - xorz, 0, xorz).runTaskTimer(Main.instance, 10L, 20L);
                            new BucketScheduling(mat, getWouldbe(event.getClickedBlock().getLocation(), event.getBlockFace()), xorz - 1, 0, -xorz).runTaskTimer(Main.instance, 10L, 20L);
                        } else if (type.equalsIgnoreCase("vertical")) {
                            new BucketScheduling(mat, getWouldbe(event.getClickedBlock().getLocation(), event.getBlockFace()), 0, 1, 0).runTaskTimer(Main.instance, 10L, 20L);
                            new BucketScheduling(mat, getWouldbe(event.getClickedBlock().getLocation(), event.getBlockFace()), 0, -1, 0).runTaskTimer(Main.instance, 10L, 20L);
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + "Unknown Direction Type Check Config");
                            return;
                        }
                    }
                }
            }
        }
    }

    public static Location getWouldbe(Location loc, BlockFace bf) {
        return new Location(loc.getWorld(), loc.getX() + bf.getModX(), loc.getY() + bf.getModY(), loc.getZ() + bf.getModZ());
    }

    @EventHandler
    public void onPlayerClickInven(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GUI) {
            int slot = event.getRawSlot();
            if (event.getRawSlot() >= 0 && event.getRawSlot() <= 26 && event.getCurrentItem().getType() == Material.LAVA_BUCKET) {
                event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
            }
            event.setCancelled(true);
        }
    }
}
