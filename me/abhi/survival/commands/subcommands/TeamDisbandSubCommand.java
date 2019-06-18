package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamDisbandSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamDisbandSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
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
        String name = team.getName();
        for (String uudis : team.getMembers()) {
            UUID uuid = UUID.fromString(uudis);
            Player members = this.plugin.getServer().getPlayer(uuid);
            if (members != null) {
                PlayerData memberData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(members);
                memberData.setTeam(null);
                members.sendMessage(ChatColor.RED + "The team has been disbanded!");
            }
        }
        this.plugin.getManagerHandler().getTeamManager().removeTeam(team);
        this.plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Team " + ChatColor.BLUE + name + ChatColor.YELLOW + " has been disbanded by " + ChatColor.GRAY + sender.getName());
        return true;
    }
}
