package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private Survival plugin;

    public PayCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <player> <amount>");
            return true;
        }
        Player player = (Player) sender;
        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null || target == player) {
            sender.sendMessage(ChatColor.RED + "Could not find player!");
            return true;
        }
        if (!NumberUtils.isDigits(args[1])) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number amount!");
            return true;
        }
        int amount = Integer.parseInt(args[1]);
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        if (playerData.getBalance() < amount) {
            sender.sendMessage(ChatColor.RED + "You have insufficient funds!");
            return true;
        }
        if (targetData.getBalance() + amount > 999999999) {
            sender.sendMessage(ChatColor.RED + "That player would have too big of a balance!");
            return true;
        }
        targetData.setBalance(targetData.getBalance() + amount);
        playerData.setBalance(playerData.getBalance() - amount);
        target.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " has paid you " + ChatColor.GREEN + "$" + amount);
        target.sendMessage(ChatColor.GREEN + "Your balance has been updated to $" + targetData.getBalance() + "!");
        sender.sendMessage(ChatColor.YELLOW + "You have paid " + ChatColor.GRAY + target.getName() + ChatColor.YELLOW + " a total of " + ChatColor.GREEN + "$" + amount);
        sender.sendMessage(ChatColor.GREEN + "Your balance has been updated to $" + playerData.getBalance() + "!");
        return true;
    }
}
