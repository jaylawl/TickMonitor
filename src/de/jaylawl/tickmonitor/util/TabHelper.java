package de.jaylawl.tickmonitor.util;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabHelper {

    public static int getArgNumber(String[] args) {
        int i = 0;
        for (String arg : args) {
            if (!arg.equals("")) {
                i++;
            }
        }
        if (args[args.length - 1].equals("")) {
            i++;
        }
        return i;
    }

    public static List<String> sortedCompletions(String lastArg, List<String> completions) {
        List<String> sortedCompletions = new ArrayList<>();
        StringUtil.copyPartialMatches(lastArg, completions, sortedCompletions);
        Collections.sort(sortedCompletions);
        return sortedCompletions;
    }

}
