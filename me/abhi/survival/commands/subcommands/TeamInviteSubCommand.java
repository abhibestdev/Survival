package me.abhi.survival.commands.subcommands;

import me.abhi.survival.Survival;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.team.Team;
import me.abhi.survival.team.TeamInvite;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamInviteSubCommand implements CommandExecutor {

    private Survival plugin;

    public TeamInviteSubCommand(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " invite <player>");
            return true;
        }
        Player target = this.plugin.getServer().getPlayer(args[1]);
        Player player = (Player) sender;
        if (target == null || target == player) {
            sender.sendMessage(ChatColor.RED + "Could not find player!");
            return true;
        }
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasTeam()) {
            sender.sendMessage(ChatColor.RED + "You are not on a team!");
            return true;
        }
        Team team = playerData.getTeam();
        if (!team.getLeader().equals(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You must be the team leader to do this!");
            return true;
        }
        if (team.getMembers().contains(target.getUniqueId().toString())) {
            sender.sendMessage(ChatColor.RED + "That player is already on the team!");
            return true;
        }
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        boolean hasInvite = false;
        for (TeamInvite teamInvite : targetData.getTeamInviteList()) {
            if (teamInvite.getTeam() == playerData.getTeam() && (System.currentTimeMillis() - teamInvite.getTimestamp() <= 60000)) {
                hasInvite = true;
            }
        }
        if (hasInvite) {
            sender.sendMessage(ChatColor.RED + "You already have an existing invite sent to that player. You must wait for that to expire before sending another one!");
            return true;
        }
        TeamInvite teamInvite = new TeamInvite(playerData.getTeam());
        targetData.getTeamInviteList().add(teamInvite);
        for (String uudis : playerData.getTeam().getMembers()) {
            UUID uuid = UUID.fromString(uudis);
            Player members = this.plugin.getServer().getPlayer(uuid);
            if (members != null) {
                members.sendMessage(ChatColor.GRAY + target.getName() + ChatColor.YELLOW + " has been invited to the team!");
            }
        }
        TextComponent inviteMessage = new TextComponent(ChatColor.GRAY + player.getName() + ChatColor.YELLOW + " has invited you to join the team " + ChatColor.BLUE + playerData.getTeam().getName() + ChatColor.GRAY + "[Accept]");
        inviteMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to join the team.").create()));
        inviteMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team join " + player.getName()));
        target.spigot().sendMessage(inviteMessage);
        return true;
    }
}
