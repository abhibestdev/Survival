package me.abhi.survival.runnable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

public class TNTRunnable extends BukkitRunnable {

    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof TNTPrimed) {
                    entity.remove();
                }
            }
        }
    }
}
