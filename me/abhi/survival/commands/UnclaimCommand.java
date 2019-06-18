package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.claim.Claim;
import me.abhi.survival.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnclaimCommand implements CommandExecutor {

    private Survival plugin;

    public UnclaimCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length <= 0) {
            Player player = (Player) sender;
            if (!this.plugin.getManagerHandler().getClaimManager().hasClaim(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "You do not have a claim!");
                return true;
            }
            Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(player.getUniqueId());
            int cost = claim.getCost();
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            playerData.setBalance(playerData.getBalance() + cost);
            this.plugin.getManagerHandler().getClaimManager().removeClaim(claim);
            sender.sendMessage(ChatColor.RED + "Your claim has been deleted.");
            sender.sendMessage(ChatColor.GREEN + "You have been refunded $" + cost + "!");
        } else if (args.length > 0 && sender.hasPermission("survival.admin")) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "Could not find player!");
                return true;
            }
            if (!this.plugin.getManagerHandler().getClaimManager().hasClaim(offlinePlayer.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "That player does not have a claim!");
                return true;
            }
            Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(offlinePlayer.getUniqueId());
            this.plugin.getManagerHandler().getClaimManager().removeClaim(claim);
            sender.sendMessage(ChatColor.YELLOW + "You have deleted the claim of " + ChatColor.GRAY + offlinePlayer.getName() + ChatColor.YELLOW + ".");
            return true;
        }
        return true;
    }
}
