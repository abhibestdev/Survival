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

public class TeamLeaveSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamLeaveSubCommand(Survival plugin) {
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
        if (team.getLeader().equals(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You are the team leader! You must promote someone or disband the party.");
            return true;
        }
        team.getMembers().remove(player.getUniqueId().toString());
        for (String uudis : team.getMembers()) {
            UUID uuid = UUID.fromString(uudis);
            Player members = this.plugin.getServer().getPlayer(uuid);
            if (members != null) {
                members.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " has left the team!");
            }
        }
        playerData.setTeam(null);
        sender.sendMessage(ChatColor.RED + "You have left the team!");
        return true;
    }
}
