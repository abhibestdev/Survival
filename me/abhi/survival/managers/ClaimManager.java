package me.abhi.survival.managers;

import me.abhi.survival.claim.Claim;
import me.abhi.survival.manager.Manager;
import me.abhi.survival.manager.ManagerHandler;
import me.abhi.survival.util.Cuboid;
import me.abhi.survival.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimManager extends Manager {

    private List<Claim> claimList;

    public ClaimManager(ManagerHandler managerHandler) {
        super(managerHandler);
        this.claimList = new ArrayList<>();
        loadClaims();
    }

    private void loadClaims() {
        if (this.managerHandler.getPlugin().getConfig().getConfigurationSection("claims") != null) {
            for (String owner : this.managerHandler.getPlugin().getConfig().getConfigurationSection("claims").getKeys(false)) {
                Location location1 = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("claims." + owner + ".1"));
                Location location2 = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("claims." + owner + ".2"));
                List<UUID> permitees = new ArrayList<>();
                for (String permits : this.managerHandler.getPlugin().getConfig().getStringList("claims." + owner + ".permitees")) {
                    permitees.add(UUID.fromString(permits));
                }
                int cost = this.managerHandler.getPlugin().getConfig().getInt("claims." + owner + ".cost");
                Cuboid cuboid = new Cuboid(location1, location2);
                Claim claim = new Claim(UUID.fromString(owner), cuboid, permitees, cost);
                addClaim(claim);
            }
        }
    }

    public boolean isClaimed(Location location) {
        for (Claim claim : claimList) {
            if (claim.getCuboid().contains(location)) {
                return true;
            }
        }
        return false;
    }

    public boolean claimiableDistance(Location location, int distance) {
        for (Claim claim : claimList) {
            for (Block block : claim.getCuboid().getBlocks()) {
                if (block.getLocation().distance(location) <= distance) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addClaim(Claim claim) {
        claimList.add(claim);
    }

    public void removeClaim(Claim claim) {
        claimList.remove(claim);
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public Claim getClaim(UUID uuid) {
        for (Claim claim : claimList) {
            if (claim.getOwner().equals(uuid)) {
                return claim;
            }
        }
        return null;
    }

    public Claim getClaim(Location location) {
        for (Claim claim : claimList) {
            if (claim.getCuboid().contains(location)) {
                return claim;
            }
        }
        return null;
    }

    public boolean hasClaim(UUID uuid) {
        for (Claim claim : claimList) {
            if (claim.getOwner().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClaimable(Cuboid cuboid) {
        for (Claim claim : claimList) {
            for (Block block : claim.getCuboid().getBlocks()) {
                if (cuboid.contains(block.getLocation()))
                    return false;
            }
        }
        return true;
    }

    public void save() {
        this.managerHandler.getPlugin().getConfig().set("claims", null);
        for (Claim claim : claimList) {
            this.managerHandler.getPlugin().getConfig().set("claims." + claim.getOwner() + ".1", LocationUtil.getStringFromLocation(claim.getCuboid().getLocation1()));
            this.managerHandler.getPlugin().getConfig().set("claims." + claim.getOwner() + ".2", LocationUtil.getStringFromLocation(claim.getCuboid().getLocation2()));
            List<String> permits = new ArrayList<>();
            for (UUID uuid : claim.getPermitees()) {
                permits.add(uuid.toString());
            }
            this.managerHandler.getPlugin().getConfig().set("claims." + claim.getOwner() + ".permitees", permits);
            this.managerHandler.getPlugin().getConfig().set("claims." + claim.getOwner() + ".cost", (claim.getCost() != 0 ? claim.getCost() : 2000));
        }
        this.managerHandler.getPlugin().saveConfig();
    }
}
