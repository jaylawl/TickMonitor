package de.jaylawl.tickmonitor;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TickMonitor implements Listener {

    private boolean enabled = true;

    private int[] indices = new int[]{0, 0, 0};
    private double[] averages = new double[]{0d, 0d, 0d};
    private ConcurrentHashMap<Integer, Double> durations30s = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Double> durations1m = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Double> durations5m = new ConcurrentHashMap<>();

    private Collection<OfflinePlayer> monitoringPlayers = new ArrayList<>();

    public TickMonitor() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(ServerTickEndEvent event) {

        if (!this.enabled) {
            return;
        }

        double duration = event.getTickDuration();

        this.indices[0]++;
        this.indices[1]++;
        this.indices[2]++;

        if (this.indices[0] >= 600) {
            this.indices[0] = 1;
        }
        if (this.indices[1] >= 1200) {
            this.indices[1] = 1;
        }
        if (this.indices[2] >= 6000) {
            this.indices[2] = 1;
        }

        this.durations30s.put(this.indices[0], duration);
        this.durations1m.put(this.indices[1], duration);
        this.durations5m.put(this.indices[2], duration);

        double totalOneMin = 0d;
        double totalFiveMin = 0d;
        double totalFifteenMin = 0d;
        for (double d : this.durations30s.values()) {
            totalOneMin += d;
        }
        for (double d : this.durations1m.values()) {
            totalFiveMin += d;
        }
        for (double d : this.durations5m.values()) {
            totalFifteenMin += d;
        }
        this.averages[0] = totalOneMin / ((double) this.durations30s.size());
        this.averages[1] = totalFiveMin / ((double) this.durations1m.size());
        this.averages[2] = totalFifteenMin / ((double) this.durations5m.size());

        if (this.monitoringPlayers.size() > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String monitorOutput = (
                    "MSPT" +
                            " | now: " + decimalFormat.format(duration) + " ms" +
                            " | 30s: " + decimalFormat.format(this.averages[0]) + " ms" +
                            " | 1m: " + decimalFormat.format(this.averages[1]) + " ms" +
                            " | 5m: " + decimalFormat.format(this.averages[2]) + " ms"
            );
            for (OfflinePlayer offlinePlayer : this.monitoringPlayers) {
                if (offlinePlayer.isOnline()) {
                    ((Player) offlinePlayer).sendActionBar(monitorOutput);
                }
            }
        }

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!this.enabled) {
            this.monitoringPlayers.clear();
        } else {
            this.reset();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public double[] getAverages() {
        return this.averages;
    }

    public void reset() {
        this.indices = new int[]{0, 0, 0};
        this.averages = new double[]{0d, 0d, 0d};
        this.durations30s.clear();
        this.durations1m.clear();
        this.durations5m.clear();
    }

    public void addMonitoringPlayer(Player player) {
        this.monitoringPlayers.add(player);
    }

    public void removeMonitoringPlayer(Player player) {
        this.monitoringPlayers.remove(player);
    }

    public boolean isMonitoring(Player player) {
        return this.monitoringPlayers.contains(player);
    }

}
