/**
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme>
 *
 * Copyright (C) 2011  Ben Echols
 *                     Dean Bailey
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
package de.luricos.bukkit.WormholeXTreme.Wormhole.model;

import org.bukkit.entity.Player;

/**
 * StargateUsage helper class
 * 
 * @author lycano
 */
public class StargateUsage {

    private Player player;
    private Stargate stargate;
    private boolean hasPlayerUsedStargate = false;
    private boolean hasPlayerPermission = false;
    private boolean hasPlayerUseCooldown = false;
    private boolean hasPlayerReachedDestination = false;
    private boolean isGateIrisActive = false;
    private boolean isGateActive = false;

    public StargateUsage() {
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setStargate(Stargate stargate) {
        this.stargate = stargate;
    }

    public void setPlayerUsedStargate(boolean used) {
        this.hasPlayerUsedStargate = used;
    }

    public void setHasPlayerPermission(boolean has) {
        this.hasPlayerPermission = has;
    }

    public void setHasPlayerUseCooldown(boolean cooldown) {
        this.hasPlayerUseCooldown = cooldown;
    }

    public void  setIsGateIrisActive(boolean active) {
        this.isGateIrisActive = active;
    }

    public void setIsGateActive(boolean active) {
        this.isGateActive = active;
    }
    
    public void setPlayerReachedDestination(boolean destinationReached) {
        this.hasPlayerReachedDestination = destinationReached;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Stargate getStargate() {
        return this.stargate;
    }
    
    public StargateUsage getStargateUsage() {
        return this;
    }

    public boolean hasPlayerUsedStargate() {
        return this.hasPlayerUsedStargate;
    }

    public boolean hasPlayerPermission() {
        return this.hasPlayerPermission;
    }

    public boolean hasPlayerUseCooldown() {
        return this.hasPlayerUseCooldown;
    }
    
    public boolean hasPlayerReachedDestination() {
        return this.hasPlayerReachedDestination;
    }

    public boolean isGateIrisActive() {
        return this.isGateIrisActive;
    }

    public boolean isGateActive() {
        return this.isGateActive;
    }
}
