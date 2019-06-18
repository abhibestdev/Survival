package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.util.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {

    private Survival plugin;

    public ClaimCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (this.plugin.getManagerHandler().getClaimManager().hasClaim(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You have a claim already. If you would like to claim another piece of land, you must unclaim your current land using the command /unclaim.");
            return true;
        }
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (player.getInventory().contains(Items.CLAIM_WAND.getItem())) {
            player.getInventory().remove(Items.CLAIM_WAND.getItem());
            playerData.setLocation1(null);
            playerData.setLocation2(null);
            player.updateInventory();
            sender.sendMessage(ChatColor.RED + "The claim was cleared!");
            return true;
        }
        player.getInventory().addItem(Items.CLAIM_WAND.getItem());
        player.updateInventory();
        sender.sendMessage(ChatColor.GREEN + "You have been given the claim wand!");
        return true;
    }
}
