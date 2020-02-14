package de.jaylawl.tickmonitor.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TickListener implements Listener {

    public TickListener() {
    }

    @EventHandler
    public void tickEnd(ServerTickEndEvent event) {
        double duration = event.getTickDuration();
        long remaining = event.getTimeRemaining();
        String msg = "MSPT: " + duration + " | Remaining nanos: " + remaining;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendActionBar(msg);
            }
        }
    }

}
