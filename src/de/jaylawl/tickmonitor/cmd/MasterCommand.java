package de.jaylawl.tickmonitor.cmd;

import de.jaylawl.tickmonitor.Main;
import de.jaylawl.tickmonitor.monitor.TickMonitor;
import de.jaylawl.tickmonitor.util.TabHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MasterCommand implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        int argNumber = TabHelper.getArgNumber(args);
        List<String> completions;

        if (argNumber == 1) {
            completions = new ArrayList<>(Arrays.asList(
                    "enable",
                    "disable",
                    "getlatest",
                    "monitor",
                    "reset"
            ));
        } else {
            return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(args[argNumber - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        String mainArg;

        if (args.length == 0) {
            mainArg = "getlatest";
        } else {
            mainArg = args[0].toLowerCase();
        }

        TickMonitor tickMonitor = Main.getTickMonitor();

        if (!tickMonitor.isEnabled() && Arrays.asList("getlatest", "get", "monitor", "reset").contains(mainArg)) {
            sender.sendMessage("§cTickMonitor is currently disabled; cancelling command");
            return true;
        }

        switch (mainArg) {

            case "enable":
            case "on":
                if (tickMonitor.isEnabled()) {
                    sender.sendMessage("TickMonitor is already enabled");
                } else {
                    tickMonitor.setEnabled(true);
                    sender.sendMessage("Successfully enabled & reset TickMonitor");
                }
                break;

            case "disable":
            case "off":
                if (!tickMonitor.isEnabled()) {
                    sender.sendMessage("TickMonitor is already disabled");
                } else {
                    tickMonitor.setEnabled(false);
                    sender.sendMessage("Successfully disabled TickMonitor");
                }
                break;

            case "getlatest":
            case "get":
                double[] averages = tickMonitor.getAverages();
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                sender.sendMessage(
                        "Latest MSPT" +
                                " | 1s: " + decimalFormat.format(averages[0]) + " ms" +
                                " | 10s: " + decimalFormat.format(averages[1]) + " ms" +
                                " | 1m: " + decimalFormat.format(averages[2]) + " ms"
                );
                break;

            case "monitor":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cIngame exclusive command");
                    return true;
                }
                Player player = ((Player) sender);
                if (tickMonitor.isMonitoring(player)) {
                    tickMonitor.removeMonitoringPlayer(player);
                    player.sendActionBar("§r ");
                } else {
                    tickMonitor.addMonitoringPlayer(player);
                }
                break;

            case "reset":
                tickMonitor.reset();
                sender.sendMessage("Successfully reset MSPT timings");
                break;
        }


        return true;
    }


}
