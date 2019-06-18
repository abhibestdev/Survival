package me.abhi.survival.managers;

import me.abhi.survival.manager.Manager;
import me.abhi.survival.manager.ManagerHandler;
import me.abhi.survival.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager extends Manager {

    private List<Team> teamList;

    public TeamManager(ManagerHandler managerHandler) {
        super(managerHandler);
        teamList = new ArrayList<>();
        loadTeams();
    }

    private void loadTeams() {
        if (this.managerHandler.getPlugin().getConfig().getConfigurationSection("teams") != null) {
            for (String team : this.managerHandler.getPlugin().getConfig().getConfigurationSection("teams").getKeys(false)) {
                String leaderUUID = this.managerHandler.getPlugin().getConfig().getString("teams." + team + ".leader");
                List<String> members = this.managerHandler.getPlugin().getConfig().getStringList("teams." + team + ".members");
                Team loadedTeam = new Team(team, UUID.fromString(leaderUUID), members);
                addTeam(loadedTeam);
            }
        }
    }

    public boolean teamExists(String name) {
        for (Team team : teamList) {
            if (team.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Team getTeam(String name) {
        for (Team team : teamList) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void addTeam(Team team) {
        teamList.add(team);
    }

    public void removeTeam(Team team) {
        teamList.remove(team);
    }

    public void save() {
        this.managerHandler.getPlugin().getConfig().set("teams", null);
        for (Team team : teamList) {
            this.managerHandler.getPlugin().getConfig().set("teams." + team.getName() + ".leader", team.getLeader().toString());
            this.managerHandler.getPlugin().getConfig().set("teams." + team.getName() + ".members", team.getMembers());
        }
        this.managerHandler.getPlugin().saveConfig();
    }
}
