package de.luricos.bukkit.WormholeXTreme.Wormhole.events;

import org.bukkit.event.HandlerList;

/**
 * @author lycano
 */
public class WormholeSystemEvent extends WormholeEvent {
    protected Action action;
    private static final HandlerList handlers = new HandlerList();

    public WormholeSystemEvent(Action action) {
        super(action.toString());

        this.action = action;
    }

    public Action getAction() {
        return this.action;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Action {
        PERMISSION_BACKEND_CHANGED,
        RELOADED
    }
}
