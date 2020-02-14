package de.jaylawl.tickmonitor;

import de.jaylawl.tickmonitor.listeners.TickListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new TickListener(), this);
    }

}
