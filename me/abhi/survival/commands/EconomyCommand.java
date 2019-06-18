package me.abhi.survival.commands;

import me.abhi.survival.Survival;
import me.abhi.survival.commands.subcommands.EconomyGiveSubCommand;
import me.abhi.survival.commands.subcommands.EconomySetSubCommand;
import me.abhi.survival.commands.subcommands.EconomyTakeSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EconomyCommand implements CommandExecutor {

    private Survival plugin;
    private EconomySetSubCommand economySetSubCommand;
    private EconomyGiveSubCommand economyGiveSubCommand;
    private EconomyTakeSubCommand economyTakeSubCommand;

    public EconomyCommand(Survival plugin) {
        this.plugin = plugin;
        this.economySetSubCommand = new EconomySetSubCommand(this.plugin);
        this.economyGiveSubCommand = new EconomyGiveSubCommand(this.plugin);
        this.economyTakeSubCommand = new EconomyTakeSubCommand(this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("survival.command.economy")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " set <player> <amount>" + ChatColor.GRAY + " - " + ChatColor.YELLOW + "Set a player's balance");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " give <player> <amount>" + ChatColor.GRAY + " - " + ChatColor.YELLOW + "Add to a player's balance");
            sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " take <player> <amount>" + ChatColor.GRAY + " - " + ChatColor.YELLOW + "Take from a player's balance");
            sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------------------------------------------");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "set": {
                economySetSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "give": {
                economyGiveSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
            case "take": {
                economyTakeSubCommand.onCommand(sender, cmd, commandLabel, args);
                break;
            }
        }
        return true;
    }
}
