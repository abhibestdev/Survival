package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private Survival plugin;
    private TeamCreateSubCommand teamCreateSubCommand;
    private TeamInviteSubCommand teamInviteSubCommand;
    private TeamJoinSubCommand teamJoinSubCommand;
    private TeamKickSubCommand teamKickSubCommand;
    private TeamPromoteSubCommand teamPromoteSubCommand;
    private TeamLeaveSubCommand teamLeaveSubCommand;
    private TeamDisbandSubCommand teamDisbandSubCommand;
    private TeamInfoSubCommand teamInfoSubCommand;

    public TeamCommand(Survival plugin) {
        this.plugin = plugin;
        this.teamCreateSubCommand = new TeamCreateSubCommand(this.plugin);
        this.teamInviteSubCommand = new TeamInviteSubCommand(this.plugin);
        this.teamJoinSubCommand = new TeamJoinSubCommand(this.plugin);
        this.teamKickSubCommand = new TeamKickSubCommand(this.plugin);
        this.teamPromoteSubCommand = new TeamPromoteSubCommand(this.plugin);
        this.teamLeaveSubCommand = new TeamLeaveSubCommand(this.plugin);
        this.teamDisbandSubCommand = new TeamDisbandSubCommand(this.plugin);
        this.teamInfoSubCommand = new TeamInfoSubCommand(this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " create <name> " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Create a Team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " invite <player> " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Invite a player to a team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " join <player> " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Join a player's team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " kick <player> " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Kick a player from your team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " promote <player> " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Promote player to leader of your team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " leave " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Leave your team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " disband " + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Disband your team");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " info <name>" + ChatColor.GRAY + "- " + ChatColor.YELLOW + "Show team info");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "NOTE: CREATING A TEAM WILL TAKE $250 OUT YOUR BALANCE");
            sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "create": {
                teamCreateSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "invite": {
                teamInviteSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "join": {
                teamJoinSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "kick": {
                teamKickSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "promote": {
                teamPromoteSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "leave": {
                teamLeaveSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "disband": {
                teamDisbandSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "info" : {
                teamInfoSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
        }
        return true;
    }
}
