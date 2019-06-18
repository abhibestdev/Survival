package me.abhi.survival.team;

public class TeamInvite {

    private Team team;
    private long timestamp;

    public TeamInvite(Team team) {
        this.team = team;
        this.timestamp = System.currentTimeMillis();
    }

    public Team getTeam() {
        return team;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
