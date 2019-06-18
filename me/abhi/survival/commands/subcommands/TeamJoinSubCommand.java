package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.TeamInvite;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamJoinSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamJoinSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " join <player>");
            return true;
        }
        Player player = (Player) sender;
        Player target = this.plugin.getServer().getPlayer(args[1]);
        if (target == null || target == player) {
            sender.sendMessage(ChatColor.RED + "Could not find player!");
            return true;
        }
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        if (!targetData.hasTeam()) {
            sender.sendMessage(ChatColor.RED + "That player is not in a team!");
            return true;
        }
        TeamInvite foundInvite = null;
        boolean hasInvite = false;
        for (TeamInvite teamInvite : playerData.getTeamInviteList()) {
            if (teamInvite.getTeam() == targetData.getTeam() && (System.currentTimeMillis() - teamInvite.getTimestamp() <= 60000)) {
                hasInvite = true;
                foundInvite = teamInvite;
            }
        }
        if (hasInvite) {
            if (playerData.hasTeam()) {
                sender.sendMessage(ChatColor.RED + "You are already in a team!");
                return true;
            }
            foundInvite.getTeam().getMembers().add(player.getUniqueId().toString());
            playerData.setTeam(foundInvite.getTeam());
            for (String uudis : foundInvite.getTeam().getMembers()) {
                UUID uuid = UUID.fromString(uudis);
                Player members = this.plugin.getServer().getPlayer(uuid);
                if (members != null) {
                    members.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " has joined the team!");
                }
            }
            playerData.getTeamInviteList().remove(foundInvite);
            return true;
        }
        sender.sendMessage(ChatColor.RED + "That invite was not found!");
        return true;
    }
}
