package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomySetSubCommand implements CommandExecutor {

    private Survival plugin;

    public EconomySetSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " set <player> <amount>");
            return true;
        }
        if (args[1].equalsIgnoreCase("*")) {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                this.plugin.getServer().dispatchCommand(sender, commandLabel + " " + args[0] + " " + player.getName() + " " + args[2]);
            }
        } else {
            Player target = this.plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Could not find player!");
                return true;
            }
            if (!NumberUtils.isDigits(args[2])) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid number amount!");
                return true;
            }
            if (Integer.parseInt(args[2]) > 999999999) {
                sender.sendMessage(ChatColor.RED + "That amount is too large!");
                return true;
            }
            PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
            targetData.setBalance(Integer.parseInt(args[2]));
            target.sendMessage(ChatColor.GREEN + "Your balance has been updated to $" + args[2] + "!");
            sender.sendMessage(ChatColor.GREEN + target.getName() + "'s balance has been updated to $" + args[2] + "!");
        }
        return true;
    }
}
