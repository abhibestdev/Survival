package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPromoteSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamPromoteSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " promote <player>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasTeam()) {
            sender.sendMessage(ChatColor.RED + "You are not on a team!");
            return true;
        }
        Team team = playerData.getTeam();
        if (!team.getLeader().equals(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You must be team leader to do this!");
            return true;
        }
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[1]);
        if (offlinePlayer == null || offlinePlayer.getUniqueId() == player.getUniqueId() || !team.getMembers().contains(offlinePlayer.getUniqueId().toString())) {
            sender.sendMessage(ChatColor.RED + "Team member not found!");
            return true;
        }
        team.setLeader(offlinePlayer.getUniqueId());
        for (String uudis : team.getMembers()) {
            UUID uuid = UUID.fromString(uudis);
            Player members = this.plugin.getServer().getPlayer(uuid);
            if (members != null) {
                members.sendMessage(ChatColor.GRAY + offlinePlayer.getName() + ChatColor.YELLOW + " has been promoted to team leader!");
            }
        }
        return true;
    }
}
