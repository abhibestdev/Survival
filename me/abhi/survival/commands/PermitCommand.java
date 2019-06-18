package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.claim.Claim;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermitCommand implements CommandExecutor {

    private Survival plugin;

    public PermitCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <player>");
            return true;
        }
        Player player = (Player) sender;
        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null || target == player) {
            sender.sendMessage(ChatColor.RED + "Could not find player!");
            return true;
        }
        if (!this.plugin.getManagerHandler().getClaimManager().hasClaim(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You do not have a claim!");
            return true;
        }
        Claim claim = this.plugin.getManagerHandler().getClaimManager().getClaim(player.getUniqueId());
        if (claim.getPermitees().contains(target.getUniqueId())) {
            claim.getPermitees().remove(target.getUniqueId());
            sender.sendMessage(ChatColor.RED + "You have removed that person from your claim!");
            return true;
        }
        claim.getPermitees().add(target.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + target.getName() + ChatColor.YELLOW + " has been added to the claim/");
        return true;
    }
}
