package me.abhi.survival.managers;

import me.abhi.survival.data.PlayerData;
import me.abhi.survival.manager.Manager;
import me.abhi.survival.manager.ManagerHandler;
import me.abhi.survival.team.Team;
import me.abhi.survival.util.ScoreHelper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager extends Manager {

    public ScoreboardManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void update(Player player) {
        if (ScoreHelper.hasScore(player) && this.managerHandler.getPlayerDataManager().hasPlayerData(player)) {
            PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
            ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
            scoreHelper.setSlotsFromList(getSlots(player));
            if (playerData.hasTeam()) {
                Team team = playerData.getTeam();
                for (Player all : this.managerHandler.getPlugin().getServer().getOnlinePlayers()) {
                    scoreHelper.addEnemy(all);
                    if (team.getMembers().contains(all.getUniqueId().toString())) {
                        if (scoreHelper.hasEnemy(all)) {
                            scoreHelper.remvoeEnemy(all);
                            scoreHelper.addFriendly(all);
                        }
                    } else if (scoreHelper.hasFriendly(all)) {
                        scoreHelper.removeFriendly(all);
                        scoreHelper.addEnemy(all);
                    }
                }
            } else {
                for (Player all : this.managerHandler.getPlugin().getServer().getOnlinePlayers()) {
                    if (!scoreHelper.hasEnemy(all)) {
                        scoreHelper.addEnemy(all);
                    }
                }
            }
        }
    }

    public List<String> getSlots(Player player) {
        List<String> slots = new ArrayList<>();
        if (ScoreHelper.hasScore(player) && this.managerHandler.getPlayerDataManager().hasPlayerData(player)) {
            ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
            PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
            scoreHelper.setTitle("&e&lSURVIVAL");
            slots.add(" ");
            slots.add("&eStats");
            slots.add(" Kills: &e" + playerData.getKills());
            slots.add(" Deaths: &e" + playerData.getDeaths());
            slots.add(" Balance: &e$" + playerData.getBalance());
            slots.add(" ");
            if (playerData.hasTeam()) {
                slots.add("&eTeam:");
                slots.add(" Name: &e" + playerData.getTeam().name);
                slots.add(" Members: &e" + playerData.getTeam().getMembers().size());
                slots.add(" ");
            }
            slots.add("&eazur.es");
        }
        return slots;
    }
}
