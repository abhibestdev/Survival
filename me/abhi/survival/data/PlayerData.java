package me.abhi.survival.data;

import me.abhi.survival.Survival;
import me.abhi.survival.claim.Claim;
import me.abhi.survival.team.Team;
import me.abhi.survival.team.TeamInvite;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private int kills;
    private int deaths;
    private int balance;
    private Team team;
    private List<TeamInvite> teamInviteList;
    private Location location1;
    private Location location2;
    private Claim inClaim;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.teamInviteList = new ArrayList<>();
        Survival plugin = Survival.getInstance();
        if (plugin.getConfig().getConfigurationSection(uuid.toString()) != null) {
            kills = plugin.getConfig().getInt(uuid.toString() + ".kills");
            deaths = plugin.getConfig().getInt(uuid.toString() + ".deaths");
            balance = plugin.getConfig().getInt(uuid.toString() + ".balance");
        } else {
            balance = 500;
        }
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBalance() {
        return balance;
    }

    public Team getTeam() {
        return team;
    }

    public boolean hasTeam() {
        return team != null;
    }

    public List<TeamInvite> getTeamInviteList() {
        return teamInviteList;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public Claim getInClaim() {
        return inClaim;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public void setInClaim(Claim inClaim) {
        this.inClaim = inClaim;
    }

    public void save() {
        Survival plugin = Survival.getInstance();
        plugin.getConfig().set(uuid.toString() + ".kills", kills);
        plugin.getConfig().set(uuid.toString() + ".deaths", deaths);
        plugin.getConfig().set(uuid.toString() + ".balance", balance);
        plugin.saveConfig();
    }
}
