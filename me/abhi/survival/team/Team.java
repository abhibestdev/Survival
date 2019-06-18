package me.abhi.survival.team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    public String name;
    public UUID leader;
    public List<String> members;

    public Team(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader.toString());
    }

    public Team(String name, UUID leader, List<String> members) {
        this.name = name;
        this.leader = leader;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }
}
