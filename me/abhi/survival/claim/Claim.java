package me.abhi.survival.claim;

import me.abhi.survival.util.Cuboid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Claim {

    private UUID owner;
    private Cuboid cuboid;
    private List<UUID> permitees;
    private int cost;

    public Claim(UUID owner, Cuboid cuboid, int cost) {
        this.owner = owner;
        this.cuboid = cuboid;
        this.permitees = new ArrayList<>();
        this.cost = cost;
    }

    public Claim(UUID owner, Cuboid cuboid, List<UUID> permitees, int cost) {
        this.owner = owner;
        this.cuboid = cuboid;
        this.permitees = permitees;
        this.cost = cost;
    }


    public UUID getOwner() {
        return owner;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public List<UUID> getPermitees() {
        return permitees;
    }

    public int getCost() {
        return cost;
    }
}
