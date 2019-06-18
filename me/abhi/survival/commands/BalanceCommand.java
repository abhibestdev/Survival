package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private Survival plugin;

    public BalanceCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length > 0) {
            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Could not find player!");
                return true;
            }
            PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
            sender.sendMessage(ChatColor.GRAY + target.getName() + "'s " + ChatColor.YELLOW + "balance is " + ChatColor.GREEN + "$" + targetData.getBalance());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        sender.sendMessage(ChatColor.YELLOW + "Your balance is " + ChatColor.GREEN + "$" + playerData.getBalance());
        return true;
    }
}
