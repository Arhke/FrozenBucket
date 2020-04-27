package net.waterraid.GenBucket;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args){
        if (commandSender instanceof Player){
            ((Player)commandSender).openInventory(GUI.getInven());
            commandSender.sendMessage(ChatColor.BLUE+"Opening GenBuckets GUI");
            ((Player)commandSender).playSound(((Player) commandSender).getLocation(), Sound.NOTE_PLING, 1, 1);
        }
        return true;
    }

}
