package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamInfoSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamInfoSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Team team = null;
        if (args.length < 2) {
            Player player = (Player) sender;
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (!playerData.hasTeam()) {
                sender.sendMessage(ChatColor.RED + "You are not on a team!");
                return true;
            }
            team = playerData.getTeam();
        } else {
            if (!this.plugin.getManagerHandler().getTeamManager().teamExists(args[1])) {
                sender.sendMessage(ChatColor.RED + "That team does not exist!");
                return true;
            }
            team = this.plugin.getManagerHandler().getTeamManager().getTeam(args[1]);
        }
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "That team does not exist!");
            return true;
        }
        List<String> names = new ArrayList<>();
        for (String members : team.getMembers()) {
            UUID uuid = UUID.fromString(members);
            if (!uuid.equals(team.getLeader())) {
                names.add(this.plugin.getServer().getOfflinePlayer(uuid).getName());
            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
        sender.sendMessage(ChatColor.YELLOW + "Team: " + ChatColor.GRAY + team.getName());
        sender.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.GRAY + this.plugin.getServer().getOfflinePlayer(team.getLeader()).getName());
        sender.sendMessage(ChatColor.YELLOW + "Members: " + ChatColor.GRAY + String.join(",", names));
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
        return true;
    }
}
