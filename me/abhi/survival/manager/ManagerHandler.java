package me.abhi.survival.manager;

import me.abhi.survival.Survival;
import me.abhi.survival.managers.*;

public class ManagerHandler {

    private Survival plugin;
    private PlayerDataManager playerDataManager;
    private ScoreboardManager scoreboardManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private ClaimManager claimManager;

    public ManagerHandler(Survival plugin) {
        this.plugin = plugin;
        registerManagers();
    }

    private void registerManagers() {
        this.playerDataManager = new PlayerDataManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.teamManager = new TeamManager(this);
        this.playerManager = new PlayerManager(this);
        this.claimManager = new ClaimManager(this);
    }

    public Survival getPlugin() {
        return plugin;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ClaimManager getClaimManager() {
        return claimManager;
    }
}
