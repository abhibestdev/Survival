package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import me.abhi.survival.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCreateSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamCreateSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " create <name>");
            return true;
        }
        if (this.plugin.getManagerHandler().getTeamManager().teamExists(args[1])) {
            sender.sendMessage(ChatColor.RED + "That team already exists!");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.hasTeam()) {
            sender.sendMessage(ChatColor.RED + "You are already in a team!");
            return true;
        }
        if (args[1].length() < 3 || args[1].length() > 10) {
            sender.sendMessage(ChatColor.RED + "Team names must be between 3 and 10 characters.");
            return true;
        }
        if (!Util.isAlpha(args[1])) {
            sender.sendMessage(ChatColor.RED + "Team names may only contain letters!");
            return true;
        }
        if (playerData.getBalance() < 250) {
            sender.sendMessage(ChatColor.RED + "It costs $250 to make a team. You need $" + (250 - playerData.getBalance()) + " more.");
            return true;
        }
        Team team = new Team(args[1], player.getUniqueId());
        this.plugin.getManagerHandler().getTeamManager().addTeam(team);
        playerData.setBalance(playerData.getBalance() - 250);
        playerData.setTeam(team);
        this.plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Team " + ChatColor.BLUE + args[1] + ChatColor.YELLOW + " has been created by " + ChatColor.GRAY + sender.getName());
        sender.sendMessage(ChatColor.GREEN + "$250 has been taken out of your balance.");
        return true;
    }
}
