package me.abhi.survival.runnable;

import me.abhi.survival.Survival;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardRunnable extends BukkitRunnable {

    private Survival plugin;

    public ScoreboardRunnable(Survival plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getManagerHandler().getScoreboardManager().update(player);
        }
    }
}
