/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.events;

import org.bukkit.event.HandlerList;

/**
 * @author lycano
 */
public class WormholeSystemEvent extends WormholeEvent {
    private Action action;
    private static final HandlerList handlers = new HandlerList();

    public WormholeSystemEvent(Action action) {
        super(action.toString());

        this.action = action;
    }

//    public Action getAction() {
//        return this.action;
//    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

//    public static HandlerList getHandlerList() {
//        return handlers;
//    }

    public enum Action {
        PERMISSION_BACKEND_CHANGED,
        RELOADED
    }
}
