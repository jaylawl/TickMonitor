package de.jaylawl.tickmonitor.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MonitoringCycleCompleteEvent extends Event {

    public enum Timeframe {
        ONE_SECOND,
        TEN_SECONDS,
        ONE_MINUTE
    }

    private final static HandlerList HANDLER_LIST = new HandlerList();
    private final Timeframe timeframe;
    private final List<Double> data;

    public MonitoringCycleCompleteEvent(Timeframe timeframe, List<Double> data) {
        this.timeframe = timeframe;
        this.data = data;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Timeframe getTimeframe() {
        return this.timeframe;
    }

    public List<Double> getData() {
        return this.data;
    }

}
