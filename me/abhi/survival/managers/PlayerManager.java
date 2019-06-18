package me.abhi.survival.managers;

import me.abhi.survival.data.PlayerData;
import me.abhi.survival.manager.Manager;
import me.abhi.survival.manager.ManagerHandler;
import me.abhi.survival.team.Team;
import org.bukkit.entity.Player;

public class PlayerManager extends Manager {

    public PlayerManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void findTeam(Player player) {
        PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
        for (Team team : this.managerHandler.getTeamManager().getTeamList()) {
            if (team.getMembers().contains(player.getUniqueId().toString())) {
                playerData.setTeam(team);
            }
        }
    }
}
