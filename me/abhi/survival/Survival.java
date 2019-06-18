package me.abhi.survival;

import me.abhi.survival.commands.*;
import me.abhi.survival.data.PlayerData;
import me.abhi.survival.listener.PlayerListener;
import me.abhi.survival.manager.ManagerHandler;
import me.abhi.survival.runnable.ScoreboardRunnable;
import me.abhi.survival.runnable.TNTRunnable;
import me.abhi.survival.util.ScoreHelper;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Survival extends JavaPlugin {

    private static Survival instance;
    private ManagerHandler managerHandler;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        registerManagers();
        registerPlayers();
        registerListeners();
        registerCommands();
        registerRunnables();
    }

    @Override
    public void onDisable() {
        save();
    }

    private void registerManagers() {
        managerHandler = new ManagerHandler(this);
    }

    private void registerPlayers() {
        for (Player player : getServer().getOnlinePlayers()) {
            this.managerHandler.getPlayerDataManager().addPlayer(player);
            ScoreHelper.createScore(player);
            this.managerHandler.getPlayerManager().findTeam(player);
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100D);
        }
    }


    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerCommands() {
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("unclaim").setExecutor(new UnclaimCommand(this));
        getCommand("permit").setExecutor(new PermitCommand(this));
    }

    private void registerRunnables() {
        new ScoreboardRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
        new TNTRunnable().runTaskTimerAsynchronously(this, 0L, 0L);
    }

    private void save() {
        this.managerHandler.getTeamManager().save();
        this.managerHandler.getClaimManager().save();
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
            playerData.save();
        }
    }

    public static Survival getInstance() {
        return instance;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
