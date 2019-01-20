/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
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

import org.bukkit.entity.Minecart;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The Stargate Minecart Teleport Event Class.
 * 
 * @author alron
 */
public class StargateMinecartTeleportEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Minecart oldMinecart;
    private Minecart newMinecart;

    /**
     * Instantiates a new stargate minecart teleport event.
     * 
     * @param oldMinecart the old minecart
     * @param newMinecart the new minecart
     */
    public StargateMinecartTeleportEvent(Minecart oldMinecart, Minecart newMinecart) {
        this.oldMinecart = oldMinecart;
        this.newMinecart = newMinecart;
    }

//    /**
//     * Gets the new minecart.
//     *
//     * @return the new minecart
//     */
//    public Minecart getNewMinecart() {
//        return newMinecart;
//    }

//    /**
//     * Gets the old minecart.
//     *
//     * @return the old minecart
//     */
//    public Minecart getOldMinecart() {
//        return oldMinecart;
//    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

//    public static HandlerList getHandlerList() {
//        return handlers;
//    }
}
